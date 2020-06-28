package com.alealogic.singleproxy.service;

import com.alealogic.singleproxy.model.TorContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@SpringBootTest
@ActiveProfiles("test")
class TorManagerTest {

    @Autowired
    TorManager torManager;
    @Autowired
    IpService ipService;

    @Test
    public void test() {
        Map<String, Integer> map = new HashMap<>();
        map.put("something", 1);

        System.out.println(map);
    }

    @Test
    public void howManyIps() {
        ReflectionTestUtils.setField(torManager, "numberOfTorNodes", 100);

        List<TorContainer> torContainers = torManager.createTorContainers();
        Map<String, Integer> ipToFrequency = new HashMap<>();

        torContainers.forEach(container -> {
            String ipAddressOfExitNode = container.getIpAddressOfExitNode();
            System.out.println(ipAddressOfExitNode);
            if (ipToFrequency.containsKey(ipAddressOfExitNode))
                ipToFrequency.put(ipAddressOfExitNode, ipToFrequency.get(ipAddressOfExitNode) + 1);
            else
                ipToFrequency.put(ipAddressOfExitNode, 1);
        });

        System.out.println(ipToFrequency);
    }
}