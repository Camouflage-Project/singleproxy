package com.alealogic.singleproxy.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProxyRequest implements Serializable {

    private String apiKey;
}
