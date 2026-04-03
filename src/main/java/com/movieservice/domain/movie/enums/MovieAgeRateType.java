package com.movieservice.domain.movie.enums;

import lombok.Getter;

@Getter
public enum MovieAgeRateType {

    ALL("전체관람가"),
    TWELVE("12세 이상 관람가"),
    FIFTEEN("15세 이상 관람가"),
    NINETEEN("19세 이상 관람가")
    ;

    private final String description;

    MovieAgeRateType(String description) {
        this.description = description;
    }
}
