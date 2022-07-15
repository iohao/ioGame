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

import com.alibaba.fastjson2.JSON;
import com.iohao.game.bolt.broker.core.common.BrokerGlobalConfig;
import com.iohao.game.bolt.broker.core.message.BrokerClusterMessage;
import com.iohao.game.bolt.broker.core.message.BrokerMessage;
import com.iohao.game.common.kit.ExecutorKit;
import com.iohao.game.common.kit.NetworkKit;
import io.scalecube.cluster.Cluster;
import io.scalecube.cluster.ClusterImpl;
import io.scalecube.cluster.ClusterMessageHandler;
import io.scalecube.cluster.Member;
import io.scalecube.cluster.membership.MembershipEvent;
import io.scalecube.cluster.transport.api.Message;
import io.scalecube.net.Address;
import io.scalecube.transport.netty.tcp.TcpTransportFactory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

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
@Slf4j
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrokerClusterManager implements ClusterMessageHandler {
    /** 集群名 每个节点的名字必须一样 */
    final String clusterName = "io_game_cluster";
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

    Broker localBroker;
    Mono<Cluster> clusterMono;

    ClusterMessageListener clusterMessageListener;

    /**
     * broker map
     * <pre>
     *     key : address
     *     value : broker
     * </pre>
     */
    Map<String, Broker> brokers = new NonBlockingHashMap<>();

    AtomicBoolean openScheduledLog = new AtomicBoolean(false);

    /** brokers changes emitter processor */
    private Sinks.Many<Collection<Broker>> brokersEmitterProcessor = Sinks.many().multicast().onBackpressureBuffer();

    BrokerClusterManager() {
    }

    public void start() throws Exception {

        final String localIp = NetworkKit.LOCAL_IP;

        // 种子节点地址
        List<Address> seedMemberAddress = this.listSeedMemberAddress();

        clusterMono = new ClusterImpl()
                .config(clusterConfig -> clusterConfig
                        .memberAlias("Gateway Broker")
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
                .transportFactory(TcpTransportFactory::new)
                .transport(transportConfig -> transportConfig.port(gossipListenPort))
                .handler(cluster -> this)
                .start()
        ;

        this.clusterMono.subscribe();

        String clusterAddress = localIp + ":" + this.gossipListenPort;
        String brokerAddress = localIp + ":" + this.port;

        this.localBroker = new Broker(localIp)
                .setId(this.brokerId)
                .setPort(this.port)
                .setBrokerAddress(brokerAddress)
                .setClusterAddress(clusterAddress)
        ;

        this.brokers.put(clusterAddress, this.localBroker);

        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        log.info("hostAddress : {}", hostAddress);
        // 种子节点（seed node）：作为其他节点加入集群的连接点的节点。实际上，一个节点可以通过向集群中的任何一个节点发送Join（加入）命令加入集群。
        // SeedNode是种子节点，用于新节点的加入和节点之间同步数据，种子节点是任意约定的，可以是A,B,C中的一个或者几个；

    }

    void send() {
        ScheduledExecutorService executorService = ExecutorKit.newSingleScheduled(BrokerClusterManager.class.toString());

        executorService.scheduleAtFixedRate(() -> {
            Message message = Message.fromData("Greetings from Carol~~~~~");
            if (BrokerGlobalConfig.openLog) {
                log.info("message : {}", message);
            }

            clusterMono.subscribe(cluster -> {
                Collection<Member> members = cluster.otherMembers();
                Flux.fromIterable(members)
                        .flatMap(member -> cluster.send(member, message))
                        .subscribe(null, Throwable::printStackTrace);
            });
        }, 1, 8, TimeUnit.SECONDS);
    }

    /**
     * 发送集群信息给客户端（这里指的是逻辑服：对外服和游戏逻辑服）
     */
    public void inform() {
        if (Objects.isNull(this.clusterMessageListener)) {
            return;
        }

        if (!openScheduledLog.get()) {
            openScheduledLog.set(true);
            ExecutorKit.newSingleScheduled(BrokerClusterManager.class.getName()).scheduleAtFixedRate(() -> {
                BrokerClusterMessage brokerClusterMessage = getBrokerClusterMessage();
                int port = localBroker.getPort();
                log.info("broker（游戏网关）: [{}] --  集群数量[{}] - 详细：[{}]"
                        , port
                        , brokerClusterMessage.count()
                        , brokerClusterMessage);
            }, 1, 10, TimeUnit.SECONDS);
        }

        BrokerClusterMessage brokerClusterMessage = getBrokerClusterMessage();

        clusterMessageListener.inform(brokerClusterMessage);
    }

    public BrokerClusterMessage getBrokerClusterMessage() {
        // 得到网关列表
        var brokerMessageList = brokers.values().stream().map(broker -> {
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

    @Override
    public void onMembershipEvent(MembershipEvent event) {
        log.info("event : {}", event);
        // 事件广播
        // xx.send(member, msg); 会触发到这
        Broker broker = memberToBroker(event.member());
        String address = broker.getClusterAddress();

        //  有机器加入，就通知客户端与broker 建立连接
        if (event.isAdded()) {
            this.makeCall(event.member()).subscribe(response -> {
                // 收到消息回复后, 这个对象是新加入的 broker 节点。see: this.makeCall method
                Broker responseBroker = JSON.parseObject(response, Broker.class);

                broker
                        .setId(responseBroker.getId())
                        .setPort(responseBroker.getPort())
                        .setBrokerAddress(responseBroker.getBrokerAddress())
                ;


                this.brokers.put(address, broker);

                this.inform();
            });
        } else if (event.isRemoved()) {
            this.brokers.remove(address);
            log.info("isRemoved onMembershipEvent: {}", address);
            this.inform();
        } else if (event.isLeaving()) {
            this.brokers.remove(address);
            log.info("isLeaving onMembershipEvent: {}", address);
            this.inform();
        }

        brokersEmitterProcessor.tryEmitNext(brokers.values());
    }

    @Override
    public void onGossip(Message gossip) {
        log.info("Message gossip : {}", gossip);
    }

    @Override
    public void onMessage(Message message) {
        // see : this.makeCall
        if (message.header("added") != null) {

            String json = this.localBroker.toJson();
            log.debug("json : {}", json);

            // 消息回复
            Message replyMessage = Message.builder()
                    .correlationId(message.correlationId())
                    .data(json)
                    .build();

            // 接收到消息，给发送方回传一个消息
            this.clusterMono
                    // 给消息请求者发送消息回传
                    .flatMap(cluster -> cluster.send(message.sender(), replyMessage))
                    .subscribe();
        }
    }

    private Mono<String> makeCall(Member member) {
        String uuid = UUID.randomUUID().toString();
        Message message = Message.builder()
                .correlationId(uuid)
                .header("added", "")
                .build();

        return this.clusterMono
                /*
                 * 发送消息到给定的地址。如果给定的传输地址没有传输通道存在，它将发出连接。
                 * Send是一个异步操作，并期望由调用方提供的correlationId和发送方地址来响应
                 * 请求，必须得到响应才会继续往下执行
                 * see : this.onMessage
                 */
                .flatMap(cluster -> cluster.requestResponse(member, message))
                .map(Message::data);

    }

    private Broker memberToBroker(Member member) {
        Address address = member.address();

        return new Broker(address.host())
                .setClusterAddress(address.toString());
    }

    private List<Address> listSeedMemberAddress() {
        return this.seedAddress
                .stream()
                .map(Address::from)
                .toList();
    }
}
