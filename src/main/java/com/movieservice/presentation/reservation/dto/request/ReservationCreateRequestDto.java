package com.movieservice.presentation.reservation.dto.request;

import java.util.List;

public record ReservationCreateRequestDto(
        long userId,
        long screenId,
        List<Long> theaterSeatIds
) {
}
