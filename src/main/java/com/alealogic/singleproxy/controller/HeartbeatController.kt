package com.alealogic.singleproxy.controller

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PostMapping
import com.alealogic.singleproxy.model.HeartbeatRequest
import com.alealogic.singleproxy.service.HeartbeatService
import org.springframework.web.bind.annotation.RequestBody

@RestController
class HeartbeatController(private val heartbeatService: HeartbeatService) {

    @PostMapping("heartbeat")
    fun heartbeat(@RequestBody heartbeatRequest: HeartbeatRequest) {
        heartbeatService.heartbeat(heartbeatRequest.key!!, heartbeatRequest.ip!!)
    }
}