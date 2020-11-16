package com.alealogic.singleproxy.controller;

import com.alealogic.singleproxy.model.HeartbeatRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeartbeatController {

    @PostMapping("heartbeat")
    public void heartbeat(@RequestBody HeartbeatRequest heartbeatRequest) {
        System.out.println(heartbeatRequest.getIp());
    }
}
