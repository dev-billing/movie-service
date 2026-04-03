package com.movieservice.infra.persistence.reservation.repository;

import com.movieservice.infra.persistence.reservation.entity.ReservationSeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationSeatJpaRepository extends JpaRepository<ReservationSeatEntity, Long> {
}
