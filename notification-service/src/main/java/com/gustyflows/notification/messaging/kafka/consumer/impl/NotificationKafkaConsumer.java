package com.gustyflows.notification.messaging.kafka.consumer.impl;

import com.gustyflows.microservices.kafka.avro.model.NotificationRequestAvroModel;
import com.gustyflows.microservices.kafka.client.KafkaAdminClient;
import com.gustyflows.microservices.kafka.properties.KafkaConfigProperties;
import com.gustyflows.microservices.kafka.properties.KafkaConsumerConfigProperties;
import com.gustyflows.notification.messaging.kafka.consumer.KafkaConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
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

    public NotificationKafkaConsumer(KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry, KafkaAdminClient kafkaAdminClient, KafkaConfigProperties kafkaConfigProperties, KafkaConsumerConfigProperties kafkaConsumerConfigProperties) {
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
        this.kafkaAdminClient = kafkaAdminClient;
        this.kafkaConfigProperties = kafkaConfigProperties;
        this.kafkaConsumerConfigProperties = kafkaConsumerConfigProperties;
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
                        @Header List<Long> keys,
                        @Header List<Integer> partitions,
                        @Header List<Long> offsets) {
        log.info("{} number of messages received with keys {}, partitions {}, and offsets {}. Thread id {}",
                messages.size(), keys.toString(), partitions.toString(), offsets.toString(), Thread.currentThread());

    }
}
