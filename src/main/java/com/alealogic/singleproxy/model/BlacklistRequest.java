package com.alealogic.singleproxy.model;

import lombok.Data;
import org.springframework.lang.NonNull;

import java.io.Serializable;

@Data
public class BlacklistRequest implements Serializable {

    @NonNull
    private String ipId;
}
