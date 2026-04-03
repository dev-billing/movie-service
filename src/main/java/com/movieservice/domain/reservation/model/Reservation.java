package com.movieservice.domain.reservation.model;

import com.movieservice.domain.common.model.Money;
import com.movieservice.domain.reservation.enums.ReservationStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class Reservation {

    private final Long reservationId;

    private final Long userId;

    private final Long screenId;

    private ReservationStatus status;

    private final Money amount;

    private final List<ReservationSeat> seats;

    @Builder(access = AccessLevel.PRIVATE)
    private Reservation(Long reservationId, Long userId, Long screenId, ReservationStatus status, Money amount, List<ReservationSeat> seats) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.screenId = screenId;
        this.status = status;
        this.amount = amount;
        this.seats = seats;
    }

    public static Reservation create(Long reservationId, Long userId, Long screenId, ReservationStatus status, Money amount, List<ReservationSeat> seats) {
        return Reservation.builder()
                .reservationId(reservationId)
                .userId(userId)
                .screenId(screenId)
                .status(status)
                .amount(amount)
                .seats(seats)
                .build();
    }

    public static Reservation create(Long userId, Long screenId, Money amount, List<ReservationSeat> seats) {
        return Reservation.builder()
                .userId(userId)
                .screenId(screenId)
                .status(ReservationStatus.PENDING)
                .amount(amount)
                .seats(seats)
                .build();
    }

    public void confirmed() {
        if (this.status != ReservationStatus.PENDING) {
            throw new IllegalArgumentException("예약 확정은 대기 중인 예약만 가능합니다.");
        }
        this.status = ReservationStatus.CONFIRMED;
    }

    public void cancelling() {
        if (this.status == ReservationStatus.PENDING || this.status == ReservationStatus.CONFIRMED) {
            this.status = ReservationStatus.CANCELLING;
            return ;
        }
        throw new IllegalArgumentException("예약 취소 요청은 확정 예약만 가능합니다.");
    }

    public void cancel() {
        if (this.status != ReservationStatus.CANCELLING) {
            throw new IllegalArgumentException("예약 취소는 취소 대기중만 가능합니다.");
        }
        this.status = ReservationStatus.CANCELLED;
    }

    public void createFail() {
        if (this.status != ReservationStatus.PENDING) {
            throw new IllegalArgumentException("예약 취소는 취소 대기중만 가능합니다.");
        }
        this.status = ReservationStatus.CREATED_FAIL;
    }
}
