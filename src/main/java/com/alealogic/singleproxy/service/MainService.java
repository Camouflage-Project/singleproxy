package com.alealogic.singleproxy.service;

import com.alealogic.singleproxy.entity.BlacklistedIp;
import com.alealogic.singleproxy.entity.Customer;
import com.alealogic.singleproxy.model.BlacklistRequest;
import com.alealogic.singleproxy.model.PortDto;
import com.alealogic.singleproxy.model.ProxyDto;
import com.alealogic.singleproxy.repository.BlacklistedIpRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class MainService {

    @Value("host.relative.to.balancer")
    private String hostRelativeToBalancer;
    private final TorManager torManager;
    private final PremiumProxyService premiumProxyService;
    private final BlacklistedIpRepository blacklistedIpRepository;

    public MainService(TorManager torManager, PremiumProxyService premiumProxyService, BlacklistedIpRepository blacklistedIpRepository) {
        this.torManager = torManager;
        this.premiumProxyService = premiumProxyService;
        this.blacklistedIpRepository = blacklistedIpRepository;
    }

    public ProxyDto getProxyForCustomer(Customer customer) {
        PortDto nextTorPortForCustomer = customer.getPremium()
                ? premiumProxyService.getNextPremiumPortForCustomer(customer)
                : torManager.getNextTorPortForCustomer(customer);
        return new ProxyDto(hostRelativeToBalancer, nextTorPortForCustomer.getPort(), nextTorPortForCustomer.getIpId());
    }

    public PortDto getTorPortForCustomer(Customer customer) {
        return torManager.getNextTorPortForCustomer(customer);
    }

    public void blacklistIp(Customer customer, BlacklistRequest blacklistRequest) {
        BlacklistedIp byCustomerIdAndIpId = blacklistedIpRepository.findByCustomerIdAndIpId(customer.getId(), blacklistRequest.getIpId());
        Optional.ofNullable(byCustomerIdAndIpId)
                .ifPresentOrElse(x -> {
                }, () -> blacklistIp(customer, blacklistRequest.getIpId()));
    }

    private void blacklistIp(Customer customer, String ipId) {
        torManager.blacklistIp(customer, ipId);
        BlacklistedIp blacklistedIp = new BlacklistedIp(customer.getId(), ipId);
        blacklistedIpRepository.save(blacklistedIp);
    }

    public Collection<String> getAllPortsForCustomer(Customer customer) {
        return torManager.getAllPortsForCustomer(customer);
    }
}
