package com.alealogic.singleproxy.service;

import com.alealogic.singleproxy.model.TorContainer;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;
import java.util.stream.IntStream;

@Service
public class TorManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(TorManager.class);

    private int portToAssign = 9050;
    private final DockerClient dockerClient = DockerClientBuilder.getInstance().build();
    private final Set<TorContainer> torContainers = new HashSet<>();
    private Iterator<Integer> httpPortIterator;

    public int getNextTorPort() {
        if (httpPortIterator == null || !httpPortIterator.hasNext())
            httpPortIterator = torContainers.stream().map(TorContainer::getHttpPort).iterator();
        return httpPortIterator.next();
    }

    public Set<TorContainer> createTorContainers(int amount) {
        IntStream.range(0, amount).forEach(i -> {
            TorContainer torContainer = startTorContainer(getThreeAvailablePorts().toArray(new Integer[0]));
            torContainers.add(torContainer);
        });

        return torContainers;
    }

    public void removeAllContainers() {
        List<Container> containers = dockerClient.listContainersCmd().exec();
        containers.forEach(container -> dockerClient.stopContainerCmd(container.getId()).exec());
        containers.forEach(container -> dockerClient.removeContainerCmd(container.getId()).exec());
    }

    public void changeIdentity(TorContainer torContainer) {
        try {
            DataOutputStream socketWriter = torContainer.getSocketWriter();
            socketWriter.write(("SIGNAL NEWNYM" + "\r\n").getBytes());
            socketWriter.flush();

            String response = torContainer.getSocketReader().readLine();
            System.out.println(response);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private TorContainer startTorContainer(Integer... ports) {
        int torPort = ports[0];
        int controlPort = ports[1];
        int httpPort = ports[2];

        CreateContainerResponse container = dockerClient.createContainerCmd("test:1.0")
                .withHostConfig(new HostConfig().withNetworkMode("host"))
                .withCmd(String.valueOf(torPort), String.valueOf(controlPort), String.valueOf(httpPort))
                .exec();

        dockerClient.startContainerCmd(container.getId()).exec();

        TorContainer torContainer = new TorContainer(container.getId(), torPort, controlPort, httpPort);
        return authenticateTor(torContainer);
    }

    private TorContainer authenticateTor(TorContainer torContainer) {
        Socket controlSocket;
        DataOutputStream socketWriter;
        BufferedReader socketReader;
        boolean authenticated;

        while (true) {
            try {
                controlSocket = new Socket();
                controlSocket.connect(new InetSocketAddress("127.0.0.1", torContainer.getControlPort()));
                socketWriter = new DataOutputStream(controlSocket.getOutputStream());
                socketReader = new BufferedReader(new InputStreamReader(controlSocket.getInputStream()));

                String password = "password";
                socketWriter.write(("AUTHENTICATE \"" + password + "\"" + "\r\n").getBytes());
                socketWriter.flush();
                String response = socketReader.readLine();
                System.out.println(response);
                authenticated = response != null && response.equals("250 OK");
                break;
            } catch (IOException ioException) {
                LOGGER.info(ioException.getMessage());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOGGER.info(e.getMessage());
                }
            }
        }

        torContainer.setAuthenticated(authenticated);
        torContainer.setControlSocket(controlSocket);
        torContainer.setSocketReader(socketReader);
        torContainer.setSocketWriter(socketWriter);
        return torContainer;
    }

    private boolean portIsAvailable(int port) {
        Socket s = null;
        try {
            s = new Socket("localhost", port);
            return false;
        } catch (IOException e) {
            return true;
        } finally {
            if(s != null){
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private List<Integer> getThreeAvailablePorts() {
        List<Integer> availablePorts = new ArrayList<>();
        while (availablePorts.size() < 3) {
            int port = portToAssign++;
            if (portIsAvailable(port)) availablePorts.add(port);
        }
        return availablePorts;
    }
}
