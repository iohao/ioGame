/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iohao.game.bolt.broker.cluster;

import com.iohao.game.bolt.broker.core.common.BrokerGlobalConfig;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.cluster.ClusterMessageListenerImpl;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

/**
 * bolt broker server 集群的管理 构建器
 * <pre>
 *     构建器创建
 *     {@link BrokerCluster#newBrokerClusterManagerBuilder()}
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-15
 */
@Slf4j
@Setter
@Accessors(fluent = true)
public class BrokerClusterManagerBuilder {

    /**
     * 种子节点地址
     * <pre>
     *     格式： ip:port
     *
     *     -- 生产环境的建议 --
     *     注意，在生产上建议一台物理机配置一个 broker （游戏网关）
     *     一个 broker 就是一个节点
     *     比如配置三台机器，端口可以使用同样的端口，假设三台机器的 ip 分别是:
     *     192.168.1.10:30056
     *     192.168.1.11:30056
     *     192.168.1.12:30056
     *
     *
     *     -- 注意这里如果没设置，构建器会给一个默认值用于测试 --
     *     这里配置写死是方便在一台机器上启动集群
     *     但是同一台机器启动多个 broker 来实现集群就要使用不同的端口，因为《端口被占用，不能相同》
     *     所以这里的配置是：
     *     127.0.0.1:30056
     *     127.0.0.1:30057
     * </pre>
     */
    List<String> seedAddress;

    /** Gossip listen port 监听端口 */
    int gossipListenPort = BrokerGlobalConfig.gossipListenPort;

    BrokerClusterManagerBuilder() {
    }

    public BrokerClusterManager build(BrokerServer brokerServer) {

        this.checked();
        // 种子节点
        this.extractedSeedAddress();

        // broker 端口（游戏网关端口）
        int port = brokerServer.getPort();

        BrokerClusterManager brokerClusterManager = new BrokerClusterManager();

        ClusterMessageListenerImpl clusterMessageListener = new ClusterMessageListenerImpl();
        clusterMessageListener.setBrokerServer(brokerServer);

        brokerClusterManager
                .setBrokerId(brokerServer.getBrokerId())
                .setClusterMessageListener(clusterMessageListener)
                // 种子节点地址
                .setSeedAddress(seedAddress)
                // Gossip listen port 监听端口
                .setGossipListenPort(gossipListenPort)
                // broker 端口（游戏网关端口）
                .setPort(port);

        try {
            // 启动集群
            brokerClusterManager.start();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return brokerClusterManager;
    }

    private void checked() {
        if (this.gossipListenPort <= 0) {
            throw new RuntimeException("gossipListenPort error!");
        }
    }

    private void extractedSeedAddress() {

        if (Objects.isNull(this.seedAddress) || this.seedAddress.isEmpty()) {
            this.seedAddress = List.of(
                    "127.0.0.1:30056",
                    "127.0.0.1:30057"
            );

            log.warn("因为你没有设置 种子节点信息，这里为你添加一些默认设置的种子节点");
        }

        log.info("当前种子节点信息: {}", this.seedAddress);
    }

}
