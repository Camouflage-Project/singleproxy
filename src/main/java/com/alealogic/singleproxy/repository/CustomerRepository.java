package com.alealogic.singleproxy.repository;

import com.alealogic.singleproxy.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findCustomerByApiKey(String apiKey);
}
