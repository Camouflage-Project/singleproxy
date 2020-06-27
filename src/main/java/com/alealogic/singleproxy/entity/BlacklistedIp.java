package com.alealogic.singleproxy.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class BlacklistedIp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long customerId;
    private String ipId;

    public BlacklistedIp() {
    }

    public BlacklistedIp(Long customerId, String ipId) {
        this.customerId = customerId;
        this.ipId = ipId;
    }
}
