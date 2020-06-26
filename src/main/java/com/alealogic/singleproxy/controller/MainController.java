package com.alealogic.singleproxy.controller;

import com.alealogic.singleproxy.model.BlacklistRequest;
import com.alealogic.singleproxy.service.TorManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    private TorManager torManager;

    public MainController(TorManager torManager) {
        this.torManager = torManager;
    }

    @GetMapping("port")
    public int port() {
        return torManager.getNextTorPort();
    }

    @PostMapping("blacklist")
    public void blacklistProxy(@RequestBody BlacklistRequest blacklistRequest) {
        System.out.println(blacklistRequest);
    }
}
