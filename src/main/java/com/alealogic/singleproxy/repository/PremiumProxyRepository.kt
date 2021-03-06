package com.alealogic.singleproxy.repository

import com.alealogic.singleproxy.entity.PremiumProxy
import org.springframework.data.jpa.repository.JpaRepository

interface PremiumProxyRepository : JpaRepository<PremiumProxy?, Long?>{

    fun findByIpId(ipId: String): PremiumProxy

    fun findAllByActiveTrue(): MutableList<PremiumProxy>

}
