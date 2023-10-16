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
package com.iohao.game.bolt.broker.server;

import com.alipay.remoting.ConnectionEventProcessor;
import com.alipay.remoting.ConnectionEventType;
import com.alipay.remoting.config.Configs;
import com.alipay.remoting.rpc.RpcServer;
import com.alipay.remoting.rpc.protocol.UserProcessor;
import com.iohao.game.bolt.broker.cluster.BrokerClusterManager;
import com.iohao.game.bolt.broker.cluster.BrokerClusterManagerBuilder;
import com.iohao.game.bolt.broker.cluster.BrokerRunModeEnum;
import com.iohao.game.bolt.broker.core.aware.AwareInject;
import com.iohao.game.bolt.broker.core.aware.CmdRegionsAware;
import com.iohao.game.bolt.broker.core.aware.UserProcessorExecutorAware;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.bolt.broker.server.aware.BrokerClientModulesAware;
import com.iohao.game.bolt.broker.server.aware.BrokerServerAware;
import com.iohao.game.bolt.broker.server.balanced.BalancedManager;
import com.iohao.game.bolt.broker.server.balanced.LogicBrokerClientLoadBalanced;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientRegionFactory;
import com.iohao.game.bolt.broker.server.balanced.region.StrictBrokerClientRegion;
import com.iohao.game.bolt.broker.server.enhance.BrokerEnhances;
import com.iohao.game.bolt.broker.server.processor.*;
import com.iohao.game.bolt.broker.server.processor.connection.CloseConnectionEventBrokerProcessor;
import com.iohao.game.bolt.broker.server.processor.connection.ConnectionEventBrokerProcessor;
import com.iohao.game.bolt.broker.server.service.BrokerClientModules;
import com.iohao.game.bolt.broker.server.service.DefaultBrokerClientModules;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingHashMap;

import java.util.*;
import java.util.concurrent.Executor;
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
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrokerServerBuilder implements AwareInject {
    /** broker （游戏网关） */
    final BrokerServer brokerServer = new BrokerServer();
    /** 用户处理器 */
    final List<Supplier<UserProcessor<?>>> processorList = new ArrayList<>();
    /** bolt 连接器 */
    final Map<ConnectionEventType, Supplier<ConnectionEventProcessor>> connectionEventProcessorMap = new NonBlockingHashMap<>();
    final BrokerClientModules brokerClientModules = new DefaultBrokerClientModules();

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
    int port = IoGameGlobalConfig.brokerPort;
    /** broker （游戏网关）的启动模式，默认单机模式 */
    @Setter
    BrokerRunModeEnum brokerRunMode = BrokerRunModeEnum.STANDALONE;
    /** 集群的管理 构建器，如果不需要集群，可以不设置 */
    BrokerClusterManagerBuilder brokerClusterManagerBuilder;

    /** BrokerClientRegion 工厂 */
    @Setter
    BrokerClientRegionFactory brokerClientRegionFactory = StrictBrokerClientRegion::new;

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
                .setBrokerClientModules(this.brokerClientModules)
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
     * 移除用户处理器，使之不与自定义的用户处理器冲突
     *
     * @param clazz 待移除的用户处理器类型
     * @return this
     */
    public BrokerServerBuilder removeUserProcessor(Class<? extends UserProcessor<?>> clazz) {

        if (clazz != null) {
            this.processorList.removeIf(processorSupplier -> processorSupplier.get().getClass().equals(clazz));
        }

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
        BrokerClusterManager brokerClusterManager = this.brokerClusterManagerBuilder.build(this.brokerServer);

        // 设置集群管理器
        this.brokerServer.setBrokerClusterManager(brokerClusterManager);
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
        Supplier<UserProcessor<?>> registerSupplier = RegisterBrokerClientModuleMessageBrokerProcessor::new;

        // 处理 - (接收真实用户的请求) 把对外服的请求转发到逻辑服
        Supplier<UserProcessor<?>> externalMessageSupplier = RequestMessageBrokerProcessor::new;

        // 处理 - 改变用户 id -- external server
        Supplier<UserProcessor<?>> changeUserIdMessageSupplier = SettingUserIdMessageBrokerProcessor::new;

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
                // 处理 - 接收脉冲生产者-的脉冲信号
                .registerUserProcessor(PulseSignalRequestBrokerProcessor::new)
                // 处理 - 接收脉冲消费者-的脉冲信号
                .registerUserProcessor(PulseSignalResponseBrokerProcessor::new)
        ;

        BrokerEnhances.enhance(this);
    }

    @Override
    public void aware(Object obj) {
        /*
         * 目前 aware 系列由框架提供，
         * 虽然这里可以开放给开发者来控制，但目前暂时不考虑开放
         */

        if (obj instanceof BrokerServerAware aware) {
            aware.setBrokerServer(this.brokerServer);
        }

        if (obj instanceof CmdRegionsAware aware) {
            aware.setCmdRegions(this.brokerServer.getCmdRegions());
        }

        if (obj instanceof UserProcessorExecutorAware aware && Objects.isNull(aware.getUserProcessorExecutor())) {
            // 如果开发者没有自定义 Executor，则使用框架提供的 Executor 策略
            Executor executor = IoGameGlobalConfig.getExecutor(aware);
            aware.setUserProcessorExecutor(executor);
        }

        if (obj instanceof BrokerClientModulesAware aware) {
            aware.setBrokerClientModules(this.brokerClientModules);
        }
    }
}