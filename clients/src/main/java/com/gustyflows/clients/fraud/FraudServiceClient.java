package com.gustyflows.clients.fraud;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "fraud",
        url = "${clients.fraud.url}"
)
public interface FraudServiceClient {
    //this is our fraud-service client, and an interface which will target FraudController
    //any microservice that wants to talk to fraud-service, can use this interface
    @GetMapping(path = "api/v1/fraud-check/{customerId}")
    FraudCheckResponse isFraudster(@PathVariable("customerId") Integer customerID);
}
