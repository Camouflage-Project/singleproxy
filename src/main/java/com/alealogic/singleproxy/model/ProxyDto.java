package com.alealogic.singleproxy.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ProxyDto implements Serializable {

    private String host;
    private int port;
    private String ipId;
}
