package com.alealogic.singleproxy.entity

import com.alealogic.singleproxy.model.Os
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var apiKey: String? = null
    var enabledProxies: Int? = null
    var sessionToken: String? = null
    var os: Os? = null
}