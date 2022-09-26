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
package com.iohao.game.bolt.broker.server;

import com.alipay.remoting.ConnectionEventProcessor;
import com.alipay.remoting.ConnectionEventType;
import com.alipay.remoting.config.Configs;
import com.alipay.remoting.rpc.RpcServer;
import com.alipay.remoting.rpc.protocol.UserProcessor;
import com.iohao.game.bolt.broker.cluster.BrokerClusterManager;
import com.iohao.game.bolt.broker.cluster.BrokerClusterManagerBuilder;
import com.iohao.game.bolt.broker.cluster.BrokerRunModeEnum;
import com.iohao.game.bolt.broker.core.common.BrokerGlobalConfig;
import com.iohao.game.bolt.broker.server.aware.BrokerServerAware;
import com.iohao.game.bolt.broker.server.balanced.BalancedManager;
import com.iohao.game.bolt.broker.server.balanced.LogicBrokerClientLoadBalanced;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientRegionFactory;
import com.iohao.game.bolt.broker.server.balanced.region.DefaultBrokerClientRegion;
import com.iohao.game.bolt.broker.server.processor.*;
import com.iohao.game.bolt.broker.server.processor.connection.CloseConnectionEventBrokerProcessor;
import com.iohao.game.bolt.broker.server.processor.connection.ConnectionEventBrokerProcessor;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;

import java.util.*;
import java.util.function.Supplier;

/**
 * Broker Server （游戏网关服） 构建器
 * <pre>
 *     see {@link BrokerServer#newBuilder()}
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-15
 */
@Slf4j
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrokerServerBuilder {
    /** broker （游戏网关） */
    final BrokerServer brokerServer = new BrokerServer();
    /** 用户处理器 */
    final List<Supplier<UserProcessor<?>>> processorList = new ArrayList<>();

    /** bolt 连接器 */
    final Map<ConnectionEventType, Supplier<ConnectionEventProcessor>> connectionEventProcessorMap = new NonBlockingHashMap<>();

    /**
     * brokerId （游戏网关的id），服务器唯一标识
     * <pre>
     *     如果没设置，会随机分配一个
     *
     *     逻辑服的模块id，标记不同的逻辑服模块。
     *     开发者随意定义，只要确保每个逻辑服的模块 id 不相同就可以
     * </pre>
     */
    @Setter
    String brokerId;
    /** broker 端口（游戏网关端口） */
    @Setter
    int port = BrokerGlobalConfig.brokerPort;
    /** broker （游戏网关）的启动模式，默认单机模式 */
    @Setter
    BrokerRunModeEnum brokerRunMode = BrokerRunModeEnum.STANDALONE;
    /** 集群的管理 构建器，如果不需要集群，可以不设置 */
    BrokerClusterManagerBuilder brokerClusterManagerBuilder;

    /** BrokerClientRegion 工厂 */
    @Setter
    BrokerClientRegionFactory brokerClientRegionFactory = DefaultBrokerClientRegion::new;

    BrokerServerBuilder() {
        // 初始化一些处理器，如果开发者觉得默认的这些处理器没用，可以选择清除后，在添加自定义的。 this.clearProcessor
        this.defaultProcessor();

        // 开启 bolt 重连, 通过系统属性来开和关，如果一个进程有多个 RpcClient，则同时生效
        System.setProperty(Configs.CONN_MONITOR_SWITCH, "true");
        System.setProperty(Configs.CONN_RECONNECT_SWITCH, "true");
    }

    /**
     * 构建游戏网关
     *
     * @return 游戏网关
     */
    public BrokerServer build() {

        this.checked();

        if (Objects.isNull(this.brokerId)) {
            this.brokerId = UUID.randomUUID().toString();
        }

        BalancedManager balancedManager = brokerServer.getBalancedManager();
        LogicBrokerClientLoadBalanced logicBalanced = balancedManager.getLogicBalanced();
        logicBalanced.setBrokerClientRegionFactory(this.brokerClientRegionFactory);

        brokerServer
                .setBrokerId(this.brokerId)
                .setBrokerRunMode(this.brokerRunMode)
                .setPort(this.port)
        ;

        // 初始化 boltRpcServer
        brokerServer.initRpcServer();

        RpcServer rpcServer = brokerServer.getRpcServer();

        // 注册用户处理器 添加到 bolt rpcServer 中
        this.processorList.forEach(processorSupplier -> {

            UserProcessor<?> userProcessor = processorSupplier.get();

            aware(userProcessor);

            rpcServer.registerUserProcessor(userProcessor);
        });

        // 注册连接器 添加到 bolt rpcServer 中
        connectionEventProcessorMap.forEach((type, valueSupplier) -> {

            var processor = valueSupplier.get();

            aware(processor);

            rpcServer.addConnectionEventProcessor(type, processor);
        });

        // 集群相关
        this.cluster();

        return brokerServer;
    }

    /**
     * 注册用户处理器
     *
     * @param processorSupplier processor
     * @return this
     */
    public BrokerServerBuilder registerUserProcessor(Supplier<UserProcessor<?>> processorSupplier) {
        this.processorList.add(processorSupplier);
        return this;
    }

    /**
     * 注册连接器
     *
     * @param type              type
     * @param processorSupplier processorSupplier
     * @return this
     */
    public BrokerServerBuilder addConnectionEventProcessor(ConnectionEventType type, Supplier<ConnectionEventProcessor> processorSupplier) {
        this.connectionEventProcessorMap.put(type, processorSupplier);
        return this;
    }

    /**
     * 集群构建器
     * <pre>
     *     如果不设置，表示不需要集群
     * </pre>
     *
     * @param brokerClusterManagerBuilder brokerClusterManagerBuilder
     * @return this
     */
    public BrokerServerBuilder brokerClusterManagerBuilder(BrokerClusterManagerBuilder brokerClusterManagerBuilder) {

        if (Objects.isNull(brokerClusterManagerBuilder)) {
            return this;
        }

        this.brokerClusterManagerBuilder = brokerClusterManagerBuilder;
        // 表示集群方式启动 broker （游戏网关）
        this.brokerRunMode = BrokerRunModeEnum.CLUSTER;

        return this;
    }

    /**
     * 移除所有默认 处理器
     * <pre>
     *     如果框架满足不了你的业务，你可以把框架默认的处理器移除，这样就可以完全的重新定义
     * </pre>
     *
     * @return this
     */
    public BrokerServerBuilder clearProcessor() {
        this.processorList.clear();
        this.connectionEventProcessorMap.clear();
        return this;
    }

    private void cluster() {
        // 单机模式，不做处理
        if (this.brokerRunMode != BrokerRunModeEnum.CLUSTER) {
            return;
        }

        Objects.requireNonNull(this.brokerClusterManagerBuilder, "开启集群模式 brokerClusterManagerBuilder 必须不为 null!");

        // ==========到这里表示是集群模式==========
        BrokerClusterManager brokerClusterManager = brokerClusterManagerBuilder.build(this.brokerServer);

        // 设置集群管理器
        brokerServer.setBrokerClusterManager(brokerClusterManager);
    }

    private void checked() {
        if (this.port <= 0) {
            throw new RuntimeException("port error!");
        }

        if (Objects.isNull(this.brokerRunMode)) {
            throw new RuntimeException("brokerRunMode expected: " + Arrays.toString(BrokerRunModeEnum.values()));
        }
    }

    private void defaultProcessor() {
        // ============================注册连接器============================

        Supplier<ConnectionEventProcessor> closeConnectionEventSupplier = CloseConnectionEventBrokerProcessor::new;
        Supplier<ConnectionEventProcessor> connectionEventSupplier = ConnectionEventBrokerProcessor::new;

        this
                .addConnectionEventProcessor(ConnectionEventType.CONNECT, connectionEventSupplier)
                .addConnectionEventProcessor(ConnectionEventType.CLOSE, closeConnectionEventSupplier);

        // ============================注册用户处理器============================

        // 处理 - 模块注册（逻辑服注册）
        Supplier<UserProcessor<?>> registerSupplier = RegisterBrokerClientMessageBrokerProcessor::new;

        // 处理 - (接收真实用户的请求) 把对外服的请求转发到逻辑服
        Supplier<UserProcessor<?>> externalMessageSupplier = ExternalRequestMessageBrokerProcessor::new;

        // 处理 - 改变用户 id -- external server
        Supplier<UserProcessor<?>> changeUserIdMessageSupplier = ChangeUserIdMessageBrokerProcessor::new;

        // 处理 - （响应真实用户的请求）把逻辑服的响应转发到对外服
        Supplier<UserProcessor<?>> responseMessageSupplier = ResponseMessageBrokerProcessor::new;

        // 处理 - 内部模块消息的转发
        Supplier<UserProcessor<?>> innerModuleMessageSupplier = InnerModuleMessageBrokerProcessor::new;
        // 处理 - 内部模块消息的转发
        Supplier<UserProcessor<?>> innerModuleVoidMessageSupplier = InnerModuleVoidMessageBrokerProcessor::new;

        // 处理 - 模块之间的访问，访问同类型的多个逻辑服
        Supplier<UserProcessor<?>> innerModuleRequestCollectMessageSupplier = InnerModuleRequestCollectMessageBrokerProcessor::new;
        // 处理 - 模块之间的访问，游戏逻辑服同时访问多个游戏对外服
        Supplier<UserProcessor<?>> innerModuleRequestCollectExternalMessageSupplier = InnerModuleRequestCollectExternalMessageBrokerProcessor::new;

        // 处理 - 把绑定消息转发到对外服
        Supplier<UserProcessor<?>> endPointLogicServerMessageSupplier = EndPointLogicServerMessageBrokerProcessor::new;

        // 处理 - 广播
        Supplier<UserProcessor<?>> broadcastMessageSupplier = BroadcastMessageBrokerProcessor::new;
        // 处理 - 顺序的广播
        Supplier<UserProcessor<?>> broadcastOrderMessageSupplier = BroadcastOrderMessageBrokerProcessor::new;

        Supplier<UserProcessor<?>> brokerClientItemConnectMessageSupplier = BrokerClientItemConnectMessageBrokerProcessor::new;

        // 处理 - 扩展逻辑服的请求信息
        Supplier<UserProcessor<?>> extRequestMessageSupplier = ExtRequestMessageBrokerProcessor::new;
        // 处理 - 响应信息给扩展逻辑服
        Supplier<UserProcessor<?>> extResponseMessageSupplier = ExtResponseMessageBrokerProcessor::new;

        this
                .registerUserProcessor(registerSupplier)
                .registerUserProcessor(externalMessageSupplier)
                .registerUserProcessor(changeUserIdMessageSupplier)
                .registerUserProcessor(responseMessageSupplier)
                .registerUserProcessor(innerModuleMessageSupplier)
                .registerUserProcessor(innerModuleVoidMessageSupplier)
                .registerUserProcessor(innerModuleRequestCollectMessageSupplier)
                .registerUserProcessor(innerModuleRequestCollectExternalMessageSupplier)
                .registerUserProcessor(broadcastMessageSupplier)
                .registerUserProcessor(broadcastOrderMessageSupplier)
                .registerUserProcessor(brokerClientItemConnectMessageSupplier)
                .registerUserProcessor(endPointLogicServerMessageSupplier)
                .registerUserProcessor(extRequestMessageSupplier)
                .registerUserProcessor(extResponseMessageSupplier)
        ;
    }

    private void aware(Object obj) {
        if (obj instanceof BrokerServerAware aware) {
            aware.setBrokerServer(this.brokerServer);
        }
    }
}