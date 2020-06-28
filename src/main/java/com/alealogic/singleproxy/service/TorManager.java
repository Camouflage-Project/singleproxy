package com.alealogic.singleproxy.service;

import com.alealogic.singleproxy.entity.BlacklistedIp;
import com.alealogic.singleproxy.entity.Customer;
import com.alealogic.singleproxy.model.PortDto;
import com.alealogic.singleproxy.model.TorContainer;
import com.alealogic.singleproxy.repository.BlacklistedIpRepository;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TorManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(TorManager.class);

    @Value("${number.tor.nodes}")
    private Integer numberOfTorNodes;
    private final IpService ipService;
    private final BlacklistedIpRepository blacklistedIpRepository;
    private int portToAssign = 9050;
    private final DockerClient dockerClient = DockerClientBuilder.getInstance().build();
    private final Map<Customer, Queue<TorContainer>> customerToNodes = new HashMap<>();
    private final Map<String, TorContainer> ipIdToTorContainer = new HashMap<>();

    public TorManager(IpService ipService, BlacklistedIpRepository blacklistedIpRepository) {
        this.ipService = ipService;
        this.blacklistedIpRepository = blacklistedIpRepository;
    }

    public PortDto getNextTorPortForCustomer(Customer customer) {
        Queue<TorContainer> nodes;
        if (customerToNodes.containsKey(customer)) nodes = customerToNodes.get(customer);
        else nodes = getNodesForCustomer(customer);

        TorContainer nextNode = nodes.remove();
        nodes.add(nextNode);

        return new PortDto(nextNode.getHttpPort(), nextNode.getIpId());
    }

    public void createTorContainers() {
        List<TorContainer> torContainers = new ArrayList<>();

        IntStream.range(0, numberOfTorNodes).forEach(i -> {
            TorContainer torContainer = startTorContainer(getThreeAvailablePorts().toArray(new Integer[0]));

            LOGGER.info("listening on port: " + torContainer.getHttpPort());

            torContainers.add(torContainer);
        });

        torContainers.forEach(this::authenticateTor);
        ipService.setPublicIp(torContainers);

        torContainers.forEach(torContainer -> {
            while (ipIdToTorContainer.containsKey(torContainer.getIpId())) {
                LOGGER.info("Another container already has this ip: " + torContainer.getIpAddressOfExitNode());
                changeIdentity(torContainer);
                ipService.setPublicIp(torContainer);
                LOGGER.info("New ip is: " + torContainer.getIpAddressOfExitNode());
            }

            ipIdToTorContainer.put(torContainer.getIpId(), torContainer);
        });

        LOGGER.info("created " + ipIdToTorContainer.size() + " tor containers");
    }

    public void stopAndRemoveAllTorContainers() {
        ipIdToTorContainer.values().forEach(container -> container.shutDown(dockerClient));
    }

    public void changeIdentity(TorContainer torContainer) {
        try {
            DataOutputStream socketWriter = torContainer.getSocketWriter();
            socketWriter.write(("SIGNAL NEWNYM" + "\r\n").getBytes());
            socketWriter.flush();

            String response = torContainer.getSocketReader().readLine();
            LOGGER.info("changing identity... response: " + response);
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

        return new TorContainer(container.getId(), torPort, controlPort, httpPort);
    }

    private void authenticateTor(TorContainer torContainer) {
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
                LOGGER.info("tor authentication response: " + response);
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

    private Queue<TorContainer> getNodesForCustomer(Customer customer) {
        Set<String> allByCustomerId = blacklistedIpRepository.findAllByCustomerId(customer.getId()).stream().map(BlacklistedIp::getIpId).collect(Collectors.toSet());

        List<TorContainer> containers = new ArrayList<>(ipIdToTorContainer.values());
        containers.removeIf(container -> allByCustomerId.contains(container.getIpId()));
        Collections.shuffle(containers);

        LinkedBlockingQueue<TorContainer> nodes = new LinkedBlockingQueue<>(containers.subList(0, customer.getEnabledProxies()));
        customerToNodes.put(customer, nodes);
        return nodes;
    }

    public void blacklistIp(Customer customer, String ipId) {
        Queue<TorContainer> nodes = customerToNodes.get(customer);
        nodes.removeIf(node -> node.getIpId().equals(ipId));
    }
}
