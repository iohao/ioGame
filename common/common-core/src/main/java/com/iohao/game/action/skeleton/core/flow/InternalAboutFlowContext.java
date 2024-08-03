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
package com.iohao.game.action.skeleton.core.flow;

import com.iohao.game.action.skeleton.core.*;
import com.iohao.game.action.skeleton.core.commumication.*;
import com.iohao.game.action.skeleton.core.flow.attr.FlowAttr;
import com.iohao.game.action.skeleton.core.flow.attr.FlowOptionDynamic;
import com.iohao.game.action.skeleton.eventbus.EventBus;
import com.iohao.game.action.skeleton.eventbus.EventBusMessage;
import com.iohao.game.action.skeleton.kit.ExecutorSelectKit;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.action.skeleton.protocol.collect.ResponseCollectMessage;
import com.iohao.game.action.skeleton.protocol.external.RequestCollectExternalMessage;
import com.iohao.game.action.skeleton.protocol.external.ResponseCollectExternalMessage;
import com.iohao.game.common.kit.concurrent.executor.ExecutorRegion;
import com.iohao.game.common.kit.concurrent.executor.ThreadExecutor;
import com.iohao.game.common.kit.exception.ThrowKit;
import com.iohao.game.common.kit.trace.TraceKit;
import lombok.experimental.UtilityClass;
import org.slf4j.MDC;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * FlowContext 能力增强接口
 * <pre>
 *     {@link SimpleAttachment} 元信息相关的能力
 *
 *     {@link SimpleCommon} 动态属性相关的能力
 *     {@link SimpleBarMessageCreator} 创建 barMessage 消息相关的能力
 *     {@link SimpleExecutor} 线程执行器相关的能力
 * </pre>
 * 通信相关的能力
 * <pre>
 *     分布式事件总线相关通信
 *     {@link SimpleCommunicationEventBus}
 *
 *     广播相关通信
 *     {@link SimpleCommunicationBroadcast}
 *
 *     与游戏逻辑服相关通信
 *     {@link SimpleCommunicationInvokeModule}
 *     {@link SimpleCommunicationInvokeModuleVoid}
 *     {@link SimpleCommunicationInvokeModuleCollect}
 *
 *     与游戏对外服相关通信
 *     {@link SimpleCommunicationInvokeExternalModule}
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-12-27
 */
interface SimpleContext extends SimpleAttachment
        // 分布式事件总线相关通信
        , SimpleCommunicationEventBus
        // 广播相关通信
        , SimpleCommunicationBroadcast
        // 与游戏逻辑服相关通信
        , SimpleCommunicationInvokeModule
        , SimpleCommunicationInvokeModuleVoid
        , SimpleCommunicationInvokeModuleCollect
        // 与游戏对外服相关通信
        , SimpleCommunicationInvokeExternalModule {
}

/**
 * 帮助 FlowContext 得到更新、获取元信息的能力
 *
 * @author 渔民小镇
 * @date 2023-12-27
 */
interface SimpleAttachment extends SimpleCommunicationInvokeExternalModule {
    /**
     * 更新元信息
     * <pre>
     *     [同步更新]
     *
     *     将元信息更新到玩家所在的游戏对外服中
     * </pre>
     *
     * @param attachment 元信息
     */
    default void updateAttachment(final UserAttachment attachment) {

        HeadMetadata headMetadata = this.getHeadMetadata();
        long userId = headMetadata.getUserId();

        if (userId <= 0) {
            ThrowKit.ofRuntimeException("userId <= 0");
        }

        // 将元信息更新到 HeadMetadata 中
        byte[] headMetadataEncode = DataCodecKit.encode(attachment);
        headMetadata.setAttachmentData(headMetadataEncode);

        // 根据业务码，调用游戏对外服与业务码对应的业务实现类 （AttachmentExternalBizRegion、ExternalBizCodeCont）
        int bizCode = IoGameCommonCoreConfig.ExternalBizCode.attachment;
        this.invokeExternalModuleCollectMessage(bizCode, headMetadataEncode);
    }

    /**
     * 更新元信息
     * <pre>
     *     [异步更新]
     *
     *     将元信息更新到玩家所在的游戏对外服中
     * </pre>
     *
     * @param attachment 元信息
     */
    default void updateAttachmentAsync(UserAttachment attachment) {
        this.getVirtualExecutor().execute(() -> this.updateAttachment(attachment));
    }

    /**
     * 更新元信息
     * <pre>
     *     [同步更新]
     *
     *     将元信息更新到玩家所在的游戏对外服中
     * </pre>
     */
    default void updateAttachment() {
        UserAttachment attachment = this.getAttachment();
        this.updateAttachment(attachment);
    }

    /**
     * 更新元信息
     * <pre>
     *     [异步更新]
     *
     *     将元信息更新到玩家所在的游戏对外服中
     * </pre>
     */
    default void updateAttachmentAsync() {
        this.getVirtualExecutor().execute(this::updateAttachment);
    }

    /**
     * 得到元附加信息
     * <pre>
     *     一般是在游戏对外服中设置的一些附加信息
     *     这些信息会跟随请求来到游戏逻辑服中
     * </pre>
     *
     * @param clazz clazz
     * @param <T>   t
     * @return 元附加信息
     */
    default <T extends UserAttachment> T getAttachment(final Class<T> clazz) {
        HeadMetadata headMetadata = this.getHeadMetadata();
        byte[] attachmentData = headMetadata.getAttachmentData();
        return DataCodecKit.decode(attachmentData, clazz);
    }

    /**
     * 得到元附加信息
     * <p>
     * example
     * <pre>{@code
     *     // 自定义 FlowContext
     *     public class MyFlowContext extends FlowContext {
     *         MyAttachment attachment;
     *
     *         @Override
     *         @SuppressWarnings("unchecked")
     *         public MyAttachment getAttachment() {
     *
     *             if (Objects.isNull(attachment)) {
     *                 this.attachment = this.getAttachment(MyAttachment.class);
     *             }
     *
     *             return this.attachment;
     *         }
     *     }
     *
     *     // 自定义元信息类
     *     public class MyAttachment implements Attachment {
     *         @Getter
     *         long userId;
     *     }
     * }
     * </pre>
     *
     * @param <T> t
     * @return 元附加信息
     */
    default <T extends UserAttachment> T getAttachment() {
        throw new RuntimeException("需要子类实现");
    }
}

/**
 * 帮助 FlowContext 得到通信的能力
 * <pre>
 *     使用此接口通信的目的，更多的是为了得到 FlowContext 中 request HeadMetadata 对象，
 *     并复用 HeadMetadata 部分信息，从而减少一些冗余代码。
 *
 *     在与其他逻辑服通信时，些接口提供了同步、异步、异步回调风格的方法
 *
 *     方法命名规则
 *     同步: invoke_xxx；返回 ResponseMessage 对象
 *     异步: invoke_xxx_Future； 返回 CompletableFuture 对象
 *     异步回调： invoke_xxx_Async； 方法参数提供了回调方法
 *
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-12-21
 * @see FlowContext
 */
interface SimpleCommunication extends SimpleExecutor
        , SimpleBarMessageCreator {

    /**
     * 游戏逻辑服
     * <pre>
     *     当前 FlowContext 所关联的游戏逻辑服
     *     BrokerClient
     * </pre>
     *
     * @return 游戏逻辑服
     */
    default BrokerClientContext getBrokerClientContext() {
        return this.option(FlowAttr.brokerClientContext);
    }

    /**
     * 框架网络通讯聚合接口
     *
     * @return 框架网络通讯聚合接口
     */
    private CommunicationAggregationContext aggregationContext() {
        return this.option(FlowAttr.aggregationContext);
    }

    /**
     * 广播通讯上下文
     * <pre>
     *     <a href="https://www.yuque.com/iohao/game/qv4qfo">文档</a>
     * </pre>
     *
     * @return BroadcastContext
     */
    default BroadcastContext getBroadcastContext() {
        return this.aggregationContext();
    }

    /**
     * 广播通讯上下文 - 严格顺序的
     * <pre>
     *     <a href="https://www.yuque.com/iohao/game/qv4qfo">文档</a>
     * </pre>
     *
     * @return BroadcastOrderContext
     */
    default BroadcastOrderContext getBroadcastOrderContext() {
        return this.aggregationContext();
    }

    /**
     * 游戏逻辑服与游戏逻辑服之间的通讯上下文
     * <pre>
     *     <a href="https://www.yuque.com/iohao/game/anguu6">文档</a>
     * </pre>
     *
     * @return InvokeModuleContext
     */
    default InvokeModuleContext getInvokeModuleContext() {
        return this.aggregationContext();
    }

    /**
     * 游戏逻辑服与游戏对外服的通讯上下文
     * <pre>
     *     <a href="https://www.yuque.com/iohao/game/ivxsw5">文档</a>
     * </pre>
     *
     * @return InvokeExternalModuleContext
     */
    default InvokeExternalModuleContext getInvokeExternalModuleContext() {
        return this.aggregationContext();
    }

    default <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
        return CompletableFuture.supplyAsync(supplier, this.getVirtualExecutor());
    }
}

/**
 * 帮助 FlowContext 得到与其他游戏逻辑服通信的能力（模块之间的访问，访问【同类型】的多个逻辑服）
 */
interface SimpleCommunicationInvokeModuleCollect extends SimpleCommunication {

    /**
     * 模块之间的访问，访问【同类型】的多个逻辑服
     * <pre>
     *     模块A 访问 模块B 的某个方法，因为只有模块B持有这些数据，这里的模块指的是游戏逻辑服。
     *     假设启动了多个模块B，分别是：模块B-1、模块B-2、模块B-3、模块B-4 等。
     *     框架支持访问【同类型】的多个逻辑服，并把多个相同逻辑服结果收集到一起。
     *
     *     <a href="https://www.yuque.com/iohao/game/rf9rb9">文档 - 请求同类型多个逻辑服通信结果</a>
     *
     *     同步调用
     * </pre>
     *
     * @param cmdInfo 路由
     * @return ResponseCollectMessage
     */
    default ResponseCollectMessage invokeModuleCollectMessage(CmdInfo cmdInfo) {
        return invokeModuleCollectMessage(cmdInfo, null);
    }

    /**
     * 模块之间的访问，访问【同类型】的多个逻辑服
     * <pre>
     *     模块A 访问 模块B 的某个方法，因为只有模块B持有这些数据，这里的模块指的是游戏逻辑服。
     *     假设启动了多个模块B，分别是：模块B-1、模块B-2、模块B-3、模块B-4 等。
     *     框架支持访问【同类型】的多个逻辑服，并把多个相同逻辑服结果收集到一起。
     *
     *     <a href="https://www.yuque.com/iohao/game/rf9rb9">文档 - 请求同类型多个逻辑服通信结果</a>
     *
     *     同步调用
     * </pre>
     *
     * @param cmdInfo 路由信息
     * @param data    业务数据
     * @return ResponseCollectMessage
     */
    default ResponseCollectMessage invokeModuleCollectMessage(CmdInfo cmdInfo, Object data) {
        var requestMessage = createRequestMessage(cmdInfo, data);
        return invokeModuleCollectMessage(requestMessage);
    }

    /**
     * 模块之间的访问，访问【同类型】的多个逻辑服
     * <pre>
     *     模块A 访问 模块B 的某个方法，因为只有模块B持有这些数据，这里的模块指的是游戏逻辑服。
     *     假设启动了多个模块B，分别是：模块B-1、模块B-2、模块B-3、模块B-4 等。
     *     框架支持访问【同类型】的多个逻辑服，并把多个相同逻辑服结果收集到一起。
     *
     *     <a href="https://www.yuque.com/iohao/game/rf9rb9">文档 - 请求同类型多个逻辑服通信结果</a>
     *
     *     同步调用
     * </pre>
     *
     * @param requestMessage requestMessage
     * @return ResponseCollectMessage
     */
    default ResponseCollectMessage invokeModuleCollectMessage(RequestMessage requestMessage) {
        var invokeModuleContext = this.getInvokeModuleContext();
        return invokeModuleContext.invokeModuleCollectMessage(requestMessage);
    }

    /**
     * 模块之间的访问，访问【同类型】的多个逻辑服
     * <pre>
     *     模块A 访问 模块B 的某个方法，因为只有模块B持有这些数据，这里的模块指的是游戏逻辑服。
     *     假设启动了多个模块B，分别是：模块B-1、模块B-2、模块B-3、模块B-4 等。
     *     框架支持访问【同类型】的多个逻辑服，并把多个相同逻辑服结果收集到一起。
     *
     *     <a href="https://www.yuque.com/iohao/game/rf9rb9">文档 - 请求同类型多个逻辑服通信结果</a>
     *
     *     异步调用
     * </pre>
     *
     * @param cmdInfo 路由
     * @return CompletableFuture ResponseCollectMessage
     */
    default CompletableFuture<ResponseCollectMessage> invokeModuleCollectMessageFuture(CmdInfo cmdInfo) {
        return this.invokeModuleCollectMessageFuture(cmdInfo, null);
    }

    /**
     * 模块之间的访问，访问【同类型】的多个逻辑服
     * <pre>
     *     模块A 访问 模块B 的某个方法，因为只有模块B持有这些数据，这里的模块指的是游戏逻辑服。
     *     假设启动了多个模块B，分别是：模块B-1、模块B-2、模块B-3、模块B-4 等。
     *     框架支持访问【同类型】的多个逻辑服，并把多个相同逻辑服结果收集到一起。
     *
     *     <a href="https://www.yuque.com/iohao/game/rf9rb9">文档 - 请求同类型多个逻辑服通信结果</a>
     *
     *     异步调用
     * </pre>
     *
     * @param cmdInfo 路由
     * @param data    业务数据
     * @return CompletableFuture ResponseCollectMessage
     */
    default CompletableFuture<ResponseCollectMessage> invokeModuleCollectMessageFuture(CmdInfo cmdInfo, Object data) {
        var requestMessage = createRequestMessage(cmdInfo, data);
        return this.invokeModuleCollectMessageFuture(requestMessage);
    }

    /**
     * 模块之间的访问，访问【同类型】的多个逻辑服
     * <pre>
     *     模块A 访问 模块B 的某个方法，因为只有模块B持有这些数据，这里的模块指的是游戏逻辑服。
     *     假设启动了多个模块B，分别是：模块B-1、模块B-2、模块B-3、模块B-4 等。
     *     框架支持访问【同类型】的多个逻辑服，并把多个相同逻辑服结果收集到一起。
     *
     *     <a href="https://www.yuque.com/iohao/game/rf9rb9">文档 - 请求同类型多个逻辑服通信结果</a>
     *
     *     异步调用
     * </pre>
     *
     * @param requestMessage requestMessage
     * @return CompletableFuture ResponseCollectMessage
     */
    default CompletableFuture<ResponseCollectMessage> invokeModuleCollectMessageFuture(RequestMessage requestMessage) {
        return this.supplyAsync(() -> this.invokeModuleCollectMessage(requestMessage));
    }

    /**
     * 模块之间的访问，访问【同类型】的多个逻辑服
     * <pre>
     *     模块A 访问 模块B 的某个方法，因为只有模块B持有这些数据，这里的模块指的是游戏逻辑服。
     *     假设启动了多个模块B，分别是：模块B-1、模块B-2、模块B-3、模块B-4 等。
     *     框架支持访问【同类型】的多个逻辑服，并把多个相同逻辑服结果收集到一起。
     *
     *     <a href="https://www.yuque.com/iohao/game/rf9rb9">文档 - 请求同类型多个逻辑服通信结果</a>
     *
     *     异步调用，回调编码风格，具备 traceId
     * </pre>
     *
     * @param cmdInfo  路由
     * @param callback 异步回调方法
     */
    default void invokeModuleCollectMessageAsync(CmdInfo cmdInfo, Consumer<ResponseCollectMessage> callback) {
        this.invokeModuleCollectMessageAsync(cmdInfo, null, callback);
    }

    /**
     * 模块之间的访问，访问【同类型】的多个逻辑服
     * <pre>
     *     模块A 访问 模块B 的某个方法，因为只有模块B持有这些数据，这里的模块指的是游戏逻辑服。
     *     假设启动了多个模块B，分别是：模块B-1、模块B-2、模块B-3、模块B-4 等。
     *     框架支持访问【同类型】的多个逻辑服，并把多个相同逻辑服结果收集到一起。
     *
     *     <a href="https://www.yuque.com/iohao/game/rf9rb9">文档 - 请求同类型多个逻辑服通信结果</a>
     *
     *     异步调用，回调编码风格，具备 traceId
     * </pre>
     *
     * @param cmdInfo  路由
     * @param data     业务数据
     * @param callback 异步回调方法
     */
    default void invokeModuleCollectMessageAsync(CmdInfo cmdInfo, Object data, Consumer<ResponseCollectMessage> callback) {
        var requestMessage = this.createRequestMessage(cmdInfo, data);
        this.invokeModuleCollectMessageAsync(requestMessage, callback);
    }

    /**
     * 模块之间的访问，访问【同类型】的多个逻辑服
     * <pre>
     *     模块A 访问 模块B 的某个方法，因为只有模块B持有这些数据，这里的模块指的是游戏逻辑服。
     *     假设启动了多个模块B，分别是：模块B-1、模块B-2、模块B-3、模块B-4 等。
     *     框架支持访问【同类型】的多个逻辑服，并把多个相同逻辑服结果收集到一起。
     *
     *     <a href="https://www.yuque.com/iohao/game/rf9rb9">文档 - 请求同类型多个逻辑服通信结果</a>
     *
     *     异步调用，回调编码风格，具备 traceId
     * </pre>
     *
     * @param requestMessage requestMessage
     * @param callback       异步回调方法
     */
    default void invokeModuleCollectMessageAsync(RequestMessage requestMessage, Consumer<ResponseCollectMessage> callback) {
        this.invokeModuleCollectMessageAsync(requestMessage, callback, this.getExecutor());
    }

    /**
     * 模块之间的访问，访问【同类型】的多个逻辑服
     * <pre>
     *     模块A 访问 模块B 的某个方法，因为只有模块B持有这些数据，这里的模块指的是游戏逻辑服。
     *     假设启动了多个模块B，分别是：模块B-1、模块B-2、模块B-3、模块B-4 等。
     *     框架支持访问【同类型】的多个逻辑服，并把多个相同逻辑服结果收集到一起。
     *
     *     <a href="https://www.yuque.com/iohao/game/rf9rb9">文档 - 请求同类型多个逻辑服通信结果</a>
     *
     *     异步调用，回调编码风格，具备 traceId
     * </pre>
     *
     * @param requestMessage   requestMessage
     * @param callback         异步回调方法
     * @param callbackExecutor 处理回调的 Executor
     */
    default void invokeModuleCollectMessageAsync(final RequestMessage requestMessage
            , final Consumer<ResponseCollectMessage> callback, final Executor callbackExecutor) {

        var headMetadata = requestMessage.getHeadMetadata();
        var traceId = headMetadata.getTraceId();

        if (Objects.isNull(traceId)) {
            this.invokeModuleCollectMessageFuture(requestMessage).thenAcceptAsync(callback, callbackExecutor);
            return;
        }

        this.invokeModuleCollectMessageFuture(requestMessage).thenAcceptAsync(responseCollectMessage -> {
            // 简单装饰
            InternalFlowContextKit.decorate(traceId, callback, responseCollectMessage);
        }, callbackExecutor);
    }
}

/**
 * 帮助 FlowContext 得到广播通信的能力
 */
interface SimpleCommunicationBroadcast extends SimpleCommunication {
    private void employTraceId(ResponseMessage responseMessage) {
        String traceId = this.getHeadMetadata().getTraceId();

        if (Objects.nonNull(traceId)) {
            HeadMetadata headMetadata = responseMessage.getHeadMetadata();
            headMetadata.setTraceId(traceId);
        }
    }

    private void extractedSourceClientId(ResponseMessage responseMessage, long userId) {
        var responseMessageHeadMetadata = responseMessage.getHeadMetadata();
        if (responseMessageHeadMetadata.getSourceClientId() != 0) {
            // 说明已经指定了需要精准广播的游戏对外服
            return;
        }

        // userId 等于自己，这里就精准广播到玩家所在的对外服中（即使启动了多个游戏对外服，也能精准到玩家所在的对外服中）
        var headMetadata = this.getHeadMetadata();
        if (userId != headMetadata.getUserId()) {
            return;
        }

        // 指定游戏对外服广播（当前玩家所在的游戏对外服）
        var sourceClientId = headMetadata.getSourceClientId();
        responseMessageHeadMetadata.setSourceClientId(sourceClientId);
    }

    /**
     * 给自己发送消息
     * <pre>
     *     路由则使用当前 action 的路由。
     * </pre>
     *
     * @param bizData 业务数据
     * @see HeadMetadata#getCmdInfo()
     */
    default void broadcastMe(Object bizData) {
        var cmdInfo = this.getCmdInfo();
        this.broadcastMe(cmdInfo, bizData);
    }

    /**
     * 给自己发送消息
     *
     * @param cmdInfo 发送到此路由
     * @param bizData 业务数据
     */
    default void broadcastMe(CmdInfo cmdInfo, Object bizData) {
        var responseMessage = this.createResponseMessage(cmdInfo, bizData);
        this.broadcastMe(responseMessage);
    }

    /**
     * 给自己发送消息
     *
     * @param responseMessage 消息
     */
    default void broadcastMe(ResponseMessage responseMessage) {
        var userId = this.getUserId();
        this.broadcast(responseMessage, userId);
    }

    /**
     * 全服广播
     *
     * @param cmdInfo 广播到此路由
     * @param bizData 业务数据
     */
    default void broadcast(CmdInfo cmdInfo, Object bizData) {
        var responseMessage = BarMessageKit.createResponseMessage(cmdInfo, bizData);
        this.broadcast(responseMessage);
    }

    /**
     * 全服广播
     *
     * @param responseMessage 消息
     */
    default void broadcast(ResponseMessage responseMessage) {
        employTraceId(responseMessage);

        BroadcastContext broadcastContext = this.getBroadcastContext();
        broadcastContext.broadcast(responseMessage);
    }

    /**
     * 广播消息给单个用户
     *
     * @param cmdInfo 广播到此路由
     * @param bizData 业务数据
     * @param userId  userId
     */
    default void broadcast(CmdInfo cmdInfo, Object bizData, long userId) {
        var responseMessage = BarMessageKit.createResponseMessage(cmdInfo, bizData);
        this.broadcast(responseMessage, userId);
    }

    /**
     * 广播消息给单个用户
     *
     * @param responseMessage 消息
     * @param userId          userId
     */
    default void broadcast(final ResponseMessage responseMessage, final long userId) {

        employTraceId(responseMessage);

        extractedSourceClientId(responseMessage, userId);

        BroadcastContext broadcastContext = this.getBroadcastContext();
        broadcastContext.broadcast(responseMessage, userId);
    }

    /**
     * 广播消息给指定用户列表
     *
     * @param cmdInfo    广播到此路由
     * @param bizData    业务数据
     * @param userIdList 指定用户列表
     */
    default void broadcast(CmdInfo cmdInfo, Object bizData, Collection<Long> userIdList) {
        var responseMessage = BarMessageKit.createResponseMessage(cmdInfo, bizData);
        this.broadcast(responseMessage, userIdList);
    }

    /**
     * 广播消息给指定用户列表
     *
     * @param responseMessage 消息
     * @param userIdList      指定用户列表 (如果为 null 或 empty 就不会触发)
     */
    default void broadcast(final ResponseMessage responseMessage, final Collection<Long> userIdList) {
        employTraceId(responseMessage);

        BroadcastContext broadcastContext = this.getBroadcastContext();
        broadcastContext.broadcast(responseMessage, userIdList);
    }

    /**
     * 顺序 - 给自己发送消息
     * <pre>
     *     路由则使用当前 action 的路由。
     * </pre>
     *
     * @param bizData 业务数据
     * @see HeadMetadata#getCmdInfo()
     */
    default void broadcastOrderMe(Object bizData) {
        var cmdInfo = this.getCmdInfo();
        this.broadcastOrderMe(cmdInfo, bizData);
    }

    /**
     * 顺序 - 给自己发送消息
     *
     * @param cmdInfo 发送到此路由
     * @param bizData 业务数据
     */
    default void broadcastOrderMe(CmdInfo cmdInfo, Object bizData) {
        var responseMessage = this.createResponseMessage(cmdInfo, bizData);
        this.broadcastOrderMe(responseMessage);
    }

    /**
     * 顺序 - 给自己发送消息
     *
     * @param responseMessage 消息
     */
    default void broadcastOrderMe(ResponseMessage responseMessage) {
        var userId = this.getUserId();
        this.broadcastOrder(responseMessage, userId);
    }

    /**
     * 顺序 - 全服广播
     *
     * @param cmdInfo 广播到此路由
     * @param bizData 业务数据
     */
    default void broadcastOrder(CmdInfo cmdInfo, Object bizData) {
        var responseMessage = BarMessageKit.createResponseMessage(cmdInfo, bizData);
        this.broadcastOrder(responseMessage);
    }

    /**
     * 顺序 - 全服广播
     *
     * @param responseMessage 消息
     */
    default void broadcastOrder(final ResponseMessage responseMessage) {
        employTraceId(responseMessage);

        BroadcastOrderContext broadcastOrderContext = this.getBroadcastOrderContext();
        broadcastOrderContext.broadcastOrder(responseMessage);
    }

    /**
     * 顺序 - 广播消息给指定用户列表
     *
     * @param cmdInfo    广播到此路由
     * @param bizData    业务数据
     * @param userIdList 指定用户列表
     */
    default void broadcastOrder(CmdInfo cmdInfo, Object bizData, Collection<Long> userIdList) {
        var responseMessage = BarMessageKit.createResponseMessage(cmdInfo, bizData);
        this.broadcastOrder(responseMessage, userIdList);
    }

    /**
     * 顺序 - 广播消息给指定用户列表
     *
     * @param responseMessage 消息
     * @param userIdList      指定用户列表 (如果为 null 或 empty 就不会触发)
     */
    default void broadcastOrder(ResponseMessage responseMessage, Collection<Long> userIdList) {
        employTraceId(responseMessage);

        BroadcastOrderContext broadcastOrderContext = this.getBroadcastOrderContext();
        broadcastOrderContext.broadcastOrder(responseMessage, userIdList);
    }

    /**
     * 顺序 - 广播消息给单个用户
     *
     * @param cmdInfo 广播到此路由
     * @param bizData 业务数据
     * @param userId  userId
     */
    default void broadcastOrder(CmdInfo cmdInfo, Object bizData, long userId) {
        var responseMessage = BarMessageKit.createResponseMessage(cmdInfo, bizData);
        this.broadcastOrder(responseMessage, userId);
    }

    /**
     * 顺序 - 广播消息给单个用户
     *
     * @param responseMessage 消息
     * @param userId          userId
     */
    default void broadcastOrder(final ResponseMessage responseMessage, final long userId) {
        employTraceId(responseMessage);

        extractedSourceClientId(responseMessage, userId);

        BroadcastOrderContext broadcastOrderContext = this.getBroadcastOrderContext();
        broadcastOrderContext.broadcastOrder(responseMessage, userId);
    }

}

/**
 * 帮助 FlowContext 得到与游戏对外服通信的能力
 */
interface SimpleCommunicationInvokeExternalModule extends SimpleCommunication {

    /**
     * 【游戏逻辑服】访问玩家所在的【游戏对外服】，通常是发起请求的游戏对外服
     * <pre>
     *     <a href="https://www.yuque.com/iohao/game/ivxsw5">文档 - 获取游戏对外服的数据与扩展</a>
     *
     *     同步调用
     * </pre>
     *
     * @param bizCode bizCode
     * @return ResponseCollectExternalMessage 一定不为 null
     */
    default ResponseCollectExternalMessage invokeExternalModuleCollectMessage(int bizCode) {
        return this.invokeExternalModuleCollectMessage(bizCode, null);
    }

    /**
     * 【游戏逻辑服】访问玩家所在的【游戏对外服】，通常是发起请求的游戏对外服
     * <pre>
     *     <a href="https://www.yuque.com/iohao/game/ivxsw5">文档 - 获取游戏对外服的数据与扩展</a>
     *
     *     同步调用
     * </pre>
     *
     * @param bizCode bizCode
     * @param data    业务数据
     * @return ResponseCollectExternalMessage 一定不为 null
     */
    default ResponseCollectExternalMessage invokeExternalModuleCollectMessage(int bizCode, Serializable data) {
        var request = createRequestCollectExternalMessage(bizCode, data);

        this.extractedSourceClientId(request);

        return this.invokeExternalModuleCollectMessage(request);
    }

    private void extractedSourceClientId(RequestCollectExternalMessage request) {
        // 强制指定需要访问的游戏对外服；当指定 id 后，将不会访问所有的游戏对外服
        var headMetadata = this.getHeadMetadata();
        request.setSourceClientId(headMetadata.getSourceClientId());
    }

    /**
     * 创建 RequestCollectExternalMessage，会为 {@link RequestCollectExternalMessage} 添加 userId、traceId 相关信息
     *
     * @param bizCode 业务码
     * @return RequestCollectExternalMessage
     */
    default RequestCollectExternalMessage createRequestCollectExternalMessage(int bizCode) {
        return this.createRequestCollectExternalMessage(bizCode, null);
    }

    /**
     * 创建 RequestCollectExternalMessage，会为 {@link RequestCollectExternalMessage} 添加 userId、traceId 相关信息
     *
     * @param bizCode 业务码
     * @param data    业务数据
     * @return RequestCollectExternalMessage
     */
    default RequestCollectExternalMessage createRequestCollectExternalMessage(int bizCode, Serializable data) {
        // 得到发起请求的游戏对外服 id
        var headMetadata = this.getHeadMetadata();

        return new RequestCollectExternalMessage()
                // 根据业务码，调用游戏对外服与业务码对应的业务实现类
                .setBizCode(bizCode)
                // 业务数据
                .setData(data)
                // userId、traceId
                .setUserId(headMetadata.getUserId())
                .setTraceId(headMetadata.getTraceId())
                ;
    }

    /**
     * 【游戏逻辑服】访问【游戏对外服】，会为 {@link RequestCollectExternalMessage} 添加 userId、traceId 相关信息，
     * 如果 request 没有指定 sourceClientId，将会访问所有的游戏对外服。
     * <pre>
     *     <a href="https://www.yuque.com/iohao/game/ivxsw5">文档 - 获取游戏对外服的数据与扩展</a>
     *
     *     同步调用
     * </pre>
     *
     * @param request request
     * @return ResponseCollectExternalMessage 一定不为 null
     */
    default ResponseCollectExternalMessage invokeExternalModuleCollectMessage(RequestCollectExternalMessage request) {
        // MDC
        if (Objects.isNull(request.getTraceId())) {
            var traceId = this.getHeadMetadata().getTraceId();
            request.setTraceId(traceId);
        }

        if (request.getUserId() == 0) {
            long userId = this.getUserId();
            request.setUserId(userId);
        }

        // 【游戏逻辑服】与【游戏对外服】通讯上下文
        var invokeExternalModuleContext = this.getInvokeExternalModuleContext();
        return invokeExternalModuleContext.invokeExternalModuleCollectMessage(request);
    }

    /**
     * 【游戏逻辑服】访问玩家所在的【游戏对外服】，通常是发起请求的游戏对外服
     * <pre>
     *     <a href="https://www.yuque.com/iohao/game/ivxsw5">文档 - 获取游戏对外服的数据与扩展</a>
     *
     *     异步调用
     * </pre>
     *
     * @param bizCode bizCode
     * @return ResponseCollectExternalMessage 一定不为 null
     */
    default CompletableFuture<ResponseCollectExternalMessage> invokeExternalModuleCollectMessageFuture(int bizCode) {
        return this.invokeExternalModuleCollectMessageFuture(bizCode, null);
    }

    /**
     * 【游戏逻辑服】访问玩家所在的【游戏对外服】，通常是发起请求的游戏对外服
     * <pre>
     *     <a href="https://www.yuque.com/iohao/game/ivxsw5">文档 - 获取游戏对外服的数据与扩展</a>
     *
     *     异步调用
     * </pre>
     *
     * @param bizCode bizCode
     * @param data    业务数据
     * @return ResponseCollectExternalMessage 一定不为 null
     */
    default CompletableFuture<ResponseCollectExternalMessage> invokeExternalModuleCollectMessageFuture(
            int bizCode, Serializable data) {

        RequestCollectExternalMessage request = this.createRequestCollectExternalMessage(bizCode, data);

        this.extractedSourceClientId(request);

        return this.invokeExternalModuleCollectMessageFuture(request);
    }

    /**
     * 【游戏逻辑服】访问【游戏对外服】，会为 {@link RequestCollectExternalMessage} 添加 userId、traceId 相关信息，
     * 如果 request 没有指定 sourceClientId，将会访问所有的游戏对外服。
     * <pre>
     *     <a href="https://www.yuque.com/iohao/game/ivxsw5">文档 - 获取游戏对外服的数据与扩展</a>
     *
     *     异步调用
     * </pre>
     *
     * @param request request
     * @return ResponseCollectExternalMessage 一定不为 null
     */
    default CompletableFuture<ResponseCollectExternalMessage> invokeExternalModuleCollectMessageFuture(
            RequestCollectExternalMessage request) {

        return this.supplyAsync(() -> this.invokeExternalModuleCollectMessage(request));
    }

    /**
     * 【游戏逻辑服】访问玩家所在的【游戏对外服】，通常是发起请求的游戏对外服
     * <pre>
     *     <a href="https://www.yuque.com/iohao/game/ivxsw5">文档 - 获取游戏对外服的数据与扩展</a>
     *
     *     异步调用，回调编码风格，具备 traceId
     * </pre>
     *
     * @param bizCode  bizCode
     * @param callback 异步回调方法
     */
    default void invokeExternalModuleCollectMessageAsync(int bizCode
            , Consumer<ResponseCollectExternalMessage> callback) {

        this.invokeExternalModuleCollectMessageAsync(bizCode, null, callback);
    }

    /**
     * 【游戏逻辑服】访问玩家所在的【游戏对外服】，通常是发起请求的游戏对外服
     * <pre>
     *     <a href="https://www.yuque.com/iohao/game/ivxsw5">文档 - 获取游戏对外服的数据与扩展</a>
     *
     *     异步调用，回调编码风格，具备 traceId
     * </pre>
     *
     * @param bizCode  bizCode
     * @param data     业务数据
     * @param callback 异步回调方法
     */
    default void invokeExternalModuleCollectMessageAsync(int bizCode
            , Serializable data, Consumer<ResponseCollectExternalMessage> callback) {

        RequestCollectExternalMessage request = this.createRequestCollectExternalMessage(bizCode, data);

        this.extractedSourceClientId(request);

        this.invokeExternalModuleCollectMessageAsync(request, callback);
    }

    /**
     * 【游戏逻辑服】访问玩家所在的【游戏对外服】，通常是发起请求的游戏对外服
     * <pre>
     *     <a href="https://www.yuque.com/iohao/game/ivxsw5">文档 - 获取游戏对外服的数据与扩展</a>
     *
     *     异步调用，回调编码风格，具备 traceId
     * </pre>
     *
     * @param request  request
     * @param callback 异步回调方法
     */
    default void invokeExternalModuleCollectMessageAsync(RequestCollectExternalMessage request
            , Consumer<ResponseCollectExternalMessage> callback) {

        this.invokeExternalModuleCollectMessageAsync(request, callback, this.getExecutor());
    }

    /**
     * 【游戏逻辑服】访问【游戏对外服】，如果 {@link RequestCollectExternalMessage} 没有指定 sourceClientId，将会访问所有的游戏对外服。
     * <pre>
     *     <a href="https://www.yuque.com/iohao/game/ivxsw5">文档 - 获取游戏对外服的数据与扩展</a>
     *
     *     异步调用，回调编码风格，具备 traceId
     * </pre>
     *
     * @param request          request
     * @param callback         异步回调方法
     * @param callbackExecutor 处理回调的 Executor
     */
    default void invokeExternalModuleCollectMessageAsync(final RequestCollectExternalMessage request
            , final Consumer<ResponseCollectExternalMessage> callback, final Executor callbackExecutor) {

        var traceId = request.getTraceId();

        if (Objects.isNull(traceId)) {
            this.invokeExternalModuleCollectMessageFuture(request).thenAcceptAsync(callback, callbackExecutor);
            return;
        }

        this.invokeExternalModuleCollectMessageFuture(request).thenAcceptAsync(responseCollectExternalMessage -> {
            // 简单装饰
            InternalFlowContextKit.decorate(traceId, callback, responseCollectExternalMessage);
        }, callbackExecutor);
    }
}

/**
 * 帮助 FlowContext 得到与其他游戏逻辑服通信的能力
 */
interface SimpleCommunicationInvokeModuleVoid extends SimpleCommunication {

    /**
     * 根据路由信息来请求其他子服务器（其他逻辑服）的方法，并且不需要返回值
     * <pre>
     *     异步无阻塞的方法，因为没有返回值；
     *     <a href="https://www.yuque.com/iohao/game/nelwuz#gtdrv">文档 - 游戏逻辑服与单个游戏逻辑服通信请求 - 无返回值（可跨进程）</a>
     *     <a href="https://www.yuque.com/iohao/game/anguu6#cZfdx">文档 - 单个逻辑服与单个逻辑服通信请求 - 无返回值（可跨进程）</a>
     * </pre>
     * example
     * <pre>{@code
     *     // 内部模块通讯上下文，内部模块指的是游戏逻辑服
     *     InvokeModuleContext invokeModuleContext = ...
     *     // 请求房间逻辑服来创建房间，并且不需要返回值
     *     // 路由、业务参数
     *     invokeModuleContext.invokeModuleVoidMessage(cmdInfo);
     * }
     * </pre>
     *
     * @param cmdInfo 路由
     */
    default void invokeModuleVoidMessage(CmdInfo cmdInfo) {
        this.invokeModuleVoidMessage(cmdInfo, null);
    }

    /**
     * 根据路由信息来请求其他子服务器（其他逻辑服）的方法，并且不需要返回值
     * <pre>
     *     异步无阻塞的方法，因为没有返回值；
     *     <a href="https://www.yuque.com/iohao/game/nelwuz#gtdrv">文档 - 游戏逻辑服与单个游戏逻辑服通信请求 - 无返回值（可跨进程）</a>
     *     <a href="https://www.yuque.com/iohao/game/anguu6#cZfdx">文档 - 单个逻辑服与单个逻辑服通信请求 - 无返回值（可跨进程）</a>
     * </pre>
     * example
     * <pre>{@code
     *     // 内部模块通讯上下文，内部模块指的是游戏逻辑服
     *     InvokeModuleContext invokeModuleContext = ...
     *     // 请求房间逻辑服来创建房间，并且不需要返回值
     *     // 路由、业务参数
     *     invokeModuleContext.invokeModuleVoidMessage(cmdInfo, data);
     * }
     * </pre>
     *
     * @param cmdInfo 路由
     * @param data    业务数据
     */
    default void invokeModuleVoidMessage(CmdInfo cmdInfo, Object data) {
        var requestMessage = this.createRequestMessage(cmdInfo, data);
        this.invokeModuleVoidMessage(requestMessage);
    }

    /**
     * 根据路由信息来请求其他子服务器（其他逻辑服）的方法，并且不需要返回值
     * <pre>
     *     异步无阻塞的方法，因为没有返回值；
     *     <a href="https://www.yuque.com/iohao/game/nelwuz#gtdrv">文档 - 游戏逻辑服与单个游戏逻辑服通信请求 - 无返回值（可跨进程）</a>
     *     <a href="https://www.yuque.com/iohao/game/anguu6#cZfdx">文档 - 单个逻辑服与单个逻辑服通信请求 - 无返回值（可跨进程）</a>
     * </pre>
     * example
     * <pre>{@code
     *     // 内部模块通讯上下文，内部模块指的是游戏逻辑服
     *     InvokeModuleContext invokeModuleContext = ...
     *     // 请求房间逻辑服来创建房间，并且不需要返回值
     *     // 路由、业务参数
     *     invokeModuleContext.invokeModuleVoidMessage(requestMessage);
     * }
     * </pre>
     *
     * @param requestMessage requestMessage
     */
    default void invokeModuleVoidMessage(RequestMessage requestMessage) {
        var invokeModuleContext = this.getInvokeModuleContext();
        invokeModuleContext.invokeModuleVoidMessage(requestMessage);
    }
}

/**
 * 帮助 FlowContext 得到与其他游戏逻辑服通信的能力
 */
interface SimpleCommunicationInvokeModule extends SimpleCommunication {

    /**
     * 根据路由信息来请求其他子服务器（其他游戏逻辑服）的数据
     * <pre>
     *     <a href="https://www.yuque.com/iohao/game/anguu6">文档 - 游戏逻辑服之间的交互</a>
     *
     *     同步调用
     * </pre>
     *
     * @param cmdInfo 路由
     * @return ResponseMessage
     */
    default ResponseMessage invokeModuleMessage(CmdInfo cmdInfo) {
        return this.invokeModuleMessage(cmdInfo, null);
    }

    /**
     * 根据路由信息来请求其他子服务器（其他游戏逻辑服）的数据
     * <pre>
     *     <a href="https://www.yuque.com/iohao/game/anguu6">文档 - 游戏逻辑服之间的交互</a>
     *
     *     同步调用
     * </pre>
     *
     * @param cmdInfo cmdInfo
     * @param data    请求参数
     * @return ResponseMessage
     */
    default ResponseMessage invokeModuleMessage(CmdInfo cmdInfo, Object data) {
        var requestMessage = this.createRequestMessage(cmdInfo, data);
        return invokeModuleMessage(requestMessage);
    }

    /**
     * 根据路由信息来请求其他子服务器（其他游戏逻辑服）的数据
     * <pre>
     *     <a href="https://www.yuque.com/iohao/game/anguu6">文档 - 游戏逻辑服之间的交互</a>
     *
     *     同步调用
     * </pre>
     *
     * @param requestMessage requestMessage
     * @return ResponseMessage
     */
    default ResponseMessage invokeModuleMessage(RequestMessage requestMessage) {
        var invokeModuleContext = this.getInvokeModuleContext();
        return invokeModuleContext.invokeModuleMessage(requestMessage);
    }

    /**
     * 根据路由信息来请求其他子服务器（其他游戏逻辑服）的数据
     * <pre>
     *     <a href="https://www.yuque.com/iohao/game/anguu6">文档 - 游戏逻辑服之间的交互</a>
     *
     *     异步调用
     * </pre>
     *
     * @param cmdInfo 路由
     * @return CompletableFuture ResponseMessage
     */
    default CompletableFuture<ResponseMessage> invokeModuleMessageFuture(CmdInfo cmdInfo) {
        return invokeModuleMessageFuture(cmdInfo, null);
    }

    /**
     * 根据路由信息来请求其他子服务器（其他游戏逻辑服）的数据
     * <pre>
     *     <a href="https://www.yuque.com/iohao/game/anguu6">文档 - 游戏逻辑服之间的交互</a>
     *
     *     异步调用
     * </pre>
     *
     * @param cmdInfo 路由
     * @param data    业务数据
     * @return CompletableFuture ResponseMessage
     */
    default CompletableFuture<ResponseMessage> invokeModuleMessageFuture(CmdInfo cmdInfo, Object data) {
        var requestMessage = this.createRequestMessage(cmdInfo, data);
        return invokeModuleMessageFuture(requestMessage);
    }

    /**
     * 根据路由信息来请求其他子服务器（其他游戏逻辑服）的数据
     * <pre>
     *     <a href="https://www.yuque.com/iohao/game/anguu6">文档 - 游戏逻辑服之间的交互</a>
     *
     *     异步调用
     * </pre>
     *
     * @param requestMessage requestMessage
     * @return CompletableFuture ResponseMessage
     */
    default CompletableFuture<ResponseMessage> invokeModuleMessageFuture(RequestMessage requestMessage) {
        return this.supplyAsync(() -> this.invokeModuleMessage(requestMessage));
    }

    /**
     * 根据路由信息来请求其他子服务器（其他游戏逻辑服）的数据
     * <pre>
     *     <a href="https://www.yuque.com/iohao/game/anguu6">文档 - 游戏逻辑服之间的交互</a>
     *
     *     异步调用，回调编码风格，具备 traceId
     * </pre>
     *
     * @param cmdInfo  路由
     * @param callback 异步回调方法
     */
    default void invokeModuleMessageAsync(CmdInfo cmdInfo, Consumer<ResponseMessage> callback) {
        this.invokeModuleMessageAsync(cmdInfo, null, callback);
    }

    /**
     * 根据路由信息来请求其他子服务器（其他游戏逻辑服）的数据
     * <pre>
     *     <a href="https://www.yuque.com/iohao/game/anguu6">文档 - 游戏逻辑服之间的交互</a>
     *
     *     异步调用，回调编码风格，具备 traceId
     * </pre>
     *
     * @param cmdInfo  路由
     * @param data     业务数据
     * @param callback 异步回调方法
     */
    default void invokeModuleMessageAsync(CmdInfo cmdInfo, Object data, Consumer<ResponseMessage> callback) {
        var requestMessage = this.createRequestMessage(cmdInfo, data);
        this.invokeModuleMessageAsync(requestMessage, callback);
    }

    /**
     * 根据路由信息来请求其他子服务器（其他游戏逻辑服）的数据
     * <pre>
     *     <a href="https://www.yuque.com/iohao/game/anguu6">文档 - 游戏逻辑服之间的交互</a>
     *
     *     异步调用，回调编码风格，具备 traceId
     * </pre>
     *
     * @param requestMessage requestMessage
     * @param callback       异步回调方法
     */
    default void invokeModuleMessageAsync(RequestMessage requestMessage, Consumer<ResponseMessage> callback) {
        this.invokeModuleMessageAsync(requestMessage, callback, this.getExecutor());
    }

    /**
     * 根据路由信息来请求其他子服务器（其他游戏逻辑服）的数据
     * <pre>
     *     <a href="https://www.yuque.com/iohao/game/anguu6">文档 - 游戏逻辑服之间的交互</a>
     *
     *     异步调用，回调编码风格，具备 traceId
     * </pre>
     *
     * @param requestMessage   requestMessage
     * @param callback         异步回调方法
     * @param callbackExecutor 处理回调的 Executor
     */
    default void invokeModuleMessageAsync(final RequestMessage requestMessage
            , final Consumer<ResponseMessage> callback, final Executor callbackExecutor) {

        var headMetadata = requestMessage.getHeadMetadata();
        var traceId = headMetadata.getTraceId();

        if (Objects.isNull(traceId)) {
            this.invokeModuleMessageFuture(requestMessage).thenAcceptAsync(callback, callbackExecutor);
            return;
        }

        this.invokeModuleMessageFuture(requestMessage).thenAcceptAsync(responseMessage -> {
            // 简单装饰
            InternalFlowContextKit.decorate(traceId, callback, responseMessage);
        }, callbackExecutor);
    }
}

/**
 * 帮助 FlowContext 得到通信的能力（事件发布）
 */
interface SimpleCommunicationEventBus extends SimpleCommunication {
    /**
     * EventBus 是逻辑服事件总线。 EventBus、业务框架、逻辑服三者是 1:1:1 的关系。
     *
     * @return EventBus
     */
    default EventBus getEventBus() {
        return this.getBarSkeleton().option(SkeletonAttr.eventBus);
    }

    /**
     * [异步] 发送事件给所有订阅者
     * <pre>
     *     1 给当前进程所有逻辑服的订阅者发送事件消息
     *     2 给其他进程的订阅者发送事件消息
     * </pre>
     *
     * @param eventSource 事件源
     */
    default void fire(Object eventSource) {
        var eventBusMessage = this.createEventBusMessage(eventSource);

        EventBus eventBus = this.getEventBus();
        eventBus.fire(eventBusMessage);
    }

    /**
     * [同步] 发送事件给所有订阅者
     * <pre>
     *     1 [同步] 给当前进程所有逻辑服的订阅者发送事件消息
     *     2 [异步] 给其他进程的订阅者发送事件消息
     *
     *     注意，这里的同步仅指当前进程订阅者的同步，对其他进程中的订阅者无效（处理远程订阅者使用的是异步）。
     * </pre>
     *
     * @param eventSource 事件源
     */
    default void fireSync(Object eventSource) {
        var eventBusMessage = this.createEventBusMessage(eventSource);

        EventBus eventBus = this.getEventBus();
        eventBus.fireSync(eventBusMessage);
    }

    /**
     * [异步] 给当前进程的订阅者和远程进程的订阅者送事件消息，如果同类型逻辑服存在多个，只会给其中一个实例发送。
     * <pre>
     *     1 给当前进程所有逻辑服的订阅者发送事件消息
     *     2 给其他进程的订阅者发送事件消息
     * </pre>
     * 使用场景
     * <pre>
     *     假设现在有一个发放奖励的邮件逻辑服，我们启动了两个（或者说多个）邮件逻辑服实例来处理业务。
     *     当我们使用 fireAny 方法发送事件时，只会给其中一个实例发送事件。
     * </pre>
     *
     * @param eventSource 事件源
     */
    default void fireAny(Object eventSource) {
        var eventBusMessage = this.createEventBusMessage(eventSource);

        EventBus eventBus = this.getEventBus();
        eventBus.fireAny(eventBusMessage);
    }

    /**
     * [同步] 给当前进程的订阅者和远程进程的订阅者送事件消息，如果同类型逻辑服存在多个，只会给其中一个实例发送。
     * <p>
     * 这里的同类型指的是相同类型的逻辑服，也就是拥有相同 tag 的逻辑服。
     * <pre>
     *     1 [同步] 给当前进程所有逻辑服的订阅者发送事件消息
     *     2 [异步] 给其他进程的订阅者发送事件消息
     *
     *     注意，这里的同步仅指当前进程订阅者的同步，对其他进程中的订阅者无效（处理远程订阅者使用的是异步）。
     * </pre>
     * 使用场景
     * <pre>
     *     假设现在有一个发放奖励的邮件逻辑服，我们启动了两个（或者说多个）邮件逻辑服实例来处理业务。
     *     当我们使用 fireAny 方法发送事件时，只会给其中一个实例发送事件。
     * </pre>
     *
     * @param eventSource 事件源
     */
    default void fireAnySync(Object eventSource) {
        var eventBusMessage = this.createEventBusMessage(eventSource);

        EventBus eventBus = this.getEventBus();
        eventBus.fireAnySync(eventBusMessage);
    }

    /**
     * [异步] 给当前进程所有逻辑服的订阅者发送事件消息
     *
     * @param eventSource 事件源
     */
    default void fireLocal(Object eventSource) {
        var eventBusMessage = this.createEventBusMessage(eventSource);

        EventBus eventBus = this.getEventBus();
        eventBus.fireLocal(eventBusMessage);
    }

    /**
     * [同步] 给当前进程所有逻辑服的订阅者发送事件消息
     *
     * @param eventSource 事件源
     */
    default void fireLocalSync(Object eventSource) {
        var eventBusMessage = this.createEventBusMessage(eventSource);

        EventBus eventBus = this.getEventBus();
        eventBus.fireLocalSync(eventBusMessage);
    }

    /**
     * [异步] 仅给当前 EventBus 的订阅者发送事件消息
     *
     * @param eventSource 事件源
     */
    default void fireMe(Object eventSource) {
        var eventBusMessage = this.createEventBusMessage(eventSource);

        EventBus eventBus = this.getEventBus();
        eventBus.fireMe(eventBusMessage);
    }

    /**
     * [同步] 仅给当前 EventBus 的订阅者发送事件消息
     *
     * @param eventSource 事件源
     */
    default void fireMeSync(Object eventSource) {
        var eventBusMessage = this.createEventBusMessage(eventSource);

        EventBus eventBus = this.getEventBus();
        eventBus.fireMeSync(eventBusMessage);
    }

    /**
     * 创建事件消息
     *
     * @param eventSource 事件源
     * @return 事件消息
     */
    default EventBusMessage createEventBusMessage(Object eventSource) {
        var headMetadata = this.getHeadMetadata();
        var userId = headMetadata.getUserId();
        var traceId = headMetadata.getTraceId();

        var eventBusMessage = new EventBusMessage();
        eventBusMessage.setEventSource(eventSource);
        eventBusMessage.setThreadIndex(userId);
        eventBusMessage.setTraceId(traceId);

        return eventBusMessage;
    }
}

/**
 * 帮助 FlowContext 得到线程执行器的能力
 */
interface SimpleExecutor extends SimpleCommon {
    default ExecutorRegion getExecutorRegion() {
        return this.getBarSkeleton().getExecutorRegion();
    }

    /**
     * 玩家对应的虚拟线程执行器
     *
     * @return 虚拟线程执行器
     */
    default Executor getVirtualExecutor() {
        // 得到用户对应的虚拟线程执行器
        var headMetadata = this.getHeadMetadata();
        var executorIndex = ExecutorSelectKit.getExecutorIndex(headMetadata);

        ExecutorRegion executorRegion = this.getExecutorRegion();
        ThreadExecutor threadExecutor = executorRegion.getUserVirtualThreadExecutor(executorIndex);
        return threadExecutor.executor();
    }

    /**
     * 玩家对应的用户线程执行器
     * <pre>
     *     该执行器也是消费 action 的执行器
     * </pre>
     *
     * @return 用户线程执行器
     */
    default Executor getExecutor() {
        // 得到用户对应的用户线程执行器
        var headMetadata = this.getHeadMetadata();
        var executorIndex = ExecutorSelectKit.getExecutorIndex(headMetadata);

        ExecutorRegion executorRegion = this.getExecutorRegion();
        ThreadExecutor threadExecutor = executorRegion.getUserThreadExecutor(executorIndex);
        return threadExecutor.executor();
    }

    /**
     * 使用用户线程执行任务
     * <pre>
     *     该方法具备全链路调用日志跟踪
     * </pre>
     *
     * @param command 任务
     */
    default void execute(Runnable command) {
        var headMetadata = this.getHeadMetadata();
        var traceId = headMetadata.getTraceId();

        if (Objects.isNull(traceId)) {
            this.getExecutor().execute(command);
            return;
        }

        this.getExecutor().execute(InternalFlowContextKit.decorator(traceId, command));
    }

    /**
     * 使用虚拟线程执行任务
     * <pre>
     *     该方法具备全链路调用日志跟踪
     * </pre>
     *
     * @param command 任务
     */
    default void executeVirtual(Runnable command) {
        var headMetadata = this.getHeadMetadata();
        var traceId = headMetadata.getTraceId();

        if (Objects.isNull(traceId)) {
            this.getVirtualExecutor().execute(command);
            return;
        }

        this.getVirtualExecutor().execute(InternalFlowContextKit.decorator(traceId, command));
    }
}

/**
 * 帮助 FlowContext 得到创建 barMessage 消息的能力
 */
interface SimpleBarMessageCreator extends SimpleCommon {
    default RequestMessage createRequestMessage(CmdInfo cmdInfo) {
        return createRequestMessage(cmdInfo, null);
    }

    /**
     * 创建一个 request 对象，并使用当前 FlowContext HeadMetadata 部分属性。
     * <pre>
     *     HeadMetadata 对象以下属性不会赋值，如有需要，请自行赋值
     *       sourceClientId
     *       endPointClientId
     *       rpcCommandType
     *       msgId
     * </pre>
     *
     * @param cmdInfo 路由
     * @param data    业务参数
     * @return request
     */
    default RequestMessage createRequestMessage(final CmdInfo cmdInfo, final Object data) {

        var headMetadata = this.getHeadMetadata()
                .cloneHeadMetadata()
                .setCmdInfo(cmdInfo);

        var requestMessage = new RequestMessage();
        requestMessage.setHeadMetadata(headMetadata);

        if (Objects.nonNull(data)) {
            requestMessage.setData(data);
        }

        return requestMessage;
    }

    /**
     * 创建响应对象，通常用于广播
     * <pre>
     *     响应对象中的 HeadMetadata 对象，会复用当前用户的一些信息；
     * </pre>
     *
     * @param cmdInfo 路由
     * @param data    业务数据
     * @return 响应对象
     */
    default ResponseMessage createResponseMessage(CmdInfo cmdInfo, Object data) {
        Objects.requireNonNull(data);

        /*
         * 创建一个 HeadMetadata，并使用原有的一些信息；
         * 在广播时，只会给 HeadMetadata 中指定的游戏对外服广播。
         */
        var headMetadata = this.getHeadMetadata();

        var headMetadataClone = headMetadata
                .cloneHeadMetadata()
                .setCmdInfo(cmdInfo)
                .setEndPointClientId(headMetadata.getEndPointClientId())
                .setSourceClientId(headMetadata.getSourceClientId());

        // 创建一个响应对象
        var responseMessage = new ResponseMessage();
        responseMessage.setHeadMetadata(headMetadataClone);
        responseMessage.setData(data);

        return responseMessage;
    }
}

interface SimpleCommon extends FlowOptionDynamic {

    /**
     * FlowContext request HeadMetadata
     *
     * @return HeadMetadata
     */
    HeadMetadata getHeadMetadata();

    /**
     * 业务框架
     *
     * @return 所关联的业务框架
     */
    BarSkeleton getBarSkeleton();

    /**
     * 当前请求的路由
     *
     * @return 路由
     */
    default CmdInfo getCmdInfo() {
        return this.getHeadMetadata().getCmdInfo();
    }

    /**
     * userId
     *
     * @return userId
     */
    default long getUserId() {
        return this.getHeadMetadata().getUserId();
    }
}

@UtilityClass
class InternalFlowContextKit {
    <T> void decorate(String traceId, Consumer<T> callback, T response) {
        try {
            MDC.put(TraceKit.traceName, traceId);
            callback.accept(response);
        } finally {
            MDC.clear();
        }
    }

    Runnable decorator(String traceId, Runnable command) {
        // 装饰者
        return () -> {
            try {
                MDC.put(TraceKit.traceName, traceId);
                command.run();
            } finally {
                MDC.clear();
            }
        };
    }
}