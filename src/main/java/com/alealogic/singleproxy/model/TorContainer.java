package com.alealogic.singleproxy.model;

import com.github.dockerjava.api.DockerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.Set;

public class TorContainer {

    private static final Logger LOGGER = LoggerFactory.getLogger(TorContainer.class);

    private String containerId;
    private String ipAddressOfExitNode;
    private String hash;
    private int torPort;
    private int controlPort;
    private int httpPort;
    private boolean authenticated;
    private Socket controlSocket;
    private BufferedReader socketReader;
    private DataOutputStream socketWriter;
    private Set<User> blacklistedUsers;

    public TorContainer(String containerId, int torPort, int controlPort, int httpPort) {
        this.containerId = containerId;
        this.torPort = torPort;
        this.controlPort = controlPort;
        this.httpPort = httpPort;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public int getTorPort() {
        return torPort;
    }

    public void setTorPort(int torPort) {
        this.torPort = torPort;
    }

    public int getControlPort() {
        return controlPort;
    }

    public void setControlPort(int controlPort) {
        this.controlPort = controlPort;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public BufferedReader getSocketReader() {
        return socketReader;
    }

    public void setSocketReader(BufferedReader socketReader) {
        this.socketReader = socketReader;
    }

    public DataOutputStream getSocketWriter() {
        return socketWriter;
    }

    public void setSocketWriter(DataOutputStream socketWriter) {
        this.socketWriter = socketWriter;
    }

    public Socket getControlSocket() {
        return controlSocket;
    }

    public void setControlSocket(Socket controlSocket) {
        this.controlSocket = controlSocket;
    }

    public Set<User> getBlacklistedUsers() {
        return blacklistedUsers;
    }

    public void setBlacklistedUsers(Set<User> blacklistedUsers) {
        this.blacklistedUsers = blacklistedUsers;
    }

    public String getIpAddressOfExitNode() {
        return ipAddressOfExitNode;
    }

    public void setIpAddressOfExitNode(String ipAddressOfExitNode) {
        this.ipAddressOfExitNode = ipAddressOfExitNode;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void shutDown(DockerClient dockerClient) {
        try {
            controlSocket.close();
            socketReader.close();
            socketWriter.close();
            dockerClient.stopContainerCmd(containerId).exec();
            dockerClient.removeContainerCmd(containerId).exec();
        } catch (IOException ioException) {
            LOGGER.error(ioException.getMessage(), ioException);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TorContainer that = (TorContainer) o;
        return torPort == that.torPort &&
                controlPort == that.controlPort &&
                httpPort == that.httpPort &&
                authenticated == that.authenticated &&
                Objects.equals(containerId, that.containerId) &&
                Objects.equals(ipAddressOfExitNode, that.ipAddressOfExitNode) &&
                Objects.equals(hash, that.hash) &&
                Objects.equals(controlSocket, that.controlSocket) &&
                Objects.equals(socketReader, that.socketReader) &&
                Objects.equals(socketWriter, that.socketWriter) &&
                Objects.equals(blacklistedUsers, that.blacklistedUsers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(containerId, ipAddressOfExitNode, hash, torPort, controlPort, httpPort, authenticated, controlSocket, socketReader, socketWriter, blacklistedUsers);
    }
}
