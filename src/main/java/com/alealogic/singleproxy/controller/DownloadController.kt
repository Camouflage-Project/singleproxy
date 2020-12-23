package com.alealogic.singleproxy.controller

import com.alealogic.singleproxy.service.FileService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

@RestController
@CrossOrigin(origins = ["http://localhost:8080", "http://localhost:3000"])
class DownloadController(private val fileService: FileService) {

    @GetMapping(value = ["alealogic-release"], produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    @Throws(IOException::class)
    fun getBinary(@CookieValue("token") sessionToken: String): ByteArray {
        return fileService.getBinaryForCustomer(sessionToken)
    }
}