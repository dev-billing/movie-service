package com.movieservice.domain.common.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Money {

    private final int value;

    @Builder
    private Money(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("금액은 0 이상이어야 합니다.");
        }
        this.value = value;
    }

    public static Money create(int value) {
        return Money.builder()
                .value(value)
                .build();
    }
}
