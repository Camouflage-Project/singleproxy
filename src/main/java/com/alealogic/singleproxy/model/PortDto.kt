package com.alealogic.singleproxy.model

import java.io.Serializable

data class PortDto(
    var port: Int? = null,
    val ipId: String? = null
) : Serializable