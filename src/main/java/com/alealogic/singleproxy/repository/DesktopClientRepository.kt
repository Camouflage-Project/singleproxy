package com.alealogic.singleproxy.repository

import com.alealogic.singleproxy.entity.DesktopClient
import org.springframework.data.jpa.repository.JpaRepository

interface DesktopClientRepository : JpaRepository<DesktopClient?, Long?> {
    fun findByKey(key: String?): DesktopClient?

    fun findByUpdateInitiatorDesktopClientId(id: Long?): DesktopClient?

    fun findAllByActiveTrue(): MutableSet<DesktopClient>

    fun findByIpId(ipId: String): DesktopClient?

    fun findByCustomerId(customerId: Long): DesktopClient?
}
