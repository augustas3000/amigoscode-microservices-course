package com.gustyflows.notification;

import com.gustyflows.amqp.RabbitMQMessageProducer;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(
//        scanBasePackages = {
//                "com.gustyflows.notification",
//                "com.gustyflows.amqp"
//        }
)
@EnableEurekaClient
@OpenAPIDefinition
public class NotificationApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }

//    @Bean
//    CommandLineRunner commandLineRunner(
//            RabbitMQMessageProducer rabbitMQMessageProducer,
//            NotificationConfig notificationConfig
//    ) {
//        return args -> {
//            rabbitMQMessageProducer.publish(
//                    new Person("Ali", 187),
//                    notificationConfig.getInternalExchange(),
//                    notificationConfig.getInternalNotificationRoutingKey());
//
//        };
//    }
//
//    record Person(String name, int age) {
//    }

}

