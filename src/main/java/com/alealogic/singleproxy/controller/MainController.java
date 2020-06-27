package com.alealogic.singleproxy.controller;

import com.alealogic.singleproxy.entity.Customer;
import com.alealogic.singleproxy.model.BlacklistRequest;
import com.alealogic.singleproxy.model.PortDto;
import com.alealogic.singleproxy.model.PortRequest;
import com.alealogic.singleproxy.service.AuthService;
import com.alealogic.singleproxy.service.MainService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    private final MainService mainService;
    private final AuthService authService;

    public MainController(MainService mainService, AuthService authService) {
        this.mainService = mainService;
        this.authService = authService;
    }

    @PostMapping("port")
    public PortDto port(@RequestBody PortRequest portRequest) {
        Customer customer = authService.authenticateCustomer(portRequest.getApiKey());
        return mainService.getTorPortForCustomer(customer);
    }

    @PostMapping("blacklist")
    public void blacklistIp(@RequestHeader("Singleproxy-API-key") String apiKey, @RequestBody BlacklistRequest blacklistRequest) {
        Customer customer = authService.authenticateCustomer(apiKey);
        mainService.blacklistIp(customer, blacklistRequest);
    }
}
