package com.alealogic.singleproxy.controller

import com.alealogic.singleproxy.service.FileService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.io.IOException

@RestController
@CrossOrigin(origins = ["http://localhost:3000"], allowCredentials = "true")
class DownloadController(private val fileService: FileService) {

    @GetMapping(value = ["alealogic-release"], produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    @Throws(IOException::class)
    fun getBinary(@CookieValue("token") sessionToken: String): ByteArray =
        fileService.getBinaryBySessionToken(sessionToken)


    @GetMapping("install")
    fun getInstallationScript(@RequestParam("id") sessionToken: String): String =
        fileService.getInstallationScript(sessionToken)

}