package com.alealogic.singleproxy.controller;

import com.alealogic.singleproxy.model.KeyRequest;
import com.alealogic.singleproxy.service.RegistrationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    private RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("register-desktop-client")
    public void registerDesktopClient(@RequestBody KeyRequest keyRequest) {
        registrationService.registerDesktopClient(keyRequest.getKey());
    }
}
