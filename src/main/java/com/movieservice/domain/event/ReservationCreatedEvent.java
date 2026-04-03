package com.movieservice.domain.event;

import com.movieservice.domain.reservation.model.Reservation;
import com.movieservice.infra.event.DomainEvent;

public record ReservationCreatedEvent(
        long reservationId,
        int amount
) implements DomainEvent {

    public static ReservationCreatedEvent create(Reservation reservation) {
        return new ReservationCreatedEvent(reservation.getReservationId(), reservation.getAmount().getValue());
    }
}
