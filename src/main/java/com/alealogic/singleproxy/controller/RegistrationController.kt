package com.alealogic.singleproxy.controller

import org.springframework.web.bind.annotation.RestController
import com.alealogic.singleproxy.service.RegistrationService
import org.springframework.web.bind.annotation.PostMapping
import com.alealogic.singleproxy.model.KeyRequest
import org.springframework.web.bind.annotation.RequestBody

@RestController
class RegistrationController(private val registrationService: RegistrationService) {

    @PostMapping("register-desktop-client")
    fun registerDesktopClient(@RequestBody keyRequest: KeyRequest) {
        registrationService.registerDesktopClient(keyRequest.key)
    }
}