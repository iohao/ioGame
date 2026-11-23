/*
 * ioGame
 * Copyright (C) 2021 - present  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 */
package com.iohao.game.bolt.broker.core.client;

import com.alipay.remoting.Connection;
import com.alipay.remoting.ConnectionEventProcessor;
import com.alipay.remoting.ConnectionEventType;
import com.alipay.remoting.config.BoltClientOption;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcClient;
import com.alipay.remoting.rpc.protocol.UserProcessor;
import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.SkeletonAttr;
import com.iohao.game.action.skeleton.core.commumication.CommunicationAggregationContext;
import com.iohao.game.action.skeleton.core.exception.ActionErrorEnum;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
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
import com.iohao.game.common.kit.concurrent.timer.delay.DelayTaskKit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

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

    /** 当前节点状态，默认断开 */
    Status status = Status.DISCONNECT;
    /** aware 注入扩展 */
    AwareInject awareInject;
    int brokerServerWithNo;

    // ================== 重试相关字段 ==================
    /** 重试任务的唯一标识（实例级别） */
    private final String retryTaskId;
    /** 最大重试次数,默认0不重试,-1无限重试 */
    private int maxRetryCount = IoGameGlobalConfig.maxRetryCount;
    /** 重试间隔（毫秒） 默认30秒*/
    private long retryDelayMillis = IoGameGlobalConfig.retryDelayMillis;
    /** 当前重试次数 */
    private int retryCount = 0;
    /** 是否正在重试中（防止并发触发） */
    private final AtomicBoolean isRetrying = new AtomicBoolean(false);
    // ====================================================

    public BrokerClientItem(String address) {
        this.address = address;
        // 使用 address + 实例哈希码 作为唯一标识，确保多实例不冲突
        this.retryTaskId = "broker_reconnect_" + address + "_" + System.identityHashCode(this);
        this.rpcClient = new RpcClient();
        // 重连选项
        rpcClient.option(BoltClientOption.CONN_RECONNECT_SWITCH, true);
        rpcClient.option(BoltClientOption.CONN_MONITOR_SWITCH, true);
    }

    private String getClientName() {
        return this.brokerClient != null ? this.brokerClient.getAppName() : this.address;
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
        try {
            InnerModuleMessage moduleMessage = new InnerModuleMessage();
            moduleMessage.setRequestMessage(requestMessage);
            return (ResponseMessage) this.invokeSync(moduleMessage);
        } catch (RemotingException | InterruptedException e) {
            log.error(e.getMessage(), e);
            var responseMessage = requestMessage.createResponseMessage();
            responseMessage.setResponseStatus(ActionErrorEnum.systemOtherErrCode.getCode());
            responseMessage.setValidatorMsg(e.getMessage());
            return responseMessage;
        }
    }

    @Override
    public void invokeModuleVoidMessage(RequestMessage requestMessage) {
        try {
            InnerModuleVoidMessage moduleVoidMessage = new InnerModuleVoidMessage();
            moduleVoidMessage.setRequestMessage(requestMessage);
            this.oneway(moduleVoidMessage);
        } catch (RemotingException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public ResponseCollectMessage invokeModuleCollectMessage(RequestMessage requestMessage) {
        RequestCollectMessage requestCollectMessage = new RequestCollectMessage()
                .setRequestMessage(requestMessage);

        try {
            return (ResponseCollectMessage) this.invokeSync(requestCollectMessage);
        } catch (RemotingException | InterruptedException e) {
            log.error(e.getMessage(), e);
            ResponseCollectMessage responseCollectMessage = new ResponseCollectMessage();
            responseCollectMessage.setStatusCode(ActionErrorEnum.systemOtherErrCode.getCode());
            responseCollectMessage.setStatusMes(e.getMessage());
            responseCollectMessage.setMessageList(Collections.emptyList());
            return responseCollectMessage;
        }
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
        return new ResponseCollectExternalMessage();
    }

    @Override
    public void invokeOneway(Object message) {
        this.internalOneway(message);
    }

    private void internalOneway(Object responseObject) {
        try {
            rpcClient.oneway(connection, responseObject);
        } catch (RemotingException e) {
            log.error(e.getMessage(), e);
        }
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
        if (Objects.nonNull(this.awareInject)) {
            this.awareInject.aware(obj);
        }
        AwareKit.aware(obj);
        if (obj instanceof BrokerClientItemAware aware) {
            aware.setBrokerClientItem(this);
        }
        if (obj instanceof BrokerClientAware aware) {
            aware.setBrokerClient(this.brokerClient);
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

    /**
     * 注册到网关
     * <p>
     * 此方法由 RequestBrokerClientModuleMessageClientProcessor 触发
     * 负责将模块信息（cmd列表）发送给网关。
     *
     * @return true 注册成功，false 注册失败
     */
    public boolean registerToBroker() {
        try {
            BrokerClientModuleMessage brokerClientModuleMessage = this.brokerClient.getBrokerClientModuleMessage();
            // 发送真正的业务模块信息给网关
            this.rpcClient.oneway(address, brokerClientModuleMessage);

            // 执行启动后的回调
            this.barSkeleton.getRunners().onStartAfter();
            this.with();

            log.info("【{}】逻辑服业务注册网关成功: {}", getClientName(), address);
            return true;
        } catch (Exception e) {
            log.error("【{}】逻辑服业务注册网关失败: {}", getClientName(), address, e);
            return false;
        }
    }

    /**
     * 启动客户端（非阻塞）
     */
    public void startup() {
        this.rpcClient.startup();
        // 首次尝试连接（非阻塞）
        this.tryConnect();
    }

    /**
     * 尝试连接网关（内部方法）
     * 注意：这里只负责发送握手消息，状态流转交由 ConnectEventClientProcessor 处理
     */
    private void tryConnect() {
        if (this.status == Status.ACTIVE) {
            return;
        }

        if (!isRetrying.compareAndSet(false, true)) {
            log.debug("【{}】已有重试任务进行中，忽略本次触发", getClientName());
            return;
        }

        try {
            // 1. 发送连接握手消息，唤醒底层 Bolt 去建连
            var message = new BrokerClientItemConnectMessage();
            this.rpcClient.oneway(address, message);

            // 2. 发送后释放锁，真正的状态修改和业务注册由底层 Bolt 触发 CONNECT 事件后，
            // 交给 ConnectEventClientProcessor 自动完成。这里不再手动注册。
            isRetrying.set(false);

        } catch (Exception e) {
            isRetrying.set(false);
            log.warn("【{}】无法连接网关 ({}): {}，将在稍后重试", getClientName(), address, e.getMessage());
            this.scheduleRetry(e);
        }
    }

    /**
     * 使用 DelayTaskKit 调度下一次重试
     */
    private void scheduleRetry(Exception e) {
        if (maxRetryCount != -1 && retryCount >= maxRetryCount) {
            log.error("【{}】重试次数已达上限 ({}), 放弃连接: {}", getClientName(), maxRetryCount, address);
            return;
        }

        retryCount++;
        DelayTaskKit.of(retryTaskId, () -> {
            log.info("【{}】执行第 {} 次重试，连接网关: {}", getClientName(), retryCount, address);
            this.tryConnect();
        }).plusTime(Duration.ofMillis(retryDelayMillis)).task();

        if (e.getCause() instanceof UnknownHostException) {
            log.warn("【{}】DNS解析失败 ({}), {} 秒后重试 (第 {} 次)",
                    getClientName(), address, retryDelayMillis / 1000, retryCount);
        } else {
            log.warn("【{}】连接网关失败 ({}), {} 秒后重试 (第 {} 次): {}",
                    getClientName(), address, retryDelayMillis / 1000, retryCount, e.getMessage());
        }
    }

    public void cancelRetry() {
        DelayTaskKit.cancel(retryTaskId);
        isRetrying.set(false);
        log.debug("【{}】已取消网关重连任务: {}", getClientName(), address);
    }

    private void with() {
        int withNo = this.brokerClient.getWithNo();
        if (withNo == 0 || withNo != this.brokerServerWithNo) {
            this.brokerServerWithNo = 0;
            return;
        }
        BrokerClientManager manager = brokerClient.getBrokerClientManager();
        manager.setBrokerClientItemWith(this);
    }
}