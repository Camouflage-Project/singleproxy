package com.alealogic.singleproxy.repository

import com.alealogic.singleproxy.entity.Request
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface RequestRepository : JpaRepository<Request?, Long?>{

    fun countAllByDesktopClientId(desktopClientId: Long): Long

    fun findByDesktopClientId(desktopClientId: Long, pageable: Pageable): List<Request>
}
