package com.alealogic.singleproxy.controller

import com.alealogic.singleproxy.model.EmailRequest
import com.alealogic.singleproxy.service.EmailService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin(
    origins = ["http://localhost:3000", "http://10.0.2.2:3000",
        "https://alealogic.com", "https://alealogic.com:3000",
        "https://www.alealogic.com:3000"
    ], allowCredentials = "true"
)
class EmailController(private val emailService: EmailService) {

    @PostMapping("/email")
    fun submitEmail(@RequestBody emailRequest: EmailRequest) = emailService.submitEmail(emailRequest.email!!)

}
