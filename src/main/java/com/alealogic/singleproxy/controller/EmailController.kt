package com.alealogic.singleproxy.controller

import com.alealogic.singleproxy.model.EmailRequest
import com.alealogic.singleproxy.service.EmailService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin(origins = ["http://localhost:8080", "https://alealogic.com:443"])
class EmailController(private val emailService: EmailService) {

    @PostMapping("/email")
    fun submitEmail(@RequestBody emailRequest: EmailRequest) = emailService.submitEmail(emailRequest.email!!)

}