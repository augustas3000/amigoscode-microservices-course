package com.gustyflows.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@ConditionalOnProperty(
        name = "messaging.broker", havingValue="kafka"
)
@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic customerToNotificationTopic() {
        return TopicBuilder.name("customer-to-notification").build();
    }

}