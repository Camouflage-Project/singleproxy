package com.alealogic.singleproxy.controller;

import com.alealogic.singleproxy.model.BinaryRequest;
import com.alealogic.singleproxy.model.KeyRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
public class UpdateController {

    @PostMapping(
            value = "binary",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] getBinary(@Value("${binary.path}") String binaryPath,
                            @RequestBody BinaryRequest binaryRequest) throws IOException {
        return Files.readAllBytes(Paths.get(binaryPath));
    }

    @PostMapping("new-version")
    public String getNewestVersion(@RequestBody KeyRequest keyRequest) {
        return "SingleProxyDesktopClient_1_1_2";
    }
}
