package com.alealogic.singleproxy.entity

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class DesktopClient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var key: String? = null
    var customerId: Long? = null
    var registered = false
    var releaseId: Long? = null
    var lastHeartbeat: LocalDateTime? = null
    var lastIp: String? = null
    var ldflags: String? = null
    var updateToReleaseId: Long? = null
    var obsolete: Boolean = false
    var updateInitiatorDesktopClientId: Long? = null
}