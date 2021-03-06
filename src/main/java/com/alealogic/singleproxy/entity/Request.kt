package com.alealogic.singleproxy.entity

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Request (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var customerId: Long? = null,
    var desktopClientId: Long? = null,
    var torContainerId: String? = null,
    var timestamp: LocalDateTime? = null
)
