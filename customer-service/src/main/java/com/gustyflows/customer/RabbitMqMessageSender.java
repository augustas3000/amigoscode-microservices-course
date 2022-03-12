package com.gustyflows.customer;

import com.gustyflows.amqp.RabbitMQMessageProducer;
import com.gustyflows.clients.notification.NotificationRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(
        name = "messaging.broker", havingValue = "rabbitmq"
)
@Component
public class RabbitMqMessageSender implements MessageSender {

    private final RabbitMQMessageProducer rabbitMQMessageProducer;

    public RabbitMqMessageSender(RabbitMQMessageProducer rabbitMQMessageProducer) {
        this.rabbitMQMessageProducer = rabbitMQMessageProducer;
    }

    @Override
    public void send(NotificationRequest notificationRequest) {
        rabbitMQMessageProducer.publish(notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );
        //todo: move exchange and key to a property class
    }
}
