package com.alealogic.singleproxy.model;

import com.github.dockerjava.api.DockerClient;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;

@Data
public class TorContainer {

    private static final Logger LOGGER = LoggerFactory.getLogger(TorContainer.class);

    private String containerId;
    private String ipAddressOfExitNode;
    private String ipId;
    private int torPort;
    private int controlPort;
    private int httpPort;
    private boolean authenticated;
    private Socket controlSocket;
    private BufferedReader socketReader;
    private DataOutputStream socketWriter;
    private boolean running;

    public TorContainer(String containerId, int torPort, int controlPort, int httpPort) {
        this.containerId = containerId;
        this.torPort = torPort;
        this.controlPort = controlPort;
        this.httpPort = httpPort;
    }

    public void shutDown(DockerClient dockerClient) {
        if (running) {
            try {
                controlSocket.close();
                socketReader.close();
                socketWriter.close();
                dockerClient.stopContainerCmd(containerId).exec();
                dockerClient.removeContainerCmd(containerId).exec();
                running = false;
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        } else {
            LOGGER.warn("Trying to shut down a container that isn't running!");
        }
    }
}
