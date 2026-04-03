package com.movieservice.infra.event.payment.dto;

public record PaymentCompletedEvent(
        long reservationId
) {
}
