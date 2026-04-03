package com.movieservice.domain.movie.model;

import com.movieservice.domain.movie.enums.MovieAgeRateType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Movie {

    private final Long movieId;

    private final String title;

    private final LocalDate releaseDate;

    private final MovieAgeRateType ageRate;

    @Builder(access = AccessLevel.PRIVATE)
    private Movie(Long movieId, String title, LocalDate releaseDate, MovieAgeRateType ageRate) {
        this.movieId = movieId;
        this.title = title;
        this.releaseDate = releaseDate;
        this.ageRate = ageRate;
    }

    public static Movie create(String title, LocalDate releaseDate, MovieAgeRateType ageRate) {
        return Movie.builder()
                .title(title)
                .releaseDate(releaseDate)
                .ageRate(ageRate)
                .build();
    }
}
