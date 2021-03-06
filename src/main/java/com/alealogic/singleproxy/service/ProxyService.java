package com.alealogic.singleproxy.service;

import com.alealogic.singleproxy.entity.BlacklistedIp;
import com.alealogic.singleproxy.entity.Customer;
import com.alealogic.singleproxy.entity.DesktopClient;
import com.alealogic.singleproxy.entity.Request;
import com.alealogic.singleproxy.exception.NotFoundException;
import com.alealogic.singleproxy.model.BlacklistRequest;
import com.alealogic.singleproxy.model.PortDto;
import com.alealogic.singleproxy.model.ProxyDto;
import com.alealogic.singleproxy.repository.BlacklistedIpRepository;
import com.alealogic.singleproxy.repository.CustomerRepository;
import com.alealogic.singleproxy.repository.DesktopClientRepository;
import com.alealogic.singleproxy.repository.RequestRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProxyService {

    @Value("${proxy.host.relative.to.balancer}")
    private String hostRelativeToBalancer;
    @Value("${token.bits.earned.per.request}")
    private Integer tokenBitsEarnedPerRequest;
    private final TorManager torManager;
    private final PremiumProxyService premiumProxyService;
    private final BlacklistedIpRepository blacklistedIpRepository;
    private final DesktopClientRepository desktopClientRepository;
    private final RequestRepository requestRepository;
    private final AuthService authService;
    private final CustomerRepository customerRepository;

    public ProxyService(TorManager torManager,
                        PremiumProxyService premiumProxyService,
                        BlacklistedIpRepository blacklistedIpRepository,
                        DesktopClientRepository desktopClientRepository,
                        RequestRepository requestRepository,
                        AuthService authService,
                        CustomerRepository customerRepository) {
        this.torManager = torManager;
        this.premiumProxyService = premiumProxyService;
        this.blacklistedIpRepository = blacklistedIpRepository;
        this.desktopClientRepository = desktopClientRepository;
        this.requestRepository = requestRepository;
        this.authService = authService;
        this.customerRepository = customerRepository;
    }

    public ProxyDto getProxyForCustomer(Customer customer) {
        PortDto nextPortForCustomer = customer.getPremium()
                ? premiumProxyService.getNextPremiumPortForCustomer(customer)
                : torManager.getNextTorPortForCustomer(customer);

        final var request = new Request(
                null,
                customer.getId(),
                nextPortForCustomer.getDesktopClientId(),
                nextPortForCustomer.getTorContainerId(),
                LocalDateTime.now()
        );
        requestRepository.save(request);

        return new ProxyDto(hostRelativeToBalancer, nextPortForCustomer.getPort(), nextPortForCustomer.getIpId());
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

    public void handleProxyFailure(String ipId) {
        final var byIpId = desktopClientRepository.findByIpId(ipId);
        if (byIpId == null) return;
        byIpId.setActive(false);
        desktopClientRepository.save(byIpId);
    }

    public boolean getProxyStatusForCustomer(String token) {
        final var customerBySessionToken = authService.authenticateBySessionToken(token);
        return getDesktopClientByCustomerId(customerBySessionToken.getId()).getActive();
    }

    @NotNull
    public Long getCustomerEarnings(@NotNull String token) {
        final var desktopClient = getDesktopClientBySessionToken(token);
        final var count = requestRepository.countAllByDesktopClientId(desktopClient.getId());
        return count * tokenBitsEarnedPerRequest;
    }

    @NotNull
    public List<LocalDateTime> getCustomerRequestsHandledTimestamps(@NotNull String token, int page) {
        final var desktopClient = getDesktopClientBySessionToken(token);
        return requestRepository.findByDesktopClientId(desktopClient.getId(), PageRequest.of(page, 10))
                .stream()
                .map(Request::getTimestamp)
                .collect(Collectors.toList());
    }

    public String getCustomerApiKey(@NotNull String token) {
        final var customer = authService.authenticateBySessionToken(token);
        if (customer.getApiKeyDialogAccepted()) return "";
        return customer.getApiKey();
    }

    private DesktopClient getDesktopClientBySessionToken(String token) {
        final var customerBySessionToken = authService.authenticateBySessionToken(token);
        return getDesktopClientByCustomerId(customerBySessionToken.getId());
    }

    private DesktopClient getDesktopClientByCustomerId(Long customerId) {
        final var byCustomerId = desktopClientRepository.findByCustomerId(customerId);
        return Optional.ofNullable(byCustomerId).orElseThrow(NotFoundException::new);
    }

    public void acceptApiKey(@NotNull String token) {
        final var customer = authService.authenticateBySessionToken(token);
        customer.setApiKeyDialogAccepted(true);
        customerRepository.save(customer);
    }
}
