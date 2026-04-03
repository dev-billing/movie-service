package com.movieservice.infra.event.payment.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieservice.domain.reservation.event.ReservationCancelEvent;
import com.movieservice.domain.reservation.service.ReservationService;
import com.movieservice.infra.event.payment.dto.PaymentCancelledEvent;
import com.movieservice.infra.event.payment.dto.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final ObjectMapper objectMapper;
    private final ReservationService reservationService;

    @KafkaListener(topics = "payment-completed", groupId = "movie-reservation")
    public void handleCompletedPayment(@Payload String message, Acknowledgment ack) {
        try {
            PaymentCompletedEvent event =
                    objectMapper.readValue(message, PaymentCompletedEvent.class);

            reservationService.confirmed(event.reservationId());
            ack.acknowledge();
        } catch (JsonProcessingException e) {
            log.error("역직렬화 실패 - Topic: payment-completed : {}", message, e);
            throw new RuntimeException("메시지 역직렬화 실패 : " + message);
        }
    }

    @KafkaListener(topics = "payment-cancelled", groupId = "movie-reservation")
    public void handleCancelledPayment(@Payload String message, Acknowledgment ack) {
        try {
            PaymentCancelledEvent event =
                    objectMapper.readValue(message, PaymentCancelledEvent.class);

            reservationService.cancel(event.reservationId());
            ack.acknowledge();
        } catch (JsonProcessingException e) {
            log.error("역직렬화 실패 - Topic: payment-completed : {}", message, e);
            throw new RuntimeException("메시지 역직렬화 실패 : " + message);
        }
    }

    @KafkaListener(topics = "payment-created-fail", groupId = "movie-reservation")
    public void handleCreatedPaymentFail(@Payload String message, Acknowledgment ack) {
        try {
            PaymentCancelledEvent event =
                    objectMapper.readValue(message, PaymentCancelledEvent.class);

            reservationService.cancel(event.reservationId());
            ack.acknowledge();
        } catch (JsonProcessingException e) {
            log.error("역직렬화 실패 - Topic: payment-completed : {}", message, e);
            throw new RuntimeException("메시지 역직렬화 실패 : " + message);
        }
    }
}
