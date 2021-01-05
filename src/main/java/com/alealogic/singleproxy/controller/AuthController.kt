package com.alealogic.singleproxy.controller

import com.alealogic.singleproxy.model.ApiKeyLoginRequest
import com.alealogic.singleproxy.model.FetchTokenRequest
import com.alealogic.singleproxy.service.AuthService
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["http://localhost:3000", "http://10.0.2.2:3000"], allowCredentials = "true")
class AuthController(private val authService: AuthService) {

    @PostMapping("/api-key-login")
    fun loginWithApiKey(@RequestBody apiKeyLoginRequest: ApiKeyLoginRequest) =
        authService.loginWithApiKey(apiKeyLoginRequest.apiKey)

    @PostMapping("/token")
    fun fetchToken(@CookieValue(name = "token", required = false) sessionToken: String?, @RequestBody fetchTokenRequest: FetchTokenRequest) =
        authService.getToken(sessionToken, fetchTokenRequest.os!!)

}