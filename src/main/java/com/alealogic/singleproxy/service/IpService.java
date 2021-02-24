package com.alealogic.singleproxy.service;

import com.alealogic.singleproxy.model.TorContainer;
import com.alealogic.singleproxy.util.Hasher;
import com.alealogic.singleproxy.util.Utils;
import com.github.dockerjava.api.DockerClient;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;

@Service
public class IpService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IpService.class);

    private final OkHttpClient baseOkHttpClient = createBaseOkHttpClient();
    private final Request request = new Request.Builder()
            .url("http://icanhazip.com/")
            .build();

    public void setPublicIp(Collection<TorContainer> torContainers, DockerClient dockerClient) {
        CountDownLatch countDownLatch = new CountDownLatch(torContainers.size());
        torContainers.forEach(torContainer -> this.setPublicIpAsync(torContainer, countDownLatch, dockerClient));

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    private void setPublicIpAsync(TorContainer torContainer, CountDownLatch countDownLatch, DockerClient dockerClient) {
        OkHttpClient okHttpClient = getProxyClient(torContainer);
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                LOGGER.error(e.getMessage(), e);
                torContainer.shutDown(dockerClient);

                countDownLatch.countDown();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String ipAddressOfExitNode = response.body().string().strip();

                if (!Utils.validIP(ipAddressOfExitNode)){
                    LOGGER.warn("ip address of exit node not valid!");
                    torContainer.shutDown(dockerClient);
                }

                torContainer.setIpAddressOfExitNode(ipAddressOfExitNode);
                torContainer.setIpId(Hasher.getHash(ipAddressOfExitNode));

                LOGGER.info("ip address of exit node: " + ipAddressOfExitNode);

                countDownLatch.countDown();
            }
        });
    }

    public boolean setPublicIpAndCheckIfValid(TorContainer torContainer) {
        OkHttpClient okHttpClient = getProxyClient(torContainer);
        Call call = okHttpClient.newCall(request);

        try {
            Response response = call.execute();
            String ipAddressOfExitNode = response.body().string().strip();

            if (!Utils.validIP(ipAddressOfExitNode)){
                LOGGER.error("invalid ip: " + ipAddressOfExitNode);
                return false;
            }

            torContainer.setIpAddressOfExitNode(ipAddressOfExitNode);
            torContainer.setIpId(Hasher.getHash(ipAddressOfExitNode));
            return true;
        } catch (IOException ioException) {
            LOGGER.error(ioException.getMessage(), ioException);
            return false;
        }
    }

    private OkHttpClient getProxyClient(TorContainer torContainer) {
        Proxy proxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress("localhost", torContainer.getHttpPort()));
        return baseOkHttpClient.newBuilder().proxy(proxy).build();
    }


    private OkHttpClient createBaseOkHttpClient() {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(50);
        dispatcher.setMaxRequestsPerHost(50);

        int timeout = 30;

        return new OkHttpClient.Builder()
                .callTimeout(Duration.ofSeconds(timeout))
                .connectTimeout(Duration.ofSeconds(timeout))
                .readTimeout(Duration.ofSeconds(timeout))
                .writeTimeout(Duration.ofSeconds(timeout))
                .dispatcher(dispatcher)
                .build();
    }
}
