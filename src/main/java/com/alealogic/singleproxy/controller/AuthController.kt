package com.alealogic.singleproxy.controller

import com.alealogic.singleproxy.model.ApiKeyLoginRequest
import com.alealogic.singleproxy.service.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(private val authService: AuthService) {

    @PostMapping("/api-key-login")
    fun loginWithApiKey(@RequestBody apiKeyLoginRequest: ApiKeyLoginRequest) =
        authService.loginWithApiKey(apiKeyLoginRequest.apiKey)
}