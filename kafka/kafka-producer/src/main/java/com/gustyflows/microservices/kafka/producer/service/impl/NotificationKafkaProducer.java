package com.gustyflows.microservices.kafka.producer.service.impl;

import com.gustyflows.microservices.kafka.avro.model.NotificationRequestAvroModel;
import com.gustyflows.microservices.kafka.producer.service.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.PreDestroy;

@Slf4j
@Service
public class NotificationKafkaProducer implements KafkaProducer<Long, NotificationRequestAvroModel> {

    private final KafkaTemplate<Long, NotificationRequestAvroModel> kafkaTemplate;

    public NotificationKafkaProducer(KafkaTemplate<Long, NotificationRequestAvroModel> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(String topicName, Long key, NotificationRequestAvroModel message) {
        log.info("Sending message='{}' to topic='{}'", message, topicName);

        ListenableFuture<SendResult<Long, NotificationRequestAvroModel>> sendResultFuture =
                kafkaTemplate.send(topicName, key, message);

        addCallback(topicName, message, sendResultFuture);
    }

    private void addCallback(
            String topicName,
            NotificationRequestAvroModel message,
            ListenableFuture<SendResult<Long, NotificationRequestAvroModel>> sendResultFuture) {

        sendResultFuture.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable exception) {
                log.error("Error while sending message {} to topic {}", message, topicName, exception);
            }

            @Override
            public void onSuccess(SendResult<Long, NotificationRequestAvroModel> result) {
                RecordMetadata recordMetadata = result.getRecordMetadata();
                log.debug("Received new metadata. Topic: {}; Partition: {}; Offset: {}; Timestamp: {}, at time {}",
                        recordMetadata.topic(),
                        recordMetadata.partition(),
                        recordMetadata.offset(),
                        recordMetadata.timestamp(),
                        System.nanoTime());
            }
        });
    }

    @PreDestroy
    public void close() {
        if (kafkaTemplate != null) {
            log.info("Closing kafka producer");
            kafkaTemplate.destroy();
        }
    }
}
