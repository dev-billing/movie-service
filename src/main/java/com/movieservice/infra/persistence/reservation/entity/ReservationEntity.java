package com.movieservice.infra.persistence.reservation.entity;

import com.movieservice.domain.common.model.Money;
import com.movieservice.domain.reservation.enums.ReservationStatus;
import com.movieservice.domain.reservation.model.Reservation;
import com.movieservice.infra.persistence.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "reservations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "screen_id", nullable = false)
    private Long screenId;

    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_status", nullable = false)
    private ReservationStatus status;

    @Column(name = "amount")
    private int amount;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", insertable = false, updatable = false)
    private List<ReservationSeatEntity> reservationSeats = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private ReservationEntity(Long reservationId, Long userId, Long screenId, ReservationStatus status, Money money, List<ReservationSeatEntity> reservationSeats) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.screenId = screenId;
        this.status = status;
        this.amount = money.getValue();
        this.reservationSeats = reservationSeats != null ? reservationSeats : new ArrayList<>();
    }

    public static ReservationEntity from(Reservation reservation) {
        return ReservationEntity.builder()
                .reservationId(reservation.getReservationId())
                .userId(reservation.getUserId())
                .screenId(reservation.getScreenId())
                .status(reservation.getStatus())
                .money(reservation.getAmount())
                .build();
    }

    public Reservation toDomain(List<ReservationSeatEntity> reservationSeats) {
        return Reservation.create(
                reservationId,
                userId,
                screenId,
                status,
                Money.create(amount),
                reservationSeats.stream()
                        .map(ReservationSeatEntity::toDomain)
                        .toList()
        );
    }

}