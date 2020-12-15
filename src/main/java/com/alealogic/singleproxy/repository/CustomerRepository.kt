package com.alealogic.singleproxy.repository

import com.alealogic.singleproxy.entity.Customer
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository : JpaRepository<Customer?, Long?> {
    fun findCustomerByApiKey(apiKey: String?): Customer?
}