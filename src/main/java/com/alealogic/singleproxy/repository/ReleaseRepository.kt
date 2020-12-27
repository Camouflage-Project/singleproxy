package com.alealogic.singleproxy.repository

import com.alealogic.singleproxy.entity.Release
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ReleaseRepository : JpaRepository<Release?, Long?> {

    @Query("select * from release order by id desc limit 1", nativeQuery = true)
    fun getLatestRelease(): Release

    fun findById(id: Long): Release

}