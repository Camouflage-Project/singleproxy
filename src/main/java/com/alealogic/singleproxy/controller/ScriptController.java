package com.alealogic.singleproxy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScriptController {

    @GetMapping("script")
    public String getScript() {
        return "ls\npwd";
    }
}
