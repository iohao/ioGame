/*
 * ioGame
 * Copyright (C) 2021 - present  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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
import java.util.concurrent.TimeUnit;
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
    /** 是否注册到网关 */
    private final AtomicBoolean registered = new AtomicBoolean(false);
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

    /**
     * 获取逻辑服名称（用于日志标识）
     * 如果 brokerClient 尚未设置，则返回 address 作为降级
     */
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

    private void internalOneway(Object responseObject) {
        try {
            rpcClient.oneway(connection, responseObject);
        } catch (RemotingException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 注册到网关（保持 public，供外部调用）
     * <p>
     * 此方法只执行注册逻辑，不触发重试。
     * 外部调用时（如网关请求），不触发重试，由外部决定是否重试。
     *
     * @return true 注册成功，false 注册失败
     */
    public boolean registerToBroker() {
        // 防止重复注册（无论来自哪里）
        if (!registered.compareAndSet(false, true)) {
            log.debug("【{}】已注册或正在注册，忽略重复调用", getClientName());
            return true;  // 返回 true 表示已注册成功
        }
        try {
            BrokerClientModuleMessage brokerClientModuleMessage = this.brokerClient.getBrokerClientModuleMessage();
            this.rpcClient.oneway(address, brokerClientModuleMessage);

            TimeUnit.MILLISECONDS.sleep(100);
            this.brokerClient.getBrokerClientManager().resetSelector();
            this.barSkeleton.getRunners().onStartAfter();
            this.with();

            log.info("【{}】逻辑服注册网关成功: {}", getClientName(), address);
            return true;
        } catch (RemotingException | InterruptedException e) {
            log.error("【{}】注册网关失败: {}", getClientName(), address, e);
            // 注意：外部调用时（如 Processor），不触发重试，仅记录日志
            // 内部调用时（如 tryConnect），由调用方负责重试
            return false;
        }
    }

    /**
     * 启动客户端（非阻塞）
     * <p>
     * 首次尝试连接，若失败则通过 DelayTaskKit 自动重试，直到成功或达到最大重试次数。
     * 解决了 DNS 临时失效、网关未启动等场景下的连接恢复问题。
     */
    public void startup() {
        this.rpcClient.startup();

        // 注册 CONNECT 事件处理器：连接建立时尝试注册
        this.addConnectionEventProcessor(ConnectionEventType.CONNECT, (remoteAddr, conn) -> {
            // 延时一小段时间，确保连接完全就绪
            DelayTaskKit.of("connect_event_" + this.address, () -> {
                if (this.status == Status.ACTIVE) {
                    return; // 已注册，忽略
                }
                // 尝试连接并注册（会发送握手消息并调用 registerToBroker）
                this.tryConnect();
            }).plusTime(Duration.ofMillis(200)).task();
        });

        // 注册 CLOSE 事件处理器（可选，用于重置状态）
        this.addConnectionEventProcessor(ConnectionEventType.CLOSE, (remoteAddr, conn) -> {
            this.status = Status.DISCONNECT;
            this.registered.set(false);
            this.cancelRetry();
            log.debug("【{}】网关连接关闭，已重置状态: {}", getClientName(), this.address);
        });

        // 首次尝试连接（启动时）
        this.tryConnect();
    }

    /**
     * 尝试连接网关（内部方法）
     * <p>
     * 发送握手消息，成功后调用 registerToBroker()，若失败则调度重试
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
            // 1. 发送连接握手消息（原 send 逻辑）
            var message = new BrokerClientItemConnectMessage();
            this.rpcClient.oneway(address, message);

            // 2. 连接成功，执行注册
            log.info("【{}】网关连接成功: {}", getClientName(), address);
            this.status = Status.ACTIVE;
            // 直接调用 public 方法注册
            // 如果注册失败，回退状态并触发重试
            if (!this.registerToBroker()) {
                this.status = Status.DISCONNECT;
                isRetrying.set(false);//先释放锁，再调度重试
                this.scheduleRetry(new RemotingException("注册失败"));
                return;
            }
            // 重置重试计数
            retryCount = 0;
            isRetrying.set(false);

        } catch (RemotingException | InterruptedException e) {
            // 连接失败（握手失败），释放锁并调度重试
            isRetrying.set(false);
            // 注意：如果是因为注册失败导致异常，这里也会重试，但由于 registerToBroker 内部捕获了异常，不会向外抛出
            // 所以这里捕获的主要是握手异常（如连接被拒绝、DNS 解析失败等）
            this.scheduleRetry(e);
        } catch (Exception e) {
            isRetrying.set(false);
            log.error("【{}】连接网关时发生未知异常，将重试", getClientName(), e);
            this.scheduleRetry(e);
        }
    }

    /**
     * 使用 DelayTaskKit 调度下一次重试
     */
    private void scheduleRetry(Exception e) {
        // 检查是否达到最大重试次数（-1 表示无限重试）
        if (maxRetryCount != -1 && retryCount >= maxRetryCount) {
            log.error("【{}】重试次数已达上限 ({}), 放弃连接: {}", getClientName(), maxRetryCount, address);
            return;
        }

        retryCount++;
        // 创建延时任务，覆盖之前的同名任务
        DelayTaskKit.of(retryTaskId, () -> {
            log.info("【{}】执行第 {} 次重试，连接网关: {}", getClientName(), retryCount, address);
            // 递归调用 tryConnect 进行重试
            this.tryConnect();
        }).plusTime(Duration.ofMillis(retryDelayMillis)).task();

        // 日志记录（区分 DNS 错误和其他错误）
        if (e.getCause() instanceof UnknownHostException) {
            log.warn("【{}】DNS解析失败 ({}), {} 秒后重试 (第 {} 次)",
                    getClientName(), address, retryDelayMillis / 1000, retryCount);
        } else {
            log.warn("【{}】连接网关失败 ({}), {} 秒后重试 (第 {} 次): {}",
                    getClientName(), address, retryDelayMillis / 1000, retryCount, e.getMessage());
        }
    }

    /**
     * 取消重试任务（可在应用关闭时调用）
     */
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

        // 连接与当前 brokerClientItem 是同一个进程的。
        BrokerClientManager manager = brokerClient.getBrokerClientManager();
        manager.setBrokerClientItemWith(this);
    }
}