package com.alealogic.singleproxy.controller

import com.alealogic.singleproxy.model.*
import com.alealogic.singleproxy.service.AuthService
import com.alealogic.singleproxy.service.ProxyService
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@CrossOrigin(
    origins = ["http://localhost:3000", "http://10.0.2.2:3000", "https://alealogic.com"],
    allowCredentials = "true"
)
class ProxyController(private val proxyService: ProxyService, private val authService: AuthService) {

    @PostMapping("proxy")
    fun getProxyInfo(@RequestBody proxyRequest: ProxyRequest): ProxyDto =
        authService.authenticateByApiKey(proxyRequest.apiKey)
            .let { proxyService.getProxyForCustomer(it) }


    @PostMapping("port")
    fun port(@RequestBody portRequest: PortRequest): PortDto {
        val customer = authService.authenticateByApiKey(portRequest.apiKey)
        return proxyService.getTorPortForCustomer(customer)
    }

    @PostMapping("blacklist")
    fun blacklistIp(
        @RequestHeader("Singleproxy-API-key") apiKey: String?,
        @RequestBody blacklistRequest: BlacklistRequest
    ) {
        val customer = authService.authenticateByApiKey(apiKey!!)
        proxyService.blacklistIp(customer, blacklistRequest)
    }

    @PostMapping("failed-proxy")
    fun reportFailedProxy(@RequestBody blacklistRequest: BlacklistRequest) {
        proxyService.handleProxyFailure(blacklistRequest.ipId)
    }

    @PostMapping("allPorts")
    fun getAllPortsForCustomer(@RequestBody portRequest: PortRequest): Collection<String> {
        val customer = authService.authenticateByApiKey(portRequest.apiKey)
        return proxyService.getAllPortsForCustomer(customer)
    }

    @GetMapping("proxy-status")
    fun getProxyStatusForCustomer(@RequestHeader("token") token: String) =
        proxyService.getProxyStatusForCustomer(token)

    @GetMapping("earnings")
    fun getCustomerEarnings(@RequestHeader("token") token: String) =
        proxyService.getCustomerEarnings(token)

    @GetMapping("request-timestamps/{page}")
    fun getCustomerRequestsHandledTimestamps(
        @RequestHeader("token") token: String,
        @PathVariable(value = "page") page: Int
    ): List<LocalDateTime> =
        proxyService.getCustomerRequestsHandledTimestamps(token, page)

    @GetMapping("api-key")
    fun getApiKey(@RequestHeader("token") token: String): String =
        proxyService.getCustomerApiKey(token)

    @PostMapping("accept-api-key")
    fun acceptApiKey(@RequestHeader("token") token: String) =
        proxyService.acceptApiKey(token)

}
