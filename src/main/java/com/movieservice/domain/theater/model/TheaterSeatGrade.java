package com.movieservice.domain.theater.model;

import com.movieservice.domain.common.model.Money;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TheaterSeatGrade {

    private final Long theaterSeatGradeId;

    private final String gradeName;

    private final Money price;

    @Builder(access = AccessLevel.PRIVATE)
    private TheaterSeatGrade(Long theaterSeatGradeId, String gradeName, Money price) {
        this.theaterSeatGradeId = theaterSeatGradeId;
        this.gradeName = gradeName;
        this.price = price;
    }

    public static TheaterSeatGrade create(Long theaterSeatGradeId, String gradeName, Money price) {
        return TheaterSeatGrade.builder()
                .theaterSeatGradeId(theaterSeatGradeId)
                .gradeName(gradeName)
                .price(price)
                .build();
    }

    public static TheaterSeatGrade create(String gradeName, Money price) {
        return TheaterSeatGrade.builder()
                .gradeName(gradeName)
                .price(price)
                .build();
    }
}
