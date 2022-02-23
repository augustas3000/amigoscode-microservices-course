package com.gustyflows.fraud;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@EnableEurekaClient
@OpenAPIDefinition
@PropertySources({
        @PropertySource("classpath:clients-${spring.profiles.active}.properties") //property source with regard to open feign client config - default/kube - see clients/src/main/resources/clients-<profile>.properties
})
public class FraudApplication {
    public static void main(String[] args) {
        SpringApplication.run(FraudApplication.class, args);
    }
}
