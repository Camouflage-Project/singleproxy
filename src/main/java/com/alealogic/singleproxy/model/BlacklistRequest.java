package com.alealogic.singleproxy.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class BlacklistRequest implements Serializable {

    private String ipId;
}
