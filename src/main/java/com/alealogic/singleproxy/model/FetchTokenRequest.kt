package com.alealogic.singleproxy.model

data class FetchTokenRequest(var os: Os?) {
    constructor(): this(null)
}
