package com.movieservice.presentation.reservation.controller;

import com.movieservice.presentation.common.response.ApiResponse;
import com.movieservice.presentation.reservation.dto.request.ReservationCreateRequestDto;
import com.movieservice.presentation.reservation.dto.response.ReservationCreateResponseDto;
import com.movieservice.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ApiResponse<ReservationCreateResponseDto> reserve(@RequestBody ReservationCreateRequestDto request) {
        return ApiResponse.ok(reservationService.reserve(request));
    }

    @PatchMapping("/{reservationId}")
    public ApiResponse<Void> canceling(@PathVariable long reservationId) {
        reservationService.canceling(reservationId);
        return ApiResponse.ok();
    }
}
