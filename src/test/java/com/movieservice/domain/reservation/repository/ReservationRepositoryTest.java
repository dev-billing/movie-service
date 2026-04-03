package com.movieservice.domain.reservation.repository;

import com.movieservice.IntegrationTestSupport;
import com.movieservice.domain.common.model.Money;
import com.movieservice.domain.reservation.model.Reservation;
import com.movieservice.domain.reservation.model.ReservationSeat;
import com.movieservice.infra.persistence.reservation.entity.ReservationEntity;
import com.movieservice.infra.persistence.reservation.entity.ReservationSeatEntity;
import com.movieservice.infra.persistence.reservation.repository.ReservationJpaRepository;
import com.movieservice.infra.persistence.reservation.repository.ReservationSeatJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.movieservice.domain.reservation.enums.ReservationStatus.PENDING;
import static com.movieservice.domain.reservation.enums.ReservationStatus.activateReservationStatus;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class ReservationRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationJpaRepository reservationJpaRepository;

    @Autowired
    private ReservationSeatJpaRepository reservationSeatJpaRepository;

    @Test
    @DisplayName("Reservation 객체로 새로운 예약을 생성한다.")
    void save() {
        // given
        long userId = 1L;
        long screenId = 1L;
        List<ReservationSeat> seats = List.of(
                ReservationSeat.create(1L),
                ReservationSeat.create(2L),
                ReservationSeat.create(3L)
        );

        Reservation reservation = Reservation.create(userId, screenId, Money.create(10000), seats);

        // when
        Reservation savedReservation = reservationRepository.save(reservation);

        // then
        assertThat(savedReservation.getReservationId()).isNotNull();
        assertThat(savedReservation.getUserId()).isEqualTo(userId);
        assertThat(savedReservation.getScreenId()).isEqualTo(screenId);
        assertThat(savedReservation.getStatus()).isEqualTo(PENDING);
        assertThat(savedReservation.getSeats()).hasSize(3);

        Optional<ReservationEntity> findReservation = reservationJpaRepository.findById(savedReservation.getReservationId());
        assertThat(findReservation).isPresent();
        assertThat(findReservation.get().getReservationId()).isEqualTo(savedReservation.getReservationId());

        List<ReservationSeatEntity> savedSeats = reservationSeatJpaRepository
                .findAll()
                .stream()
                .filter(seat -> seat.getReservationId().equals(savedReservation.getReservationId()))
                .toList();

        assertThat(savedSeats).hasSize(3);
        assertThat(savedSeats)
                .extracting(ReservationSeatEntity::getTheaterSeatId)
                .containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    @DisplayName("상영 일련번호와 예약할 좌석 번호들 중 하나라도 예약된 좌석이 포함된 경우 true 반환한다.")
    void hasReservationByScreenIdAndSeatIds(){
        // given
        long userId = 1L;
        long screenId = 1L;
        List<ReservationSeat> seats = List.of(
                ReservationSeat.create(1L),
                ReservationSeat.create(2L),
                ReservationSeat.create(3L)
        );
        Reservation reservation = Reservation.create(userId, screenId, Money.create(10000), seats);
        reservationRepository.save(reservation);

        // when
        boolean result = reservationRepository.hasReservationByScreenIdAndSeatIds(screenId, List.of(1L), activateReservationStatus());

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("상영 일련번호와 예약할 좌석 번호들 중 예약된 좌석이 하나도 없는 경우 false 반환한다.")
    void notHasReservationByScreenIdAndSeatIds(){
        // given
        long userId = 1L;
        long screenId = 1L;
        List<ReservationSeat> seats = List.of(
                ReservationSeat.create(1L),
                ReservationSeat.create(2L),
                ReservationSeat.create(3L)
        );
        Reservation reservation = Reservation.create(userId, screenId, Money.create(10000), seats);
        reservationRepository.save(reservation);

        // when
        boolean result = reservationRepository.hasReservationByScreenIdAndSeatIds(screenId, List.of(4L), activateReservationStatus());

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("예약이 되어 있는 자리를 예약할 때 데이터 충돌 예외가 발생한다.")
    void rebookReservedSeatThrowException(){
        // given
        long userId = 1L;
        long screenId = 1L;
        List<ReservationSeat> seats = List.of(
                ReservationSeat.create(1L),
                ReservationSeat.create(2L),
                ReservationSeat.create(3L)
        );
        Reservation reservation = Reservation.create(userId, screenId, Money.create(10000), seats);
        reservationRepository.save(reservation);

        // when
        // then
        assertThatThrownBy(() -> reservationRepository.save(reservation))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}