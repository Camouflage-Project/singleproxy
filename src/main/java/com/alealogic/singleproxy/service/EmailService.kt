package com.alealogic.singleproxy.service

import com.alealogic.singleproxy.entity.Email
import com.alealogic.singleproxy.repository.EmailRepository
import org.springframework.stereotype.Service

@Service
class EmailService(private val emailRepository: EmailRepository) {

    fun submitEmail(email: String): Email = emailRepository.save(Email().apply { this.email = email })

}