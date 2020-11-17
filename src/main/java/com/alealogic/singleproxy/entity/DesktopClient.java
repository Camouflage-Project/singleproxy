package com.alealogic.singleproxy.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
public class DesktopClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String key;
    private Long customerId;
    private Long sessionId;
    private boolean registered;
    private String version;
    private LocalDateTime lastHeartbeat;
    private String lastIp;
    private String ldflags;
}
