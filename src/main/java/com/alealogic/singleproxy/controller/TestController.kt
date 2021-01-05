package com.alealogic.singleproxy.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    private val logger: Logger = LoggerFactory.getLogger(TestController::class.java)

    @GetMapping("greeting")
    fun greeting(): String {
        logger.info("called greeting")
        return "hey"
    }



}