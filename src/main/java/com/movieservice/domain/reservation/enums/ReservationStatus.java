package com.movieservice.domain.reservation.enums;

import lombok.Getter;

import java.util.Set;

@Getter
public enum ReservationStatus {

    PENDING("대기중", "결제 대기 중인 예약"),
    CREATED_FAIL("생성 실패", "예약 생성 실패"),
    CONFIRMED("확정", "결제가 완료된 예약"),
    CANCELLING("취소 처리중", "환불 진행 중인 예약"),
    CANCELLED("취소 완료", "사용자가 취소한 예약"),
    COMPLETED("완료", "관람이 완료된 예약");

    private final String displayName;

    private final String description;

    ReservationStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public static Set<ReservationStatus> activateReservationStatus() {
        return Set.of(PENDING, CONFIRMED);
    }
}
