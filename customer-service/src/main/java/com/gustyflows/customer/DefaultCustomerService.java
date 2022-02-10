package com.gustyflows.customer;

import com.gustyflows.clients.fraud.FraudCheckResponse;
import com.gustyflows.clients.fraud.FraudServiceClient;
import com.gustyflows.clients.notification.NotificationRequest;
import com.gustyflows.clients.notification.NotificationServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class DefaultCustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;
    private final FraudServiceClient fraudServiceClient;
    private final NotificationServiceClient notificationServiceClient;

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

        //todo: make it async, i.e add to queue
        notificationServiceClient.sendNotification(
                new NotificationRequest(
                        customer.getId(),
                        customer.getEmail(),
                        String.format("Hi %s, welcome to Amigoscode", customer.getFirstName())));

        //todo: check if email valid
        //todo: check if email not taken
    }
}
