package com.alealogic.singleproxy.controller;

import com.alealogic.singleproxy.model.BlacklistRequest;
import com.alealogic.singleproxy.model.PortRequest;
import com.alealogic.singleproxy.model.PortResponse;
import com.alealogic.singleproxy.service.TorManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    private final TorManager torManager;

    public MainController(TorManager torManager) {
        this.torManager = torManager;
    }

    @PostMapping("port")
    public PortResponse port(@RequestBody PortRequest portRequest) {
        return torManager.getNextTorPort(portRequest.getApiKey());
    }

    @PostMapping("blacklist")
    public void blacklistProxy(@RequestBody BlacklistRequest blacklistRequest) {
        System.out.println(blacklistRequest);
    }
}
