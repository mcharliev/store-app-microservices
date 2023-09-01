package com.programmingtechie.order.service.service;

import com.programmingtechie.order.service.event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public void sendMessage(OrderPlacedEvent orderPlacedEvent) {
        Message<OrderPlacedEvent> message = MessageBuilder
                .withPayload(orderPlacedEvent)
                .setHeader(KafkaHeaders.TOPIC, "notificationTopic")
                .build();
        kafkaTemplate.send(message);
    }
}
