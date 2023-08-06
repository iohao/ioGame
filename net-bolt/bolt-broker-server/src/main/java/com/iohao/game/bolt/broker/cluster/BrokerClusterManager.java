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

import com.iohao.game.bolt.broker.core.message.BrokerClusterMessage;
import com.iohao.game.common.consts.IoGameLogName;
import com.iohao.game.common.kit.NetworkKit;
import io.scalecube.cluster.Cluster;
import io.scalecube.cluster.ClusterImpl;
import io.scalecube.net.Address;
import io.scalecube.transport.netty.tcp.TcpTransportFactory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * Bolt Broker Manager 集群
 * <pre>
 *     gossip
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-14
 */
@Getter
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j(topic = IoGameLogName.ClusterTopic)
public final class BrokerClusterManager {
    /** broker （游戏网关）唯一 id */
    String brokerId;
    /** broker 端口（游戏网关端口） */
    int port;
    /** Gossip listen port 监听端口 */
    int gossipListenPort;

    /**
     * 种子节点地址
     * <pre>
     *     格式： ip:port
     * </pre>
     */
    List<String> seedAddress;

    Cluster cluster;

    ClusterMessageListener clusterMessageListener;

    String name;

    BrokerClusterMessageHandler messageHandler;

    BrokerClusterManager() {
    }

    public void start() {
        final String localIp = NetworkKit.LOCAL_IP;
        Broker localBroker = getLocalBroker(localIp);

        this.name = String.format("ioGameCluster-%d-%d-%s", port, gossipListenPort, localIp);
        this.messageHandler = new BrokerClusterMessageHandler(name, localBroker, clusterMessageListener);

        // 种子节点地址
        List<Address> seedMemberAddress = this.listSeedMemberAddress();

        this.cluster = new ClusterImpl()
                .config(options -> options
                        .memberAlias(name)
                        .metadata(new BrokerClusterMetadata(name, localBroker))
                        .externalHost(localIp)
                        // externalPort是一个容器环境的配置属性，它被设置为向 scalecube 集群发布一个映射到 scalecube 传输侦听端口。
                        .externalPort(gossipListenPort)
                )
                // 种子成员地址
                .membership(membershipConfig -> membershipConfig
                        // 种子节点地址
                        .seedMembers(seedMemberAddress)
                        // 时间间隔
                        .syncInterval(5_000)
                )
                .handler(messageHandler)
                .transportFactory(TcpTransportFactory::new)
                .transport(transportConfig -> transportConfig.port(gossipListenPort))
                .startAwait();

        Map<String, Broker> brokers = this.messageHandler.brokers;
        brokers.put(localBroker.getClusterAddress(), localBroker);
    }

    public BrokerClusterMessage getBrokerClusterMessage() {
        return this.messageHandler.getBrokerClusterMessage();
    }

    private Broker getLocalBroker(String localIp) {
        String clusterAddress = localIp + ":" + this.gossipListenPort;
        String brokerAddress = localIp + ":" + this.port;
        return new Broker(localIp)
                .setId(this.brokerId)
                .setPort(this.port)
                .setBrokerAddress(brokerAddress)
                .setClusterAddress(clusterAddress);
    }

    private List<Address> listSeedMemberAddress() {
        return this.seedAddress
                .stream()
                .map(Address::from)
                .toList();
    }
}
