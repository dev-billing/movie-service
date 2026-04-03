package com.movieservice.infra.persistence.reservation.entity;

import com.movieservice.domain.reservation.model.ReservationSeat;
import com.movieservice.infra.persistence.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "reservation_seats")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationSeatEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_seat_id")
    private Long reservationSeatId;

    @Column(name ="reservation_id")
    private Long reservationId;

    @Column(name = "theater_seat_id", nullable = false)
    private Long theaterSeatId;

    @Builder(access = AccessLevel.PRIVATE)
    private ReservationSeatEntity(Long reservationId, Long theaterSeatId) {
        this.reservationId = reservationId;
        this.theaterSeatId = theaterSeatId;
    }

    public static ReservationSeatEntity from(ReservationSeat reservationSeat) {
        return ReservationSeatEntity.builder()
                .reservationId(reservationSeat.getReservationId())
                .theaterSeatId(reservationSeat.getTheaterSeatId())
                .build();
    }

    public ReservationSeat toDomain() {
        return ReservationSeat.create(reservationSeatId, reservationId, theaterSeatId);
    }

}