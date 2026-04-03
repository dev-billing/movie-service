package com.movieservice.domain.screen.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Screen {

    private final Long screenId;

    private final Long theaterId;

    private final Long movieId;

    private final LocalDateTime screenDateTime;

    @Builder(access = AccessLevel.PRIVATE)
    private Screen(Long screenId, Long theaterId, Long movieId, LocalDateTime screenDateTime) {
        this.screenId = screenId;
        this.theaterId = theaterId;
        this.movieId = movieId;
        this.screenDateTime = screenDateTime;
    }

    public static Screen create(Long screenId, Long theaterId, Long movieId, LocalDateTime screenDateTime) {
        return Screen.builder()
                .screenId(screenId)
                .theaterId(theaterId)
                .movieId(movieId)
                .screenDateTime(screenDateTime)
                .build();
    }

    public static Screen create(Long theaterId, Long movieId, LocalDateTime screenDateTime) {
        return Screen.builder()
                .theaterId(theaterId)
                .movieId(movieId)
                .screenDateTime(screenDateTime)
                .build();
    }
}
