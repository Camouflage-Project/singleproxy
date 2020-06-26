package com.alealogic.singleproxy.service;

import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Service
public class PreDestroyService {

    private final TorManager torManager;

    public PreDestroyService(TorManager torManager) {
        this.torManager = torManager;
    }

    @PreDestroy
    public void destroy() {
        torManager.stopAndRemoveAllTorContainers();
    }
}
