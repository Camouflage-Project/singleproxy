package com.alealogic.singleproxy.repository;

import com.alealogic.singleproxy.entity.DesktopClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DesktopClientRepository extends JpaRepository<DesktopClient, Long> {

    Optional<DesktopClient> findByKey(String key);
}
