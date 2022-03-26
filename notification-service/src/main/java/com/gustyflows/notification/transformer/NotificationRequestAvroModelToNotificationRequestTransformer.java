package com.gustyflows.notification.transformer;

import com.gustyflows.clients.notification.NotificationRequest;
import com.gustyflows.microservices.kafka.avro.model.NotificationRequestAvroModel;
import org.springframework.stereotype.Component;

@Component
public class NotificationRequestAvroModelToNotificationRequestTransformer {
    public NotificationRequest getNotificationRequestFromNotificationRequestAvroModel(NotificationRequestAvroModel notificationRequestAvroModel) {
        return new NotificationRequest(
                notificationRequestAvroModel.getToCustomerId(),
                notificationRequestAvroModel.getToCustomerEmail(),
                notificationRequestAvroModel.getMessage());
    }
}
