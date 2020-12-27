package com.alealogic.singleproxy.controller

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PostMapping
import kotlin.Throws
import java.io.IOException
import com.alealogic.singleproxy.model.BinaryRequest
import com.alealogic.singleproxy.model.KeyRequest
import com.alealogic.singleproxy.service.UpdateService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import java.nio.file.Files
import java.nio.file.Paths

@RestController
class UpdateController(private val updateService: UpdateService) {

    @PostMapping(value = ["binary"], produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    @Throws(
        IOException::class
    )
    fun getBinary(
        @RequestBody keyRequest: KeyRequest
    ): ByteArray {
        return updateService.getBinary(keyRequest.key)
    }

    @PostMapping("new-version")
    fun getNewestVersion(@RequestBody keyRequest: KeyRequest): String? =
        updateService.getNewestVersion(keyRequest.key)

}