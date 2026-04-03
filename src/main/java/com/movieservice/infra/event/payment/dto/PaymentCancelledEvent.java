package com.movieservice.infra.event.payment.dto;

public record PaymentCancelledEvent(
        long reservationId
) {
}
