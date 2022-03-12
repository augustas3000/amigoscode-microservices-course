package com.gustyflows.customer;

import com.gustyflows.clients.notification.NotificationRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(
        name = "messaging.broker", havingValue = "kafka"
)
@Component
public class KafkaMessageSender implements MessageSender {

    private final KafkaTemplate<String, NotificationRequest> kafkaTemplate;

    public KafkaMessageSender(KafkaTemplate<String, NotificationRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(NotificationRequest notificationRequest) {
        kafkaTemplate.send("customer-to-notification", notificationRequest);
    }
}
