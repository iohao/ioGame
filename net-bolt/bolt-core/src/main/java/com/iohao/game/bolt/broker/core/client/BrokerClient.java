/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
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
package com.iohao.game.bolt.broker.core.client;

import com.alipay.remoting.ConnectionEventProcessor;
import com.alipay.remoting.ConnectionEventType;
import com.alipay.remoting.config.Configs;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.protocol.UserProcessor;
import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.commumication.BrokerClientContext;
import com.iohao.game.action.skeleton.core.commumication.CommunicationAggregationContext;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.bolt.broker.core.aware.ProcessorAwareContext;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.bolt.broker.core.common.processor.hook.ClientProcessorHooks;
import com.iohao.game.bolt.broker.core.message.BrokerClientModuleMessage;
import com.iohao.game.common.kit.log.IoGameLoggerFactory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * BrokerClient 是与 broker（游戏网关）通信的 client
 * <p>
 * 对外服、逻辑服都是 broker 的 client
 * <pre>
 *     see {@link BrokerClientBuilder#build()}
 *     see {@link BrokerClientHelper}
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-14
 */
@Getter
@Setter(value = AccessLevel.PROTECTED)
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrokerClient implements BrokerClientContext {
    static final Logger log = IoGameLoggerFactory.getLoggerCommon();

    /** 服务器唯一标识 */
    String id;
    /**
     * 逻辑服标签 （tag 相当于归类）
     * <pre>
     *     用于逻辑服的归类
     *     假设逻辑服： 战斗逻辑服 启动了两台或以上，为了得到启动连接的逻辑服，我们可以通过 tag 在后台查找
     *     相同的逻辑服一定要用相同的 tag
     *
     *     注意，如果没设置这个值，会使用 this.appName 的值
     * </pre>
     */
    String tag;
    /** 模块名 */
    String appName;

    /** 业务框架 */
    BarSkeleton barSkeleton;

    /** 连接 broker （游戏网关） 的地址 */
    BrokerAddress brokerAddress;
    /** 逻辑服类型 */
    BrokerClientType brokerClientType = BrokerClientType.LOGIC;
    /** 模块信息 （子游戏服的信息、逻辑服） */
    BrokerClientModuleMessage brokerClientModuleMessage;

    /** 消息发送超时时间 */
    int timeoutMillis = IoGameGlobalConfig.timeoutMillis;

    BrokerClientManager brokerClientManager;

    /** 连接事件 */
    Map<ConnectionEventType, Supplier<ConnectionEventProcessor>> connectionEventProcessorMap;
    /** 用户处理器 */
    List<Supplier<UserProcessor<?>>> processorList;

    AtomicBoolean initAtomic = new AtomicBoolean(false);

    /** bolt 业务处理器的钩子管理器 */
    ClientProcessorHooks clientProcessorHooks;

    ProcessorAwareContext processorAwareContext;

    BrokerClient() {
        // 开启 bolt 重连, 通过系统属性来开和关，如果一个进程有多个 RpcClient，则同时生效
        System.setProperty(Configs.CONN_MONITOR_SWITCH, "true");
        System.setProperty(Configs.CONN_RECONNECT_SWITCH, "true");
    }

    public static BrokerClientBuilder newBuilder() {
        return new BrokerClientBuilder();
    }

    public void init() {

        if (initAtomic.get()) {
            return;
        }

        initAtomic.set(true);

        this.initBoltClientManager();
    }

    private BrokerClientItem next() {
        return this.brokerClientManager.next();
    }

    @Override
    public CommunicationAggregationContext getCommunicationAggregationContext() {
        return this.brokerClientManager.next();
    }

    public Object invokeSync(final Object request, final int timeoutMillis) throws RemotingException, InterruptedException {
        BrokerClientItem nextClient = next();
        return nextClient.invokeSync(request, timeoutMillis);
    }

    public Object invokeSync(final Object request) throws RemotingException, InterruptedException {
        return invokeSync(request, timeoutMillis);
    }

    @Override
    public void oneway(final Object request) throws Exception {
        BrokerClientItem nextClient = next();
        nextClient.oneway(request);
    }

    void invokeWithCallback(Object request) throws RemotingException {
        BrokerClientItem nextClient = next();
        nextClient.invokeWithCallback(request);
    }

    /**
     * 使用 BroadcastContext 代替，方法将在下个大版本中移除
     *
     * @param responseMessage responseMessage
     * @param userIdList      userIdList
     */
    @Deprecated
    public void broadcast(ResponseMessage responseMessage, Collection<Long> userIdList) {
        BrokerClientItem nextClient = next();
        nextClient.broadcast(responseMessage, userIdList);
    }

    /**
     * 使用 BroadcastContext 代替，方法将在下个大版本中移除
     *
     * @param responseMessage responseMessage
     * @param userId          userId
     */
    @Deprecated
    public void broadcast(ResponseMessage responseMessage, long userId) {
        BrokerClientItem nextClient = next();
        nextClient.broadcast(responseMessage, userId);
    }

    /**
     * 使用 BroadcastContext 代替，方法将在下个大版本中移除
     *
     * @param responseMessage responseMessage
     */
    @Deprecated
    public void broadcast(ResponseMessage responseMessage) {
        BrokerClientItem nextClient = next();
        nextClient.broadcast(responseMessage);
    }

    @Override
    public void sendResponse(Object responseObject) {
        try {
            BrokerClientItem nextClient = next();
            nextClient.oneway(responseObject);
        } catch (RemotingException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void initBoltClientManager() {
        if (Objects.isNull(this.brokerClientManager)) {
            this.brokerClientManager = new BrokerClientManager();
        }

        this.brokerClientManager
                .setBrokerAddress(this.brokerAddress)
                .setConnectionEventProcessorMap(this.connectionEventProcessorMap)
                .setProcessorList(this.processorList)
                .setBarSkeleton(this.barSkeleton)
                .setTimeoutMillis(this.timeoutMillis)
                .setBrokerClient(this)
        ;

        this.brokerClientManager.init();

    }
}
