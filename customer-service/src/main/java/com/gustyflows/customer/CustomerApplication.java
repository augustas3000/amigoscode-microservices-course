package com.gustyflows.customer;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/*
when switching to rabbitmq messaging, ensure KafkaAutoConfiguration.class is excluded
to prevent application from trying to connect to kafka
 */

@SpringBootApplication(
        scanBasePackages = {
                "com.gustyflows"
        }
//        exclude = {KafkaAutoConfiguration.class}
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
}

/*
swagger ui by default available at http://localhost:{port}/swagger-ui/index.html
 */
