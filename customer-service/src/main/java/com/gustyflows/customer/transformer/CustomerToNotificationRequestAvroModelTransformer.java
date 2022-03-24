package com.gustyflows.customer.transformer;

import com.gustyflows.customer.Customer;
import com.gustyflows.microservices.kafka.avro.model.NotificationRequestAvroModel;
import org.springframework.stereotype.Component;

@Component
public class CustomerToNotificationRequestAvroModelTransformer {

    public NotificationRequestAvroModel getNotificationRequestAvroModelFromCustomer(Customer customer) {
        return NotificationRequestAvroModel
                .newBuilder()
                .setToCustomerId(customer.getId())
                .setToCustomerEmail(customer.getEmail())
                .setMessage("heelloo")
                .build();
    }
}
