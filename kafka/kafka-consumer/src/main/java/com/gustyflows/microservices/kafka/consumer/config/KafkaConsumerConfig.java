package com.gustyflows.microservices.kafka.consumer.config;

import com.gustyflows.microservices.kafka.properties.KafkaConfigProperties;
import com.gustyflows.microservices.kafka.properties.KafkaConsumerConfigProperties;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig<K extends Serializable, V extends SpecificRecordBase> {

    private final KafkaConsumerConfigProperties kafkaConsumerConfigProperties;
    private final KafkaConfigProperties kafkaConfigProperties;

    public KafkaConsumerConfig(KafkaConsumerConfigProperties kafkaConsumerConfigProperties, KafkaConfigProperties kafkaConfigProperties) {
        this.kafkaConsumerConfigProperties = kafkaConsumerConfigProperties;
        this.kafkaConfigProperties = kafkaConfigProperties;
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        HashMap<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigProperties.getBootstrapServers());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaConsumerConfigProperties.getKeyDeserializer());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaConsumerConfigProperties.getValueDeserializer());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerConfigProperties.getConsumerGroupId());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerConfigProperties.getAutoOffsetReset());
        props.put(kafkaConfigProperties.getSchemaRegistryUrlKey(), kafkaConfigProperties.getSchemaRegistryUrl());
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafkaConsumerConfigProperties.getSessionTimeoutMs());
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, kafkaConsumerConfigProperties.getHeartBeatInterval());
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, kafkaConsumerConfigProperties.getMaxPollIntervalMs());
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG,
                kafkaConsumerConfigProperties.getMaxPartitionFetchBytesDefault() *
                        kafkaConsumerConfigProperties.getMaxPartitionFetchBytesBoostFactor());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaConsumerConfigProperties.getMaxPollRecords());

        return props;
    }

    @Bean
    public ConsumerFactory<K, V> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<K, V>> kafkaListenerContainerFactory(
            ConsumerFactory<K, V> consumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<K, V> factory = new ConcurrentKafkaListenerContainerFactory<K, V>();
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(kafkaConsumerConfigProperties.getBatchListener());
        factory.setConcurrency(kafkaConsumerConfigProperties.getConcurrencyLevel());
        factory.setAutoStartup(kafkaConsumerConfigProperties.getAutoStartup());
        factory.getContainerProperties().setPollTimeout(kafkaConsumerConfigProperties.getPollTimeoutMs());
        return factory;
    }


}
