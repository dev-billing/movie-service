package com.movieservice.domain.reservation.model;

import com.movieservice.domain.common.model.Money;
import com.movieservice.domain.reservation.enums.ReservationStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static com.movieservice.domain.reservation.enums.ReservationStatus.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class ReservationTest {

    @Test
    @DisplayName("PENDING 상태만 예약 확정이 가능하다")
    void onlyPendingReservationCanBeConfirmed() {
        // given
        ReservationStatus status = PENDING;
        Reservation reservation = Reservation.create(1L, 1L, 1L, status, Money.create(10000), List.of());

        // when
        reservation.confirmed();

        // then
        assertThat(reservation.getStatus()).isEqualTo(CONFIRMED);
    }

    @ParameterizedTest
    @EnumSource(value = ReservationStatus.class, mode = EnumSource.Mode.EXCLUDE, names = "PENDING")
    @DisplayName("PENDING이 아닌 상태에서는 예약 확정이 불가능하다")
    void nonPendingReservationCannotBeConfirmed(ReservationStatus status) {
        // given
        Reservation reservation = Reservation.create(1L, 1L, 1L, status, Money.create(10000), List.of());

        // when & then
        assertThatThrownBy(reservation::confirmed)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 확정은 대기 중인 예약만 가능합니다.");
    }

    @Test
    @DisplayName("CONFIRMED 상태만 예약 확정이 가능하다")
    void confirmedReservationCanBeCanceled() {
        // given
        ReservationStatus status = CONFIRMED;
        Reservation reservation = Reservation.create(1L, 1L, 1L, status, Money.create(10000), List.of());

        // when
        reservation.cancelling();

        // then
        assertThat(reservation.getStatus()).isEqualTo(CANCELLING);
    }

    @ParameterizedTest
    @EnumSource(value = ReservationStatus.class, mode = EnumSource.Mode.EXCLUDE, names = "CONFIRMED")
    @DisplayName("CONFIRMED이 아닌 상태에서는 예약 확정이 불가능하다")
    void nonConfirmedReservationCannotBeCanceled(ReservationStatus status) {
        // given
        Reservation reservation = Reservation.create(1L, 1L, 1L, status, Money.create(10000), List.of());

        // when & then
        assertThatThrownBy(reservation::cancelling)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 취소 요청은 확정 예약만 가능합니다.");
    }

}