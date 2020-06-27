package com.alealogic.singleproxy.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class PortResponse implements Serializable {

    private int port;
    private String ipId;
}
