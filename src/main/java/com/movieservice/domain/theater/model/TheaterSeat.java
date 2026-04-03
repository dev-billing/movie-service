package com.movieservice.domain.theater.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TheaterSeat {

    private final Long theaterSeatId;

    private final Long theaterId;

    private final TheaterSeatGrade theaterSeatGrade;

    private final String rowName;

    private final Integer seatNum;

    @Builder(access = AccessLevel.PRIVATE)
    private TheaterSeat(Long theaterSeatId, Long theaterId, TheaterSeatGrade theaterSeatGrade, String rowName, Integer seatNum) {
        this.theaterSeatId = theaterSeatId;
        this.theaterId = theaterId;
        this.theaterSeatGrade = theaterSeatGrade;
        this.rowName = rowName;
        this.seatNum = seatNum;
    }

    public static TheaterSeat create(Long theaterSeatId, Long theaterId, TheaterSeatGrade theaterSeatGrade, String rowName, Integer seatNum) {
        return TheaterSeat.builder()
                .theaterSeatId(theaterSeatId)
                .theaterId(theaterId)
                .theaterSeatGrade(theaterSeatGrade)
                .rowName(rowName)
                .seatNum(seatNum)
                .build();
    }

    public static TheaterSeat create(Long theaterId, TheaterSeatGrade theaterSeatGrade, String rowName, Integer seatNum) {
        return TheaterSeat.builder()
                .theaterId(theaterId)
                .theaterSeatGrade(theaterSeatGrade)
                .rowName(rowName)
                .seatNum(seatNum)
                .build();
    }
}
