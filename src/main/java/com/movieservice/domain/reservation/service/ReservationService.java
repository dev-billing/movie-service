package com.movieservice.domain.reservation.service;

import com.movieservice.domain.common.model.Money;
import com.movieservice.domain.event.ReservationCreatedEvent;
import com.movieservice.domain.reservation.enums.ReservationStatus;
import com.movieservice.domain.reservation.event.ReservationCancelEvent;
import com.movieservice.domain.reservation.model.Reservation;
import com.movieservice.domain.reservation.model.ReservationSeat;
import com.movieservice.domain.reservation.repository.ReservationRepository;
import com.movieservice.domain.screen.repository.ScreenRepository;
import com.movieservice.domain.theater.model.TheaterSeat;
import com.movieservice.domain.theater.repository.TheaterRepository;
import com.movieservice.infra.event.EventPublisher;
import com.movieservice.infra.lock.DistributedLockHelper;
import com.movieservice.presentation.reservation.dto.request.ReservationCreateRequestDto;
import com.movieservice.presentation.reservation.dto.response.ReservationCreateResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ScreenRepository screenRepository;
    private final TheaterRepository theaterRepository;
    private final ReservationRepository reservationRepository;

    private final DistributedLockHelper distributedLockHelper;
    private final EventPublisher eventPublisher;

    @Transactional
    public ReservationCreateResponseDto reserve(ReservationCreateRequestDto request) {

        if (screenRepository.findByScreenId(request.screenId()).isEmpty()) {
            throw new IllegalArgumentException("존재하지 않은 상영 정보입니다.");
        }

        List<TheaterSeat> theaterSeats = theaterRepository.findAllTheaterSeatByTheaterSeatIds(request.theaterSeatIds());
        int totalAmount = theaterSeats.stream()
                .map(seat -> seat.getTheaterSeatGrade().getPrice())
                .mapToInt(Money::getValue)
                .sum();
        if (theaterSeats.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않은 좌석이 있습니다.");
        }

        String reservationLockKeyFormat = "lock:screenId:%s:seatId:%s";
        Set<String> lockKeys = request.theaterSeatIds()
                .stream()
                .map(seatId -> String.format(reservationLockKeyFormat, request.screenId(), seatId))
                .collect(Collectors.toSet());

        return distributedLockHelper.executeWithMultiLock(lockKeys, 3, 5, TimeUnit.SECONDS,
                () -> {
                    // 현재 해당 영화에 예약된 좌석이 있는지 검사
                    if (reservationRepository.hasReservationByScreenIdAndSeatIds(request.screenId(),
                            request.theaterSeatIds(),
                            ReservationStatus.activateReservationStatus())) {
                        throw new IllegalArgumentException("이미 예약된 좌석입니다.");
                    }

                    List<ReservationSeat> seats = request.theaterSeatIds().stream()
                            .map(ReservationSeat::create)
                            .toList();

                    Reservation reservation = Reservation.create(
                            request.userId(),
                            request.screenId(),
                            Money.create(totalAmount),
                            seats
                    );

                    Reservation savedReservation = reservationRepository.save(reservation);
                    eventPublisher.publish("reservation-created", ReservationCreatedEvent.create(savedReservation));
                    return ReservationCreateResponseDto.from(savedReservation);
                });
    }

    @Transactional
    public void confirmed(long reservationId) {
        Optional<Reservation> optionalReservation = reservationRepository.findByReservationId(reservationId);

        if (optionalReservation.isEmpty()) {
            throw new IllegalArgumentException("예약 정보를 찾을 수 없습니다.");
        }

        Reservation reservation = optionalReservation.get();
        reservation.confirmed();
        reservationRepository.update(reservation);
    }

    @Transactional
    public void canceling(long reservationId) {
        Optional<Reservation> optionalReservation = reservationRepository.findByReservationId(reservationId);

        if (optionalReservation.isEmpty()) {
            throw new IllegalArgumentException("예약 정보를 찾을 수 없습니다.");
        }

        Reservation reservation = optionalReservation.get();
        reservation.cancelling();
        reservationRepository.update(reservation);
        eventPublisher.publish("reservation-cancelled", new ReservationCancelEvent(reservationId));
    }

    @Transactional
    public void cancel(long reservationId) {
        Optional<Reservation> optionalReservation = reservationRepository.findByReservationId(reservationId);

        if (optionalReservation.isEmpty()) {
            throw new IllegalArgumentException("예약 정보를 찾을 수 없습니다.");
        }

        Reservation reservation = optionalReservation.get();
        reservation.createFail();
        reservationRepository.update(reservation);
    }


}
