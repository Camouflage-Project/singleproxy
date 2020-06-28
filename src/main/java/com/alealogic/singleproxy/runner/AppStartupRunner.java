package com.alealogic.singleproxy.runner;

import com.alealogic.singleproxy.service.TorManager;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class AppStartupRunner implements ApplicationRunner {

    private final TorManager torManager;

    public AppStartupRunner(TorManager torManager) {
        this.torManager = torManager;
    }

    @Override
    public void run(ApplicationArguments args) {
        torManager.createTorContainers();
    }
}
