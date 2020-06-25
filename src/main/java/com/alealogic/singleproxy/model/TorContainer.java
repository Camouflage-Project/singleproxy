package com.alealogic.singleproxy.model;

import com.github.dockerjava.api.DockerClient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class TorContainer {

    private String containerId;
    private int torPort;
    private int controlPort;
    private int httpPort;
    private boolean authenticated;
    private Socket controlSocket;
    private BufferedReader socketReader;
    private DataOutputStream socketWriter;

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

    public void shutdown() {

    }

    public Socket getControlSocket() {
        return controlSocket;
    }

    public void setControlSocket(Socket controlSocket) {
        this.controlSocket = controlSocket;
    }

    public void shutDown(DockerClient dockerClient) {
        try {
            controlSocket.close();
            socketReader.close();
            socketWriter.close();
            dockerClient.stopContainerCmd(containerId).exec();
            dockerClient.removeContainerCmd(containerId).exec();
        } catch (IOException ioException) {
            ioException.printStackTrace();
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
                Objects.equals(controlSocket, that.controlSocket) &&
                Objects.equals(socketReader, that.socketReader) &&
                Objects.equals(socketWriter, that.socketWriter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(containerId, torPort, controlPort, httpPort, authenticated, controlSocket, socketReader, socketWriter);
    }
}
