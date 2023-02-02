package com.iohao.game.bolt.broker.cluster;

import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 渔民小镇
 * @date 2022-05-16
 */
public class Gossip1Test {

    @Test
    public void a() throws InterruptedException {

        // 种子节点
        List<String> seedAddress = List.of(
                "127.0.0.1:30056",
                "127.0.0.1:30057"
        );

        int gossipListenPort = 30056;
        int port = IoGameGlobalConfig.brokerPort;

        BrokerClusterManager brokerClusterManager = new BrokerClusterManager();
        brokerClusterManager.setGossipListenPort(gossipListenPort);
        brokerClusterManager.setPort(port);
        brokerClusterManager.setSeedAddress(seedAddress);

        brokerClusterManager.start();

        TimeUnit.MINUTES.sleep(22);
    }
}