package com.gustyflows.customer;

import com.gustyflows.amqp.RabbitMQMessageProducer;
import com.gustyflows.clients.fraud.FraudCheckResponse;
import com.gustyflows.clients.fraud.FraudServiceClient;
import com.gustyflows.clients.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DefaultCustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final FraudServiceClient fraudServiceClient;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;

    @Override
    public void registerCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        Customer customer = Customer.builder()
                .firstName(customerRegistrationRequest.firstName())
                .lastName(customerRegistrationRequest.lastName())
                .email(customerRegistrationRequest.email())
                .build();

        customerRepository.saveAndFlush(customer);

        FraudCheckResponse fraudCheckResponse = fraudServiceClient.isFraudster(customer.getId());

        if (fraudCheckResponse.isFraudster()) {
            //todo: create custom exceptions + global exception mapper
            throw new IllegalStateException("Fraudster");
        }

        //todo: check if email valid
        //todo: check if email not taken

        NotificationRequest notificationRequest = new NotificationRequest(
                customer.getId(),
                customer.getEmail(),
                String.format("Hi %s, welcome to Amigoscode", customer.getFirstName()));

        //todo: move exchange and key to a property class
        rabbitMQMessageProducer.publish(notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );


    }
}
