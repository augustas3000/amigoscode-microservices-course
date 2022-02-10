package com.gustyflows.fraud;

import com.gustyflows.clients.fraud.FraudCheckResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/fraud-check")
@AllArgsConstructor
public class FraudController {

    private final IFraudCheckService fraudCheckService;

    @GetMapping(path = "{customerId}")
    public FraudCheckResponse isFraudster(
            @PathVariable("customerId") Integer customerID) {
        log.info("Checking if the customer {\"customerId\" : {}} is fraudulent", customerID);
        boolean isFraudulent = fraudCheckService.isFraudulent(customerID);
        return new FraudCheckResponse(isFraudulent);
    }
}
