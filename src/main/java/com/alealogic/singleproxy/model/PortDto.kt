package com.alealogic.singleproxy.model

import java.io.Serializable

data class PortDto(
    val port: Int? = null,
    val ipId: String? = null,
    val desktopClientId: Long? = null,
    val torContainerId: String? = null
) : Serializable
