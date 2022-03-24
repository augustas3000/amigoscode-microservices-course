package com.gustyflows.customer;

import com.gustyflows.clients.fraud.FraudCheckResponse;
import com.gustyflows.clients.fraud.FraudServiceClient;
import com.gustyflows.clients.notification.NotificationRequest;
import com.gustyflows.customer.transformer.CustomerToNotificationRequestAvroModelTransformer;
import com.gustyflows.microservices.kafka.avro.model.NotificationAvroModel;
import com.gustyflows.microservices.kafka.producer.service.KafkaProducer;
import com.gustyflows.microservices.kafka.properties.KafkaConfigProperties;
import org.springframework.stereotype.Service;

@Service
public class DefaultCustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final FraudServiceClient fraudServiceClient;
    private final MessageSender messageSender;
    private final KafkaProducer<Long, NotificationAvroModel> kafkaProducer;
    private final KafkaConfigProperties kafkaConfigProperties;;
    private CustomerToNotificationRequestAvroModelTransformer customerToNotificationRequestAvroModelTransformer;

    public DefaultCustomerService(CustomerRepository customerRepository, FraudServiceClient fraudServiceClient, MessageSender messageSender, KafkaProducer<Long, NotificationAvroModel> kafkaProducer, KafkaConfigProperties kafkaConfigProperties, CustomerToNotificationRequestAvroModelTransformer customerToNotificationRequestAvroModelTransformer) {
        this.customerRepository = customerRepository;
        this.fraudServiceClient = fraudServiceClient;
        this.messageSender = messageSender;
        this.kafkaProducer = kafkaProducer;
        this.kafkaConfigProperties = kafkaConfigProperties;
        this.customerToNotificationRequestAvroModelTransformer = customerToNotificationRequestAvroModelTransformer;
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
                "sup");

        NotificationAvroModel avroModelFromCustomer =
                customerToNotificationRequestAvroModelTransformer.getNotificationAvroModelFromCustomer(customer);

        kafkaProducer.send(kafkaConfigProperties.getTopicName(), avroModelFromCustomer.getToCustomerId(), avroModelFromCustomer);
    }
}
