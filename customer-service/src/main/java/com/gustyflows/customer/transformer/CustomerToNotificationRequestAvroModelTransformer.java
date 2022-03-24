package com.gustyflows.customer.transformer;

import com.gustyflows.customer.Customer;
import com.gustyflows.microservices.kafka.avro.model.NotificationAvroModel;
import org.springframework.stereotype.Component;

@Component
public class CustomerToNotificationRequestAvroModelTransformer {

    public NotificationAvroModel getNotificationAvroModelFromCustomer(Customer customer) {
        return NotificationAvroModel
                .newBuilder()
                .setToCustomerId(customer.getId())
                .setToCustomerEmail(customer.getEmail())
                .setMessage("heelloo")
                .build();
    }
}
