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
package com.iohao.game.bolt.broker.core.client;

import com.alipay.remoting.Connection;
import com.alipay.remoting.ConnectionEventProcessor;
import com.alipay.remoting.ConnectionEventType;
import com.alipay.remoting.config.BoltClientOption;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcClient;
import com.alipay.remoting.rpc.protocol.UserProcessor;
import com.iohao.game.action.skeleton.core.BarMessageKit;
import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.SkeletonAttr;
import com.iohao.game.action.skeleton.core.commumication.CommunicationAggregationContext;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.action.skeleton.protocol.SyncRequestMessage;
import com.iohao.game.action.skeleton.protocol.collect.RequestCollectMessage;
import com.iohao.game.action.skeleton.protocol.collect.ResponseCollectMessage;
import com.iohao.game.action.skeleton.protocol.external.RequestCollectExternalMessage;
import com.iohao.game.action.skeleton.protocol.external.ResponseCollectExternalMessage;
import com.iohao.game.action.skeleton.pulse.Pulses;
import com.iohao.game.action.skeleton.pulse.core.consumer.PulseConsumers;
import com.iohao.game.action.skeleton.pulse.core.producer.PulseProducers;
import com.iohao.game.bolt.broker.core.aware.*;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.bolt.broker.core.message.BrokerClientItemConnectMessage;
import com.iohao.game.bolt.broker.core.message.BrokerClientModuleMessage;
import com.iohao.game.bolt.broker.core.message.InnerModuleMessage;
import com.iohao.game.bolt.broker.core.message.InnerModuleVoidMessage;
import com.iohao.game.common.kit.CollKit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * 客户连接项
 * <pre>
 *     与游戏网关是 1:1 的关系
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-14
 */
@Slf4j
@Getter
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrokerClientItem implements CommunicationAggregationContext, AwareInject {
    public enum Status {
        /** 活跃 */
        ACTIVE,
        /** 断开：与网关断开了 */
        DISCONNECT;
    }

    final RpcClient rpcClient;
    /** 广播 */
    final Broadcast broadcast = new Broadcast(this);

    /** 与 broker 通信的连接 */
    Connection connection;
    /** ip:port */
    String address;
    /** 消息发送超时时间 */
    int timeoutMillis = IoGameGlobalConfig.timeoutMillis;
    /** 业务框架 */
    BarSkeleton barSkeleton;
    /** broker 的 client */
    BrokerClient brokerClient;

    Status status = Status.DISCONNECT;
    /** aware 注入扩展 */
    AwareInject awareInject;
    int brokerServerWithNo;

    public BrokerClientItem(String address) {
        this.address = address;
        this.rpcClient = new RpcClient();
        // 重连选项
        rpcClient.option(BoltClientOption.CONN_RECONNECT_SWITCH, true);
        rpcClient.option(BoltClientOption.CONN_MONITOR_SWITCH, true);
    }

    public Object invokeSync(final Object request, final int timeoutMillis) throws RemotingException, InterruptedException {
        return rpcClient.invokeSync(connection, request, timeoutMillis);
    }

    public Object invokeSync(final Object request) throws RemotingException, InterruptedException {
        return invokeSync(request, timeoutMillis);
    }

    public void oneway(final Object request) throws RemotingException {
        this.rpcClient.oneway(connection, request);
    }

    void invokeWithCallback(Object request) throws RemotingException {
        this.rpcClient.invokeWithCallback(connection, request, null, timeoutMillis);
    }

    @Override
    public void broadcast(ResponseMessage responseMessage, Collection<Long> userIdList) {

        if (CollKit.isEmpty(userIdList)) {
            log.warn("广播无效 userIdList : {}", userIdList);
            return;
        }

        this.broadcast.broadcast(responseMessage, userIdList);
    }

    @Override
    public void broadcast(ResponseMessage responseMessage, long userId) {
        this.broadcast.broadcast(responseMessage, userId);
    }

    @Override
    public void broadcast(ResponseMessage responseMessage) {
        this.broadcast.broadcast(responseMessage);
    }

    @Override
    public void broadcastOrder(ResponseMessage responseMessage, Collection<Long> userIdList) {
        this.broadcast.broadcastOrder(responseMessage, userIdList);
    }

    @Override
    public void broadcastOrder(ResponseMessage responseMessage, long userId) {
        this.broadcast.broadcastOrder(responseMessage, userId);
    }

    @Override
    public void broadcastOrder(ResponseMessage responseMessage) {
        this.broadcast.broadcastOrder(responseMessage);
    }

    @Override
    public ResponseMessage invokeModuleMessage(RequestMessage requestMessage) {

        InnerModuleMessage moduleMessage = new InnerModuleMessage();
        moduleMessage.setRequestMessage(requestMessage);

        ResponseMessage o = null;

        try {
            o = (ResponseMessage) this.invokeSync(moduleMessage);
        } catch (RemotingException | InterruptedException e) {
            log.error(e.getMessage(), e);
        }

        return o;
    }

    @Override
    public void invokeModuleVoidMessage(RequestMessage requestMessage) {

        InnerModuleVoidMessage moduleVoidMessage = new InnerModuleVoidMessage();
        moduleVoidMessage.setRequestMessage(requestMessage);

        try {
            this.oneway(moduleVoidMessage);
        } catch (RemotingException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public ResponseCollectMessage invokeModuleCollectMessage(RequestMessage requestMessage) {

        SyncRequestMessage syncRequestMessage = BarMessageKit.convertSyncRequestMessage(requestMessage);

        RequestCollectMessage requestCollectMessage = new RequestCollectMessage()
                .setRequestMessage(syncRequestMessage);

        try {
            return (ResponseCollectMessage) this.invokeSync(requestCollectMessage);
        } catch (RemotingException | InterruptedException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    @Override
    public ResponseCollectExternalMessage invokeExternalModuleCollectMessage(int bizCode, Serializable data) {
        RequestCollectExternalMessage request = new RequestCollectExternalMessage()
                .setBizCode(bizCode)
                .setData(data);

        return this.invokeExternalModuleCollectMessage(request);
    }

    @Override
    public ResponseCollectExternalMessage invokeExternalModuleCollectMessage(RequestCollectExternalMessage request) {
        try {
            return (ResponseCollectExternalMessage) this.invokeSync(request);
        } catch (RemotingException | InterruptedException e) {
            log.error(e.getMessage(), e);
        }

        /*
         * 给一个空对象，这样调用端可以减少一些 null 判断。
         * 而且正常情况下，也走不到这里。
         */
        return new ResponseCollectExternalMessage();
    }

    @Override
    public void invokeOneway(Object message) {
        this.internalOneway(message);
    }

    void addConnectionEventProcessor(ConnectionEventType type, ConnectionEventProcessor processor) {

        aware(processor);

        this.rpcClient.addConnectionEventProcessor(type, processor);
    }

    void registerUserProcessor(UserProcessor<?> processor) {

        aware(processor);

        this.rpcClient.registerUserProcessor(processor);
    }

    @Override
    public void aware(Object obj) {
        /*
         * 目前 aware 系列由框架提供，
         * 虽然这里可以开放给开发者来控制，但目前暂时不考虑开放
         */
        if (Objects.nonNull(this.awareInject)) {
            this.awareInject.aware(obj);
        }

        if (obj instanceof BrokerClientItemAware aware) {
            aware.setBrokerClientItem(this);
        }

        if (obj instanceof BrokerClientAware aware) {
            aware.setBrokerClient(this.brokerClient);
        }

        if (obj instanceof UserProcessorExecutorAware aware && Objects.isNull(aware.getUserProcessorExecutor())) {
            // 如果开发者没有自定义 Executor，则使用框架提供的 Executor 策略
            Executor executor = IoGameGlobalConfig.getExecutor(aware);
            aware.setUserProcessorExecutor(executor);
        }

        if (obj instanceof PulseConsumerAware aware) {
            Pulses pulses = this.barSkeleton.option(SkeletonAttr.pulses);
            PulseConsumers pulseConsumers = pulses.getPulseConsumers();
            aware.setPulseConsumers(pulseConsumers);
        }

        if (obj instanceof PulseProducerAware aware) {
            Pulses pulses = this.barSkeleton.option(SkeletonAttr.pulses);
            PulseProducers pulseProducers = pulses.getPulseProducers();
            aware.setPulseProducers(pulseProducers);
        }
    }

    private void internalOneway(Object responseObject) {
        try {
            rpcClient.oneway(connection, responseObject);
        } catch (RemotingException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 注册到网关 broker
     * 客户端服务器注册到网关服
     */
    public void registerToBroker() {
        // 客户端服务器注册到游戏网关服
        BrokerClientModuleMessage brokerClientModuleMessage = this.brokerClient.getBrokerClientModuleMessage();

        try {

            this.rpcClient.oneway(address, brokerClientModuleMessage);

            TimeUnit.MILLISECONDS.sleep(100);
            this.status = Status.ACTIVE;
            this.brokerClient.getBrokerClientManager().resetSelector();

            this.barSkeleton.getRunners().onStartAfter();

            this.with();
        } catch (RemotingException | InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void startup() {
        this.rpcClient.startup();
        this.send();
    }

    private void send() {
        BrokerClientItemConnectMessage message = new BrokerClientItemConnectMessage();
        try {
            this.rpcClient.oneway(address, message);
        } catch (RemotingException | InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void with() {
        int withNo = this.brokerClient.getWithNo();

        if (withNo == 0 || withNo != this.brokerServerWithNo) {
            this.brokerServerWithNo = 0;
            return;
        }

        // 连接与当前 brokerClientItem 是同一个进程的。
        BrokerClientManager manager = brokerClient.getBrokerClientManager();
        manager.setBrokerClientItemWith(this);
    }
}
