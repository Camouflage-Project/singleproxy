package com.alealogic.singleproxy.service;

import com.alealogic.singleproxy.repository.DesktopClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RegistrationService {

    private final DesktopClientRepository desktopClientRepository;

    public RegistrationService(DesktopClientRepository desktopClientRepository) {
        this.desktopClientRepository = desktopClientRepository;
    }

    public void registerDesktopClient(String desktopClientKey) {
        final var byKey = desktopClientRepository.findByKey(desktopClientKey);
        final var desktopClient = byKey
                .orElseThrow(() -> {
                    log.info("no DesktopClient found in db with desktopClientKey " + desktopClientKey);
                    return new IllegalStateException("no ");
                });

        desktopClient.setRegistered(true);
        desktopClientRepository.save(desktopClient);
    }
}
