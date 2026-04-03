package com.movieservice.infra.persistence.screen.entity;

import com.movieservice.domain.screen.model.Screen;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "screens")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScreenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "screen_id")
    private Long screenId;

    @Column(name = "theater_id", nullable = false)
    private Long theaterId;

    @Column(name = "movie_id", nullable = false)
    private Long movieId;

    @Column(name = "screen_date_time", nullable = false)
    private LocalDateTime screenDateTime;

    @Builder(access = AccessLevel.PRIVATE)
    private ScreenEntity(Long theaterId, Long movieId, LocalDateTime screenDateTime) {
        this.theaterId = theaterId;
        this.movieId = movieId;
        this.screenDateTime = screenDateTime;
    }

    public static ScreenEntity of(Long theaterId, Long movieId, LocalDateTime screenDateTime) {
        return ScreenEntity.builder()
                .theaterId(theaterId)
                .movieId(movieId)
                .screenDateTime(screenDateTime)
                .build();
    }

    public static ScreenEntity from(Screen screen) {
        return ScreenEntity.builder()
                .theaterId(screen.getTheaterId())
                .movieId(screen.getMovieId())
                .screenDateTime(screen.getScreenDateTime())
                .build();
    }

    public Screen toDomain() {
        return Screen.create(screenId, theaterId, movieId, screenDateTime);
    }


}