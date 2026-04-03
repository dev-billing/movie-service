package com.movieservice.domain.theater.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Theater {

    private final Long theaterId;

    private final String theaterName;

    @Builder(access = AccessLevel.PRIVATE)
    private Theater(Long theaterId, String theaterName) {
        this.theaterId = theaterId;
        this.theaterName = theaterName;
    }

    public static Theater create(String theaterName) {
        return Theater.builder()
                .theaterName(theaterName)
                .build();
    }
}
