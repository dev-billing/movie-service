package com.movieservice.presentation.reservation.controller;

import com.movieservice.ControllerTestSupport;
import com.movieservice.presentation.reservation.dto.request.ReservationCreateRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReservationControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("신규 예약을 등록한다.")
    void createReservation() throws Exception {
        // given
        long userId = 1L;
        long screenId = 1L;
        List<Long> theaterSeatIds = List.of(1L, 2L, 3L);

        ReservationCreateRequestDto request = new ReservationCreateRequestDto(
                userId,
                screenId,
                theaterSeatIds
        );

        // when
        // then
        mockMvc.perform(
                        post("/api/reservations")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("예약을 취소한다.")
    void cancelReservation() throws Exception {
        // given
        long reservationId = 1L;

        // when
        // then
        mockMvc.perform(
                        patch("/api/reservations/{reservationId}", reservationId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

}