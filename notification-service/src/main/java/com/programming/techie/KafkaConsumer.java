package com.programming.techie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = "notificationTopic",groupId = "notificationId")
    public void consumer(OrderPlacedEvent orderPlacedEvent){
        log.info("Received Notification for order -{}",orderPlacedEvent.getOrderNumber());
    }
}
