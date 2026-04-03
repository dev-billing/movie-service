package com.movieservice.infra.persistence.theater.repository;

import com.movieservice.infra.persistence.theater.entity.TheaterSeatGradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterSeatGradeJpaRepository extends JpaRepository<TheaterSeatGradeEntity, Long> {
}