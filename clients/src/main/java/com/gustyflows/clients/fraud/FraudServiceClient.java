package com.gustyflows.clients.fraud;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "fraud-service",
        path = "api/v1/fraud-check"
)
public interface FraudServiceClient {
    //this is our fraud-service client, and an interface which will target FraudController
    //any microservice that wants to talk to fraud-service, can use this interface
    @GetMapping(path = "{customerId}")
    FraudCheckResponse isFraudster(@PathVariable("customerId") Integer customerID);
}
