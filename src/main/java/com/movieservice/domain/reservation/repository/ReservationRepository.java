package com.movieservice.domain.reservation.repository;


import com.movieservice.domain.reservation.enums.ReservationStatus;
import com.movieservice.domain.reservation.model.Reservation;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    boolean hasReservationByScreenIdAndSeatIds(long screenId,
                                               List<Long> theaterSeatIds,
                                               Set<ReservationStatus> statuses);

    Optional<Reservation> findByReservationId(long reservationId);

    void update(Reservation reservation);

}
