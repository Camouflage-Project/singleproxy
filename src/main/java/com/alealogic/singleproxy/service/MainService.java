package com.alealogic.singleproxy.service;

import com.alealogic.singleproxy.entity.BlacklistedIp;
import com.alealogic.singleproxy.entity.Customer;
import com.alealogic.singleproxy.model.BlacklistRequest;
import com.alealogic.singleproxy.model.PortDto;
import com.alealogic.singleproxy.repository.BlacklistedIpRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MainService {

    private final TorManager torManager;
    private final BlacklistedIpRepository blacklistedIpRepository;

    public MainService(TorManager torManager, BlacklistedIpRepository blacklistedIpRepository) {
        this.torManager = torManager;
        this.blacklistedIpRepository = blacklistedIpRepository;
    }

    public PortDto getTorPortForCustomer(Customer customer) {
        return torManager.getNextTorPortForCustomer(customer);
    }

    public void blacklistIp(Customer customer, BlacklistRequest blacklistRequest) {
        BlacklistedIp byCustomerIdAndIpId = blacklistedIpRepository.findByCustomerIdAndIpId(customer.getId(), blacklistRequest.getIpId());
        Optional.ofNullable(byCustomerIdAndIpId)
                .ifPresentOrElse(x -> {}, () -> createAndSaveBlacklistedIp(customer.getId(), blacklistRequest.getIpId()));
    }

    private void createAndSaveBlacklistedIp(Long customerId, String ipId) {
        BlacklistedIp blacklistedIp = new BlacklistedIp(customerId, ipId);
        blacklistedIpRepository.save(blacklistedIp);
    }
}
