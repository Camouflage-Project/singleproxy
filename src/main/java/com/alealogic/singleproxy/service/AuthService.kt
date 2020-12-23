package com.alealogic.singleproxy.service

import com.alealogic.singleproxy.repository.CustomerRepository
import com.alealogic.singleproxy.entity.Customer
import com.alealogic.singleproxy.exception.ApiKeyAuthenticationException
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService(private val customerRepository: CustomerRepository) {

    fun authenticateCustomer(apiKey: String) =
        customerRepository.findCustomerByApiKey(apiKey) ?: throw ApiKeyAuthenticationException()

    fun loginWithApiKey(apiKey: String?) =
        customerRepository.findCustomerByApiKey(apiKey)?.sessionToken ?: throw ApiKeyAuthenticationException()

    fun getToken(token: String?): String =
        customerRepository.findCustomerBySessionToken(token)?.sessionToken
            ?: Customer().apply {
                apiKey = UUID.randomUUID().toString()
                sessionToken = UUID.randomUUID().toString()
            }.let { customerRepository.save(it) }.sessionToken!!

}