package com.movieservice.infra.persistence.theater.entity;

import com.movieservice.domain.common.model.Money;
import com.movieservice.domain.theater.model.TheaterSeatGrade;
import com.movieservice.infra.persistence.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "theater_seat_grades")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TheaterSeatGradeEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theater_seat_grade_id")
    private Long theaterSeatGradeId;

    @Column(name = "grade_name", nullable = false, length = 50)
    private String gradeName;

    @Column(name = "price", nullable = false)
    private int price;

    @Builder(access = AccessLevel.PRIVATE)
    private TheaterSeatGradeEntity(String gradeName, Money money) {
        this.gradeName = gradeName;
        this.price = money.getValue();
    }

    public static TheaterSeatGradeEntity from(TheaterSeatGrade theaterSeatGrade) {
        return TheaterSeatGradeEntity.builder()
                .gradeName(theaterSeatGrade.getGradeName())
                .money(theaterSeatGrade.getPrice())
                .build();
    }

    public TheaterSeatGrade toDomain() {
        return TheaterSeatGrade.create(theaterSeatGradeId, gradeName, Money.create(price));
    }

}