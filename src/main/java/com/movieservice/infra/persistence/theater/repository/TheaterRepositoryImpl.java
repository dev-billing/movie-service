package com.movieservice.infra.persistence.theater.repository;

import com.movieservice.domain.theater.model.TheaterSeat;
import com.movieservice.domain.theater.repository.TheaterRepository;
import com.movieservice.infra.persistence.theater.entity.TheaterSeatEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TheaterRepositoryImpl implements TheaterRepository {

    private final TheaterSeatJpaRepository theaterSeatJpaRepository;

    @Override
    public List<TheaterSeat> findAllTheaterSeatByTheaterSeatIds(List<Long> theaterSeatIds) {
        return theaterSeatJpaRepository.findAllTheaterSeatByTheaterId(theaterSeatIds)
                .stream()
                .map(TheaterSeatEntity::toDomain)
                .toList();
    }
}
