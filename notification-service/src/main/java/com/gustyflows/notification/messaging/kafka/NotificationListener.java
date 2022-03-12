package com.gustyflows.notification.messaging.kafka;

import com.gustyflows.clients.notification.NotificationRequest;
import com.gustyflows.notification.DefaultNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(
        name = "messaging.broker", havingValue = "kafka"
)
@Component
@Slf4j
public class NotificationListener {

    private final DefaultNotificationService notificationService;

    public NotificationListener(DefaultNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(
            topics = "customer-to-notification",
            containerFactory = "notificationRequestListenerContainerFactory"
    )
    void listener(NotificationRequest notificationRequest) {
        //todo replace with custom object notification request
        log.info("Consumed {} from topic", notificationRequest);
        notificationService.send(notificationRequest);
    }
}
