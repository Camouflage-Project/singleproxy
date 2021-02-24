package com.alealogic.singleproxy.entity

import com.alealogic.singleproxy.model.Os
import javax.persistence.*

@Entity
data class Customer (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var apiKey: String? = null,
    var enabledProxies: Int? = null,
    var sessionToken: String? = null,
    var premium: Boolean = false,
    @Enumerated(EnumType.STRING)
    var os: Os? = null
)