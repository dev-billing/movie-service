package com.movieservice.infra.persistence.theater.repository;

import com.movieservice.infra.persistence.theater.entity.TheaterSeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TheaterSeatJpaRepository extends JpaRepository<TheaterSeatEntity, Long> {

    @Query("SELECT ts " +
            "FROM TheaterSeatEntity ts " +
            "JOIN ts.theaterSeatGrade tsg " +
            "WHERE ts.theaterSeatId IN :theaterSeatIds")
    List<TheaterSeatEntity> findAllTheaterSeatByTheaterId(List<Long> theaterSeatIds);
}
