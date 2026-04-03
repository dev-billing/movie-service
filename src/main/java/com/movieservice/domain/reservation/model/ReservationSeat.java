package com.movieservice.domain.reservation.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReservationSeat {

    private final Long reservationSeatId;

    private final Long reservationId;

    private final Long theaterSeatId;

    @Builder(access = AccessLevel.PRIVATE)
    private ReservationSeat(Long reservationSeatId, Long reservationId, Long theaterSeatId) {
        this.reservationSeatId = reservationSeatId;
        this.reservationId = reservationId;
        this.theaterSeatId = theaterSeatId;
    }

    public static ReservationSeat create(Long theaterSeatId) {
        return ReservationSeat.builder()
                .theaterSeatId(theaterSeatId)
                .build();
    }

    public static ReservationSeat create(Long reservationId, Long theaterSeatId) {
        return ReservationSeat.builder()
                .reservationId(reservationId)
                .theaterSeatId(theaterSeatId)
                .build();
    }

    public static ReservationSeat create(Long reservationSeatId, Long reservationId, Long theaterSeatId) {
        return ReservationSeat.builder()
                .reservationSeatId(reservationSeatId)
                .reservationId(reservationId)
                .theaterSeatId(theaterSeatId)
                .build();
    }

}
