package com.gustyflows.customer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final ICustomerService defaultCustomerService;

    @Autowired
    public CustomerController(ICustomerService defaultCustomerService) {
        this.defaultCustomerService = defaultCustomerService;
    }

    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
        log.info("New customer registration {}", customerRegistrationRequest);
        defaultCustomerService.registerCustomer(customerRegistrationRequest);
    }
}
