package com.gustyflows.notification.messaging.kafka.consumer.impl;

import com.gustyflows.microservices.kafka.avro.model.NotificationRequestAvroModel;
import com.gustyflows.microservices.kafka.client.KafkaAdminClient;
import com.gustyflows.microservices.kafka.properties.KafkaConfigProperties;
import com.gustyflows.microservices.kafka.properties.KafkaConsumerConfigProperties;
import com.gustyflows.notification.INotificationService;
import com.gustyflows.notification.messaging.kafka.consumer.KafkaConsumer;
import com.gustyflows.notification.transformer.NotificationRequestAvroModelToNotificationRequestTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class NotificationKafkaConsumer implements KafkaConsumer<Long, NotificationRequestAvroModel> {

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private final KafkaAdminClient kafkaAdminClient;
    private final KafkaConfigProperties kafkaConfigProperties;
    private final KafkaConsumerConfigProperties kafkaConsumerConfigProperties;
    private final INotificationService notificationService;
    private final NotificationRequestAvroModelToNotificationRequestTransformer transformer;

    public NotificationKafkaConsumer(KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry, KafkaAdminClient kafkaAdminClient, KafkaConfigProperties kafkaConfigProperties, KafkaConsumerConfigProperties kafkaConsumerConfigProperties, INotificationService notificationService, NotificationRequestAvroModelToNotificationRequestTransformer transformer) {
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
        this.kafkaAdminClient = kafkaAdminClient;
        this.kafkaConfigProperties = kafkaConfigProperties;
        this.kafkaConsumerConfigProperties = kafkaConsumerConfigProperties;
        this.notificationService = notificationService;
        this.transformer = transformer;
    }

    @EventListener
    public void onAppStarted(ApplicationStartedEvent event) {
        kafkaAdminClient.checkTopicsCreated();
        log.info("Topics with name {} is ready for operations!", kafkaConfigProperties.getTopicNamesToCreate().toArray());
        Objects.requireNonNull(
                kafkaListenerEndpointRegistry.getListenerContainer(kafkaConsumerConfigProperties.getConsumerGroupId())
        ).start();
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.consumer-group-id}", topics = "${kafka-config.topic-name}")
    public void receive(@Payload List<NotificationRequestAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<Long> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number of messages received with keys {}, partitions {}, and offsets {}. Thread id {}",
                messages.size(), keys.toString(), partitions.toString(), offsets.toString(), Thread.currentThread());


        messages.stream()
                .map(transformer::getNotificationRequestFromNotificationRequestAvroModel)
                .forEach(notificationService::send);
    }
}
