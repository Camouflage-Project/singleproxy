package com.alealogic.singleproxy.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import kotlin.Throws
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

@RestController
class DownloadController {

    @GetMapping(value = ["alealogic-release"], produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    @Throws(
        IOException::class
    )
    fun getBinary(@CookieValue("token") tokenCookie: String, @Value("\${binary.path}") binaryPath: String): ByteArray {
        println(tokenCookie)
        return Files.readAllBytes(Paths.get(binaryPath))
    }
}