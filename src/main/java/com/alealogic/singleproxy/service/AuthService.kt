package com.alealogic.singleproxy.service

import com.alealogic.singleproxy.repository.CustomerRepository
import com.alealogic.singleproxy.entity.Customer
import com.alealogic.singleproxy.exception.AuthenticationException
import com.alealogic.singleproxy.model.Os
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService(private val customerRepository: CustomerRepository) {

    fun authenticateByApiKey(apiKey: String) =
        customerRepository.findCustomerByApiKey(apiKey) ?: throw AuthenticationException()

    fun authenticateBySessionToken(token: String) =
        customerRepository.findCustomerBySessionToken(token) ?: throw AuthenticationException()

    fun loginWithApiKey(apiKey: String?) =
        customerRepository.findCustomerByApiKey(apiKey)?.sessionToken ?: throw AuthenticationException()

    fun getToken(token: String?, os:Os): String {
        val customer = token?.let { customerRepository.findCustomerBySessionToken(token) }
            ?: Customer().apply {
            apiKey = UUID.randomUUID().toString()
            sessionToken = UUID.randomUUID().toString()
        }

        return customer
            .apply { this.os = os }
            .let { customerRepository.save(it) }
            .sessionToken!!
    }

}
