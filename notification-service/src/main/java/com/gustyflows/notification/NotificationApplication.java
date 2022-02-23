package com.gustyflows.notification;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication(
        scanBasePackages = {
                "com.gustyflows.notification",
                "com.gustyflows.amqp"
        }
)
@EnableEurekaClient
@OpenAPIDefinition
@PropertySources({
        @PropertySource("classpath:clients-${spring.profiles.active}.properties") //property source with regard to open feign client config - default/kube - see clients/src/main/resources/clients-<profile>.properties
})
public class NotificationApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }
}

