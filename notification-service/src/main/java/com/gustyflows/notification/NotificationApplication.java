package com.gustyflows.notification;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(
        scanBasePackages = {
                "com.gustyflows.notification",
                "com.gustyflows.amqp"
        }
)
@EnableEurekaClient
@OpenAPIDefinition
public class NotificationApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }
}

