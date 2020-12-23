package com.alealogic.singleproxy.controller

import com.alealogic.singleproxy.model.ApiKeyLoginRequest
import com.alealogic.singleproxy.service.AuthService
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["http://localhost:8080", "http://localhost:3000"])
class AuthController(private val authService: AuthService) {

    @PostMapping("/api-key-login")
    fun loginWithApiKey(@RequestBody apiKeyLoginRequest: ApiKeyLoginRequest) =
        authService.loginWithApiKey(apiKeyLoginRequest.apiKey)

    @GetMapping("/token")
    fun getToken(@CookieValue("token") token: String?): String = authService.getToken(token)

}