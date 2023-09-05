package com.programming.techie.kafka;

import com.programming.techie.event.OrderPlacedEvent;
import com.programming.techie.service.MailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.programming.techie.constant.Constant.EMAIL_SUBJECT;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
    private final MailSender mailSender;

    @KafkaListener(topics = "myTopic", groupId = "notificationId")
    public void consumer(OrderPlacedEvent orderPlacedEvent) {
        String message = "Спасибо за заказ, скоро наш менеджер свяжется с вами, ваш № заказа:"
                + orderPlacedEvent.getOrderNumber();
//        mailSender.sendMail(EMAIL_SUBJECT, message, orderPlacedEvent.getUserEmail());
        log.info("Received Notification for order -{}",orderPlacedEvent.getOrderNumber());
        log.info(orderPlacedEvent.getOrderNumber() + " " + orderPlacedEvent.getUserEmail());
    }
}
