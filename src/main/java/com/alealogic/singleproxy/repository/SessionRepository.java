package com.alealogic.singleproxy.repository;

import com.alealogic.singleproxy.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
}
