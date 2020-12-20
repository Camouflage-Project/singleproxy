package com.alealogic.singleproxy.repository

import com.alealogic.singleproxy.entity.Email
import org.springframework.data.jpa.repository.JpaRepository

interface EmailRepository : JpaRepository<Email?, Long?>