package com.movieservice.infra.persistence.reservation.repository;

import com.movieservice.domain.reservation.enums.ReservationStatus;
import com.movieservice.domain.reservation.model.Reservation;
import com.movieservice.domain.reservation.model.ReservationSeat;
import com.movieservice.domain.reservation.repository.ReservationRepository;
import com.movieservice.infra.persistence.reservation.entity.ReservationEntity;
import com.movieservice.infra.persistence.reservation.entity.ReservationSeatEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepository {

    private final ReservationJpaRepository reservationJpaRepository;
    private final ReservationSeatJpaRepository reservationSeatJpaRepository;

    @Override
    public Reservation save(Reservation reservation) {
        ReservationEntity reservationEntity = ReservationEntity.from(reservation);
        ReservationEntity savedReservation = reservationJpaRepository.save(reservationEntity);

        Long reservationId = savedReservation.getReservationId();

        List<ReservationSeatEntity> reservationSeatEntities = reservation.getSeats().stream()
                .map(seat -> ReservationSeat.create(reservationId, seat.getTheaterSeatId()))
                .map(ReservationSeatEntity::from)
                .toList();

        List<ReservationSeatEntity> savedReservationSeatEntities = reservationSeatJpaRepository.saveAll(reservationSeatEntities);

        return savedReservation.toDomain(savedReservationSeatEntities);
    }

    @Override
    public boolean hasReservationByScreenIdAndSeatIds(long screenId, List<Long> theaterSeatIds, Set<ReservationStatus> statuses) {
        return reservationJpaRepository.existReservationByScreenIdAndSeatIds(screenId, theaterSeatIds, statuses);
    }

    @Override
    public Optional<Reservation> findByReservationId(long reservationId) {
        return reservationJpaRepository.findById(reservationId)
                .map(entity -> entity.toDomain(List.of()));
    }

    @Override
    public void update(Reservation reservation) {
        ReservationEntity reservationEntity = ReservationEntity.from(reservation);
        reservationJpaRepository.save(reservationEntity);
    }

}
