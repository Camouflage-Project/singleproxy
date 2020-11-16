package com.alealogic.singleproxy.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class HeartbeatRequest implements Serializable {

    private String key;
    private String ip;

}
