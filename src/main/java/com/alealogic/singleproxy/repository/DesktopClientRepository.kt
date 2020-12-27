package com.alealogic.singleproxy.repository

import com.alealogic.singleproxy.entity.DesktopClient
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface DesktopClientRepository : JpaRepository<DesktopClient?, Long?> {
    fun findByKey(key: String?): DesktopClient?

    fun findByUpdateInitiatorDesktopClientId(id: Long?): DesktopClient?
}