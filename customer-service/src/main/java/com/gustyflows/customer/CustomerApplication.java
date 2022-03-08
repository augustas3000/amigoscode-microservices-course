package com.gustyflows.customer;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication(
        scanBasePackages = {
                "com.gustyflows.customer",
                "com.gustyflows.amqp",
                "com.gustyflows.kafka"
        }
)
@OpenAPIDefinition
@EnableFeignClients(
        basePackages = "com.gustyflows.clients"
)
@PropertySources({
        @PropertySource("classpath:clients-${spring.profiles.active}.properties") //property source with regard to open feign client config - default/kube - see clients/src/main/resources/clients-<profile>.properties
})
public class CustomerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(KafkaTemplate<String, String> kafkaTemplate) {
        return args -> {
            for (int i = 0; i < 10_000; i++) {
                String message = "hello kafka " + i;
                kafkaTemplate.send("customer-to-notification", message);
            }

        };
    }

}

/*
swagger ui by default available at http://localhost:{port}/swagger-ui/index.html
 */
