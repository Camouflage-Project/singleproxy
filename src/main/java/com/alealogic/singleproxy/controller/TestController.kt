package com.alealogic.singleproxy.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    @GetMapping("greeting")
    fun greeting(): String = "ls"
}