package com.movieservice.infra.persistence.reservation.repository;

import com.movieservice.domain.reservation.enums.ReservationStatus;
import com.movieservice.infra.persistence.reservation.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long> {

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM ReservationEntity r " +
            "JOIN r.reservationSeats rs " +
            "WHERE r.screenId = :screenId " +
            "AND rs.theaterSeatId IN :theaterSeatIds " +
            "AND r.status IN :statuses")
    boolean existReservationByScreenIdAndSeatIds(long screenId,
                                                 Collection<Long> theaterSeatIds,
                                                 Collection<ReservationStatus> statuses);

}
