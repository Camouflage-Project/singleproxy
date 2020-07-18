package com.alealogic.singleproxy.model;

import org.springframework.lang.NonNull;

import java.io.Serializable;

public class BlacklistRequest implements Serializable {

    private String ipId;

    public String getIpId() {
        return ipId;
    }

    public void setIpId(@NonNull String ipId) {
        this.ipId = ipId;
    }
}
