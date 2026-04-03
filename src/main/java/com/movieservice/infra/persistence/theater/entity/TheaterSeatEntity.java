package com.movieservice.infra.persistence.theater.entity;

import com.movieservice.domain.theater.model.TheaterSeat;
import com.movieservice.infra.persistence.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "theater_seats")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TheaterSeatEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theater_seat_id")
    private Long theaterSeatId;

    @Column(name = "theater_id", nullable = false)
    private Long theaterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_seat_grade_id")
    private TheaterSeatGradeEntity theaterSeatGrade;

    @Column(name = "row_name", nullable = false, length = 10)
    private String rowName;

    @Column(name = "seat_num", nullable = false)
    private Integer seatNum;

    @Builder(access = AccessLevel.PRIVATE)
    private TheaterSeatEntity(Long theaterId, TheaterSeatGradeEntity theaterSeatGrade, String rowName, Integer seatNum) {
        this.theaterId = theaterId;
        this.theaterSeatGrade = theaterSeatGrade;
        this.rowName = rowName;
        this.seatNum = seatNum;
    }

    public static TheaterSeatEntity from(TheaterSeat theaterSeat, TheaterSeatGradeEntity theaterSeatGrade) {
        return TheaterSeatEntity.builder()
                .theaterId(theaterSeat.getTheaterId())
                .theaterSeatGrade(theaterSeatGrade)
                .rowName(theaterSeat.getRowName())
                .seatNum(theaterSeat.getSeatNum())
                .build();
    }

    public TheaterSeat toDomain() {
        return TheaterSeat.create(theaterSeatId, theaterId, theaterSeatGrade.toDomain(), rowName, seatNum);
    }
}