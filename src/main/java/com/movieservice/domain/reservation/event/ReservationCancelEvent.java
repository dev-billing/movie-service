package com.movieservice.domain.reservation.event;

import com.movieservice.infra.event.DomainEvent;

public record ReservationCancelEvent(
        long reservationId) implements DomainEvent {

}
