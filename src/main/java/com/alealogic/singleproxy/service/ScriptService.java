package com.alealogic.singleproxy.service;

import com.alealogic.singleproxy.entity.Customer;
import com.alealogic.singleproxy.exception.ApiKeyAuthenticationException;
import com.alealogic.singleproxy.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ScriptService {

    private final CustomerRepository customerRepository;

    public ScriptService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer authenticateCustomer(String apiKey) {
        return Optional.ofNullable(customerRepository.findCustomerByApiKey(apiKey)).orElseThrow(ApiKeyAuthenticationException::new);
    }
}
