package com.movieservice.infra.persistence.movie.entity;

import com.movieservice.domain.movie.enums.MovieAgeRateType;
import com.movieservice.domain.movie.model.Movie;
import com.movieservice.infra.persistence.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "movies")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long movieId;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "age_rate", nullable = false)
    private MovieAgeRateType ageRate;

    @Builder(access = AccessLevel.PRIVATE)
    private MovieEntity(String title, LocalDate releaseDate, MovieAgeRateType ageRate) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.ageRate = ageRate;
    }

    public static MovieEntity from(Movie movie) {
        return MovieEntity.builder()
                .title(movie.getTitle())
                .releaseDate(movie.getReleaseDate())
                .ageRate(movie.getAgeRate())
                .build();
    }
}