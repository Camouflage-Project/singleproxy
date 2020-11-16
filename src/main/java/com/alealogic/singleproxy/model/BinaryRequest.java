package com.alealogic.singleproxy.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class BinaryRequest implements Serializable {

    private String key;
    private String binaryName;

}
