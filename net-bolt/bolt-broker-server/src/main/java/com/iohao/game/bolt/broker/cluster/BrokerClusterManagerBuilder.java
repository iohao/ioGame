/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.iohao.game.bolt.broker.cluster;

import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.cluster.ClusterMessageListenerImpl;
import com.iohao.game.common.consts.IoGameLogName;
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

@Setter
@Accessors(fluent = true)
@Slf4j(topic = IoGameLogName.ClusterTopic)
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
    int gossipListenPort = IoGameGlobalConfig.gossipListenPort;

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

        if (IoGameGlobalConfig.isBrokerClusterLog()) {
            log.info("当前种子节点信息: {}", this.seedAddress);
        }
    }

}
