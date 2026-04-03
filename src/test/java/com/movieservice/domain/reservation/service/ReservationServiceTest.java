package com.movieservice.domain.reservation.service;

import com.movieservice.IntegrationTestSupport;
import com.movieservice.presentation.reservation.dto.request.ReservationCreateRequestDto;
import com.movieservice.presentation.reservation.dto.response.ReservationCreateResponseDto;
import com.movieservice.domain.common.model.Money;
import com.movieservice.domain.reservation.enums.ReservationStatus;
import com.movieservice.domain.reservation.model.Reservation;
import com.movieservice.domain.reservation.model.ReservationSeat;
import com.movieservice.infra.persistence.reservation.entity.ReservationEntity;
import com.movieservice.infra.persistence.reservation.repository.ReservationJpaRepository;
import com.movieservice.infra.persistence.reservation.repository.ReservationSeatJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.movieservice.domain.reservation.enums.ReservationStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationServiceTest extends IntegrationTestSupport {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationJpaRepository reservationJpaRepository;

    @Autowired
    private ReservationSeatJpaRepository reservationSeatJpaRepository;

    @AfterEach
    void tearDown() {
        reservationSeatJpaRepository.deleteAllInBatch();
        reservationJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("예약 요청 정보로 새로운 예약을 생성한다.")
    void reserve() {
        // given
        long userId = 1L;
        long screenId = 1L;
        List<Long> theaterSeatIds = List.of(1L, 2L, 3L);

        ReservationCreateRequestDto request = new ReservationCreateRequestDto(
                userId,
                screenId,
                theaterSeatIds
        );

        // when
        ReservationCreateResponseDto response = reservationService.reserve(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.reservationId()).isGreaterThan(0L);
        assertThat(response.status()).isEqualTo(PENDING);
        assertThat(response.amount()).isEqualTo(36000);
        assertThat(response.reservationSeatIds()).hasSize(3)
                .containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    @DisplayName("존재하지 않은 영화 상영은 예약할 수 없다.")
    void reserveWithWrongScreen(){
        // given
        long userId = 1L;
        ReservationCreateRequestDto request = new ReservationCreateRequestDto(
                userId,
                -1L,
                List.of(1L)
        );

        // when
        // then
        assertThatThrownBy(() -> reservationService.reserve(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않은 상영 정보입니다.");
    }

    @Test
    @DisplayName("존재하지 않은 좌석은 예약할 수 없다.")
    void reserveWithWrongTheaterSeat(){
        // given
        long userId = 1L;
        ReservationCreateRequestDto request = new ReservationCreateRequestDto(
                userId,
                1L,
                List.of(-1L)
        );

        // when
        // then
        assertThatThrownBy(() -> reservationService.reserve(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않은 좌석이 있습니다.");
    }

    @Test
    @DisplayName("예약이 되어 있는 좌석은 예약할 수 없다.")
    void reserveSameSeat(){
        // given
        long userId = 1L;
        ReservationCreateRequestDto request = new ReservationCreateRequestDto(
                userId,
                1L,
                List.of(1L, 2L)
        );
        reservationService.reserve(request);

        // when
        // then
        assertThatThrownBy(() -> reservationService.reserve(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 예약된 좌석입니다.");

    }

    @Test
    @DisplayName("동시에 예약하는 경우 중복 예약을 방지해야 한다.")
    void sameTimeReserveNotDuplicatedReservation() throws Exception {
        // given
        long screenId = 1L;
        List<Long> theaterSeatIds = List.of(1L, 2L);
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        List<Exception> exceptions = Collections.synchronizedList(new ArrayList<>());

        // when
        for (int i = 0; i < threadCount; i++) {
            final long userId = i;
            executorService.submit(() -> {
                try {
                    readyLatch.countDown();
                    startLatch.await(); // 모든 스레드가 준비될 때까지 대기

                    ReservationCreateRequestDto request = new ReservationCreateRequestDto(
                            userId,
                            screenId,
                            theaterSeatIds
                    );
                    reservationService.reserve(request);

                } catch (Exception e) {
                    exceptions.add(e);
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        readyLatch.await(); // 모든 스레드가 준비될 때까지 대기
        startLatch.countDown(); // 동시에 시작!
        doneLatch.await();

        executorService.shutdown();

        // then
        List<ReservationEntity> reservations = reservationJpaRepository.findAll();
        assertThat(reservations).hasSize(1);
        assertThat(exceptions)
                .hasSize(9)
                .allMatch(e -> e instanceof IllegalArgumentException)
                .extracting(Throwable::getMessage)
                .containsOnly("이미 예약된 좌석입니다.");

    }

    @Test
    @DisplayName("예약을 취소한다.")
    void cancelReservation(){
        // given
        ReservationEntity reservationEntity = createReservation();
        long reservationId = reservationEntity.getReservationId();

        ReservationStatus status = CONFIRMED;
        Reservation reservation = Reservation.create(reservationId, 1L, 1L, status, Money.create(10000), List.of());
        reservationJpaRepository.save(ReservationEntity.from(reservation));

        // when
        reservationService.cancel(reservationId);

        // then
        ReservationEntity cancelReservation = reservationJpaRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다."));

        assertThat(cancelReservation.getReservationId()).isEqualTo(reservationId);
        assertThat(cancelReservation.getStatus()).isEqualTo(CANCELLED);
    }

    @Test
    @DisplayName("예약 취소시 예약 정보를 가지고 없다면 예외가 발생한다.")
    void cancelReservationNotFoundReservation(){
        // given
        long reservationId = -1;

        // when
        // then
        assertThatThrownBy(() -> reservationService.cancel(reservationId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 정보를 찾을 수 없습니다.");
    }

    private ReservationEntity createReservation() {
        long userId = 1L;
        long screenId = 1L;
        int money = 10000;
        List<Long> theaterSeatIds = List.of(1L, 2L, 3L);
        List<ReservationSeat> seats = theaterSeatIds.stream()
                .map(ReservationSeat::create)
                .toList();

        Reservation reservation = Reservation.create(userId, screenId, Money.create(money), seats);
        return reservationJpaRepository.save(ReservationEntity.from(reservation));
    }

}