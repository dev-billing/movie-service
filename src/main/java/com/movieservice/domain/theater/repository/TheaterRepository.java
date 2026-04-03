package com.movieservice.domain.theater.repository;

import com.movieservice.domain.theater.model.TheaterSeat;

import java.util.List;

public interface TheaterRepository {

    List<TheaterSeat> findAllTheaterSeatByTheaterSeatIds(List<Long> theaterSeatIds);

}
