package com.alealogic.singleproxy.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PortService {

    private int portToAssign = 10060;

    public List<Integer> getThreeAvailablePorts() {
        List<Integer> availablePorts = new ArrayList<>();
        while (availablePorts.size() < 3) {
            int port = portToAssign++;
            if (available(port)) availablePorts.add(port);
        }
        return availablePorts;
    }

    public Integer getNextAvailablePort() {
        while (true) {
            int port = portToAssign++;
            if (available(port))
                return port;
        }
    }

    private boolean available(int port) {
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
                    log.error(e.getMessage(), e);
                }
            }
        }
    }
}

