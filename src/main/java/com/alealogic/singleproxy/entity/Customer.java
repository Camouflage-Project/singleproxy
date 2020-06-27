package com.alealogic.singleproxy.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String apiKey;
    private Integer enabledProxies;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public int getEnabledProxies() {
        return enabledProxies;
    }

    public void setEnabledProxies(Integer enabledProxies) {
        this.enabledProxies = enabledProxies;
    }
}
