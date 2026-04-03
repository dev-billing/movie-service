package com.movieservice.infra.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public <T extends DomainEvent> void publish(String topic, T event) {
        log.info("Publishing event to topic {}: {}", topic, event);
        kafkaTemplate.send(topic, event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Event published successfully: {}", event);
                    } else {
                        log.error("Failed to publish event: {}", event, ex);
                    }
                });
    }
}
