package com.alealogic.singleproxy.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
public class DownloadController {

    @GetMapping(
            value = "alealogic-release",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] getBinary(@Value("${binary.path}") String binaryPath) throws IOException {
        return Files.readAllBytes(Paths.get(binaryPath));
    }
}
