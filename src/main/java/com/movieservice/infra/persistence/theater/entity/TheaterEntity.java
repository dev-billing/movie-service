package com.movieservice.infra.persistence.theater.entity;

import com.movieservice.domain.theater.model.Theater;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "theaters")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TheaterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theater_id")
    private Long theaterId;

    @Column(name = "theater_name", nullable = false, length = 100)
    private String theaterName;

    @Builder(access = AccessLevel.PRIVATE)
    private TheaterEntity(String theaterName) {
        this.theaterName = theaterName;
    }

    public static TheaterEntity from(Theater theater) {
        return TheaterEntity.builder()
                .theaterName(theater.getTheaterName())
                .build();
    }
}