package com.gustyflows.notification.messaging.rabbitmq;

import com.gustyflows.clients.notification.NotificationRequest;
import com.gustyflows.notification.DefaultNotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(
        name = "messaging.broker", havingValue = "rabbitmq"
)
@Component
@AllArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final DefaultNotificationService notificationService;

    @RabbitListener(queues = "${rabbitmq.queues.notification}")
    public void consume(NotificationRequest notificationRequest) {
        log.info("Consumed {} from queue", notificationRequest);
        notificationService.send(notificationRequest);
    }
}
