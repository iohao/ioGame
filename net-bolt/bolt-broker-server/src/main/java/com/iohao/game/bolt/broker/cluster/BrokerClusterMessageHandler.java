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
import com.iohao.game.bolt.broker.core.message.BrokerMessage;
import com.iohao.game.common.consts.IoGameLogName;
import io.scalecube.cluster.Cluster;
import io.scalecube.cluster.ClusterMessageHandler;
import io.scalecube.cluster.Member;
import io.scalecube.cluster.membership.MembershipEvent;
import io.scalecube.cluster.transport.api.Message;
import io.scalecube.net.Address;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;
import reactor.core.publisher.Sinks;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ClusterMessageHandler impl
 *
 * @author 渔民小镇
 * @date 2023-05-27
 */
@Slf4j(topic = IoGameLogName.ClusterTopic)
final class BrokerClusterMessageHandler implements ClusterMessageHandler, Function<Cluster, ClusterMessageHandler> {
    final String name;
    final Broker localBroker;
    final ClusterMessageListener clusterMessageListener;

    /**
     * broker map
     * <pre>
     *     key : address
     *     value : broker
     * </pre>
     */
    Map<String, Broker> brokers = new NonBlockingHashMap<>();
    Cluster cluster;
    /** brokers changes emitter processor */
    Sinks.Many<Collection<Broker>> brokersEmitterProcessor = Sinks.many().multicast().onBackpressureBuffer();

    public BrokerClusterMessageHandler(String name, Broker localBroker, ClusterMessageListener clusterMessageListener) {
        this.name = name;
        this.localBroker = localBroker;
        this.clusterMessageListener = clusterMessageListener;
    }

    @Override
    public ClusterMessageHandler apply(Cluster cluster) {
        this.cluster = cluster;
        return this;
    }

    @Override
    public void onMessage(Message message) {
        log.info("\n{}", name + " received: " + message.data());
    }

    @Override
    public void onGossip(Message gossip) {
        log.info("\n{}", name + " received: " + gossip.data());
    }

    @Override
    public void onMembershipEvent(MembershipEvent event) {
//        print(event);

        Map<String, Broker> brokers = new NonBlockingHashMap<>();

        this.cluster.members().forEach(member -> {
            Optional<BrokerClusterMetadata> optional = cluster.metadata(member);
            optional.ifPresent(metadata -> {
                Broker memberBroker = metadata.getLocalBroker();

                Address address = member.address();

                Broker theBroker = new Broker(address.host())
                        .setClusterAddress(address.toString())
                        .setId(memberBroker.getId())
                        .setPort(memberBroker.getPort())
                        .setBrokerAddress(memberBroker.getBrokerAddress());

                String clusterAddress = theBroker.getClusterAddress();
                brokers.put(clusterAddress, theBroker);
            });
        });

        // 使用新的 brokers。无论是 ADDED、REMOVED 都重新生成一次。
        this.brokers = brokers;

        this.brokersEmitterProcessor.tryEmitNext(this.brokers.values());

        this.inform();
    }

    BrokerClusterMessage getBrokerClusterMessage() {
        // 得到 Broker（游戏网关）列表
        var brokerMessageList = this.brokers.values().stream().map(broker -> {
            BrokerMessage item = new BrokerMessage();
            item.setAddress(broker.getBrokerAddress());
            item.setId(broker.getId());
            return item;
        }).collect(Collectors.toList());

        // 集群消息
        BrokerClusterMessage brokerClusterMessage = new BrokerClusterMessage();
        brokerClusterMessage.setBrokerMessageList(brokerMessageList);

        return brokerClusterMessage;
    }

    /**
     * 发送集群信息给客户端（这里指的是逻辑服：对外服和游戏逻辑服）
     */
    private void inform() {
        if (Objects.isNull(this.clusterMessageListener)) {
            return;
        }

        BrokerClusterMessage brokerClusterMessage = getBrokerClusterMessage();
        // 将集群信息发送给游戏对外服、游戏逻辑服
        this.clusterMessageListener.inform(brokerClusterMessage);
    }

    private void print(MembershipEvent event) {

        int size = cluster.members().size();
        String mStr = cluster.members().stream()
                .map(Member::toString)
                .collect(Collectors.joining("\n"));

        log.info("\n{} {}", name + " received: " + event.member().alias(), event);
        log.info("size : {} {} \n{}", size, event.type(), mStr);
    }
}
