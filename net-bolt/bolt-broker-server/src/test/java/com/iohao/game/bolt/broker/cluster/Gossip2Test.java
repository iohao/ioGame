package com.iohao.game.bolt.broker.cluster;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 渔民小镇
 * @date 2022-05-16
 */
public class Gossip2Test {
    public static void main(String[] args) throws Exception {
        new Gossip2Test().a();
    }

    private void a() throws Exception {

        // 种子节点
        List<String> seedAddress = List.of(
                "127.0.0.1:30056",
                "127.0.0.1:30057"
        );

        int gossipListenPort = 30057;
        int port = 10201;

        BrokerClusterManager brokerClusterManager = new BrokerClusterManager();
        brokerClusterManager.setGossipListenPort(gossipListenPort);
        brokerClusterManager.setPort(port);
        brokerClusterManager.setSeedAddress(seedAddress);

        brokerClusterManager.start();

        brokerClusterManager.send();

        TimeUnit.MINUTES.sleep(22);
    }
}