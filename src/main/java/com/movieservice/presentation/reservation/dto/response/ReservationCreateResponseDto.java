package com.movieservice.presentation.reservation.dto.response;


import com.movieservice.domain.reservation.enums.ReservationStatus;
import com.movieservice.domain.reservation.model.Reservation;
import com.movieservice.domain.reservation.model.ReservationSeat;

import java.util.List;

public record ReservationCreateResponseDto(
        long reservationId,
        long screenId,
        ReservationStatus status,
        int amount,
        List<Long> reservationSeatIds
) {

    public static ReservationCreateResponseDto from(Reservation reservation) {
        return new ReservationCreateResponseDto(reservation.getReservationId(),
                reservation.getScreenId(),
                reservation.getStatus(),
                reservation.getAmount().getValue(),
                reservation.getSeats().stream()
                        .map(ReservationSeat::getTheaterSeatId)
                        .toList());
    }
}
