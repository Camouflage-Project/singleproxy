package com.alealogic.singleproxy.repository;

import com.alealogic.singleproxy.entity.BlacklistedIp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface BlacklistedIpRepository extends JpaRepository<BlacklistedIp, Long> {

    Collection<BlacklistedIp> findAllByCustomerId(Long customerId);

    BlacklistedIp findByCustomerIdAndIpId(Long customerId, String ipId);
}
