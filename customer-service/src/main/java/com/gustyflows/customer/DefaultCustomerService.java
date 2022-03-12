package com.gustyflows.customer;

import com.gustyflows.clients.fraud.FraudCheckResponse;
import com.gustyflows.clients.fraud.FraudServiceClient;
import com.gustyflows.clients.notification.NotificationRequest;
import org.springframework.stereotype.Service;

@Service
public class DefaultCustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final FraudServiceClient fraudServiceClient;
    private final MessageSender messageSender;

    public DefaultCustomerService(CustomerRepository customerRepository, FraudServiceClient fraudServiceClient, MessageSender messageSender) {
        this.customerRepository = customerRepository;
        this.fraudServiceClient = fraudServiceClient;
        this.messageSender = messageSender;
    }

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

        messageSender.send(notificationRequest);
    }
}
