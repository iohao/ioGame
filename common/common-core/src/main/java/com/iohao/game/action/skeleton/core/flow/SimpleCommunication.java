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
package com.iohao.game.action.skeleton.core.flow;

import com.iohao.game.action.skeleton.core.BarMessageKit;
import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.core.commumication.*;
import com.iohao.game.action.skeleton.core.flow.attr.FlowAttr;
import com.iohao.game.action.skeleton.core.flow.attr.FlowOptionDynamic;
import com.iohao.game.action.skeleton.eventbus.EventBus;
import com.iohao.game.action.skeleton.eventbus.EventBusMessage;
import com.iohao.game.action.skeleton.eventbus.EventBusSubscriber;
import com.iohao.game.action.skeleton.kit.ExecutorSelectKit;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.action.skeleton.protocol.collect.ResponseCollectMessage;
import com.iohao.game.action.skeleton.protocol.external.RequestCollectExternalMessage;
import com.iohao.game.action.skeleton.protocol.external.ResponseCollectExternalMessage;
import com.iohao.game.common.kit.TraceKit;
import com.iohao.game.common.kit.concurrent.executor.ThreadExecutor;
import com.iohao.game.common.kit.concurrent.executor.UserVirtualExecutorRegion;
import org.slf4j.MDC;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
interface SimpleCommunication extends FlowOptionDynamic {
    /**
     * 创建请求对象
     *
     * @param cmdInfo 路由
     * @param data    业务数据
     * @return 请求对象
     */
    RequestMessage createRequestMessage(final CmdInfo cmdInfo, final Object data);

    /**
     * FlowContext request HeadMetadata
     *
     * @return HeadMetadata
     */
    HeadMetadata getHeadMetadata();

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
    default CommunicationAggregationContext aggregationContext() {
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
    default ResponseMessage invokeModuleMessage(final CmdInfo cmdInfo) {
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
    default ResponseMessage invokeModuleMessage(final CmdInfo cmdInfo, final Object data) {
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
    default ResponseMessage invokeModuleMessage(final RequestMessage requestMessage) {
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
    default CompletableFuture<ResponseMessage> invokeModuleMessageFuture(final CmdInfo cmdInfo) {
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
    default CompletableFuture<ResponseMessage> invokeModuleMessageFuture(final CmdInfo cmdInfo, final Object data) {
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
    default CompletableFuture<ResponseMessage> invokeModuleMessageFuture(final RequestMessage requestMessage) {
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
    default void invokeModuleMessageAsync(final CmdInfo cmdInfo, final Consumer<ResponseMessage> callback) {
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
    default void invokeModuleMessageAsync(final CmdInfo cmdInfo, final Object data, final Consumer<ResponseMessage> callback) {
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
    default void invokeModuleMessageAsync(final RequestMessage requestMessage, final Consumer<ResponseMessage> callback) {
        final HeadMetadata headMetadata = requestMessage.getHeadMetadata();
        var traceId = headMetadata.getTraceId();

        var virtualExecutor = this.getVirtualExecutor();

        if (Objects.isNull(traceId)) {
            this.invokeModuleMessageFuture(requestMessage).thenAcceptAsync(callback, virtualExecutor);
            return;
        }

        this.invokeModuleMessageFuture(requestMessage).thenAcceptAsync(responseMessage -> {
            try {
                MDC.put(TraceKit.traceName, traceId);
                callback.accept(responseMessage);
            } finally {
                MDC.clear();
            }
        }, virtualExecutor);
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
     * @param cmdInfo 路由
     * @return ResponseCollectMessage
     */
    default ResponseCollectMessage invokeModuleCollectMessage(final CmdInfo cmdInfo) {
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
    default ResponseCollectMessage invokeModuleCollectMessage(final CmdInfo cmdInfo, final Object data) {
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
    default ResponseCollectMessage invokeModuleCollectMessage(final RequestMessage requestMessage) {
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
    default CompletableFuture<ResponseCollectMessage> invokeModuleCollectMessageFuture(final CmdInfo cmdInfo) {
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
    default CompletableFuture<ResponseCollectMessage> invokeModuleCollectMessageFuture(final CmdInfo cmdInfo, final Object data) {
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
    default CompletableFuture<ResponseCollectMessage> invokeModuleCollectMessageFuture(final RequestMessage requestMessage) {
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
    default void invokeModuleCollectMessageAsync(final CmdInfo cmdInfo, final Consumer<ResponseCollectMessage> callback) {
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
    default void invokeModuleCollectMessageAsync(final CmdInfo cmdInfo, final Object data, final Consumer<ResponseCollectMessage> callback) {
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
    default void invokeModuleCollectMessageAsync(final RequestMessage requestMessage, final Consumer<ResponseCollectMessage> callback) {
        final HeadMetadata headMetadata = requestMessage.getHeadMetadata();
        var traceId = headMetadata.getTraceId();

        var virtualExecutor = this.getVirtualExecutor();

        if (Objects.isNull(traceId)) {
            this.invokeModuleCollectMessageFuture(requestMessage).thenAcceptAsync(callback, virtualExecutor);
            return;
        }

        this.invokeModuleCollectMessageFuture(requestMessage).thenAcceptAsync(responseCollectMessage -> {
            try {
                MDC.put(TraceKit.traceName, traceId);
                callback.accept(responseCollectMessage);
            } finally {
                MDC.clear();
            }
        }, virtualExecutor);
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
     *     invokeModuleContext.invokeModuleVoidMessage(cmdInfo);
     * }
     * </pre>
     *
     * @param cmdInfo 路由
     */
    default void invokeModuleVoidMessage(final CmdInfo cmdInfo) {
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
    default void invokeModuleVoidMessage(final CmdInfo cmdInfo, final Object data) {
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
    default void invokeModuleVoidMessage(final RequestMessage requestMessage) {
        var invokeModuleContext = this.getInvokeModuleContext();
        invokeModuleContext.invokeModuleVoidMessage(requestMessage);
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
     * @return ResponseCollectExternalMessage 一定不为 null
     */
    default ResponseCollectExternalMessage invokeExternalModuleCollectMessage(final int bizCode) {
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
    default ResponseCollectExternalMessage invokeExternalModuleCollectMessage(final int bizCode, final Serializable data) {
        RequestCollectExternalMessage request = createRequestCollectExternalMessage(bizCode, data);
        return this.invokeExternalModuleCollectMessage(request);
    }

    private RequestCollectExternalMessage createRequestCollectExternalMessage(final int bizCode, final Serializable data) {
        // 得到发起请求的游戏对外服 id
        HeadMetadata headMetadata = this.getHeadMetadata();
        var sourceClientId = headMetadata.getSourceClientId();

        return new RequestCollectExternalMessage()
                // 强制指定需要访问的游戏对外服；当指定 id 后，将不会访问所有的游戏对外服
                .setSourceClientId(sourceClientId)
                .setBizCode(bizCode)
                .setData(data);
    }

    /**
     * 【游戏逻辑服】访问玩家所在的【游戏对外服】，通常是发起请求的游戏对外服。
     * <p>
     * 如果需要访问多个游戏对外服
     * <pre>
     *     <a href="https://www.yuque.com/iohao/game/ivxsw5">文档 - 获取游戏对外服的数据与扩展</a>
     *
     *     同步调用
     * </pre>
     *
     * @param request request
     * @return ResponseCollectExternalMessage 一定不为 null
     */
    default ResponseCollectExternalMessage invokeExternalModuleCollectMessage(final RequestCollectExternalMessage request) {
        // MDC
        var traceId = this.getHeadMetadata().getTraceId();
        request.setTraceId(traceId);

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
    default CompletableFuture<ResponseCollectExternalMessage> invokeExternalModuleCollectMessageFuture(final int bizCode) {
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
    default CompletableFuture<ResponseCollectExternalMessage> invokeExternalModuleCollectMessageFuture(final int bizCode, final Serializable data) {
        var requestCollectExternalMessage = this.createRequestCollectExternalMessage(bizCode, data);
        return this.invokeExternalModuleCollectMessageFuture(requestCollectExternalMessage);
    }

    /**
     * 【游戏逻辑服】访问玩家所在的【游戏对外服】，通常是发起请求的游戏对外服
     * <pre>
     *     <a href="https://www.yuque.com/iohao/game/ivxsw5">文档 - 获取游戏对外服的数据与扩展</a>
     *
     *     异步调用
     * </pre>
     *
     * @param request request
     * @return ResponseCollectExternalMessage 一定不为 null
     */
    default CompletableFuture<ResponseCollectExternalMessage> invokeExternalModuleCollectMessageFuture(final RequestCollectExternalMessage request) {
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
    default void invokeExternalModuleCollectMessageAsync(final int bizCode, final Consumer<ResponseCollectExternalMessage> callback) {
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
    default void invokeExternalModuleCollectMessageAsync(final int bizCode, final Serializable data, final Consumer<ResponseCollectExternalMessage> callback) {
        var requestCollectExternalMessage = this.createRequestCollectExternalMessage(bizCode, data);
        this.invokeExternalModuleCollectMessageAsync(requestCollectExternalMessage, callback);
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
    default void invokeExternalModuleCollectMessageAsync(final RequestCollectExternalMessage request, final Consumer<ResponseCollectExternalMessage> callback) {
        var traceId = request.getTraceId();

        var virtualExecutor = this.getVirtualExecutor();

        if (Objects.isNull(traceId)) {
            this.invokeExternalModuleCollectMessageFuture(request).thenAcceptAsync(callback, virtualExecutor);
            return;
        }

        this.invokeExternalModuleCollectMessageFuture(request).thenAcceptAsync(responseCollectExternalMessage -> {
            try {
                MDC.put(TraceKit.traceName, traceId);
                callback.accept(responseCollectExternalMessage);
            } finally {
                MDC.clear();
            }
        }, virtualExecutor);
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
        final HeadMetadata headMetadata = this.getHeadMetadata();
        CmdInfo cmdInfo = headMetadata.getCmdInfo();

        this.broadcastMe(cmdInfo, bizData);
    }

    /**
     * 给自己发送消息
     *
     * @param cmdInfo 发送到此路由
     * @param bizData 业务数据
     */
    default void broadcastMe(CmdInfo cmdInfo, Object bizData) {
        var userId = this.userId();
        this.broadcast(cmdInfo, bizData, userId);
    }

    /**
     * 给自己发送消息
     *
     * @param responseMessage 消息
     */
    default void broadcastMe(ResponseMessage responseMessage) {
        var userId = this.userId();
        this.broadcast(responseMessage, userId);
    }

    /**
     * 全服广播
     *
     * @param cmdInfo 广播到此路由
     * @param bizData 业务数据
     */
    default void broadcast(CmdInfo cmdInfo, Object bizData) {
        ResponseMessage responseMessage = this.createResponseMessage(cmdInfo, bizData);
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
        ResponseMessage responseMessage = this.createResponseMessage(cmdInfo, bizData);
        this.broadcast(responseMessage, userId);
    }

    /**
     * 广播消息给单个用户
     *
     * @param responseMessage 消息
     * @param userId          userId
     */
    default void broadcast(ResponseMessage responseMessage, long userId) {
        employTraceId(responseMessage);

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
        ResponseMessage responseMessage = this.createResponseMessage(cmdInfo, bizData);
        this.broadcast(responseMessage, userIdList);
    }

    /**
     * 广播消息给指定用户列表
     *
     * @param responseMessage 消息
     * @param userIdList      指定用户列表 (如果为 null 或 empty 就不会触发)
     */
    default void broadcast(ResponseMessage responseMessage, Collection<Long> userIdList) {
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
        final HeadMetadata headMetadata = this.getHeadMetadata();
        CmdInfo cmdInfo = headMetadata.getCmdInfo();

        this.broadcastOrderMe(cmdInfo, bizData);
    }

    /**
     * 顺序 - 给自己发送消息
     *
     * @param cmdInfo 发送到此路由
     * @param bizData 业务数据
     */
    default void broadcastOrderMe(CmdInfo cmdInfo, Object bizData) {
        var userId = this.userId();
        this.broadcastOrder(cmdInfo, bizData, userId);
    }

    /**
     * 顺序 - 给自己发送消息
     *
     * @param responseMessage 消息
     */
    default void broadcastOrderMe(ResponseMessage responseMessage) {
        var userId = this.userId();
        this.broadcastOrder(responseMessage, userId);
    }

    /**
     * 顺序 - 全服广播
     *
     * @param cmdInfo 广播到此路由
     * @param bizData 业务数据
     */
    default void broadcastOrder(CmdInfo cmdInfo, Object bizData) {
        ResponseMessage responseMessage = this.createResponseMessage(cmdInfo, bizData);
        this.broadcastOrder(responseMessage);
    }

    /**
     * 顺序 - 全服广播
     *
     * @param responseMessage 消息
     */
    default void broadcastOrder(ResponseMessage responseMessage) {
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
        ResponseMessage responseMessage = this.createResponseMessage(cmdInfo, bizData);
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
        ResponseMessage responseMessage = this.createResponseMessage(cmdInfo, bizData);
        this.broadcastOrder(responseMessage, userId);
    }

    /**
     * 顺序 - 广播消息给单个用户
     *
     * @param responseMessage 消息
     * @param userId          userId
     */
    default void broadcastOrder(ResponseMessage responseMessage, long userId) {
        employTraceId(responseMessage);

        BroadcastOrderContext broadcastOrderContext = this.getBroadcastOrderContext();
        broadcastOrderContext.broadcastOrder(responseMessage, userId);
    }

    /**
     * EventBus 是逻辑服事件总线，与业务框架、逻辑服是 1:1:1 的关系
     *
     * @return EventBus
     */
    default EventBus getEventBus() {
        return this.option(FlowAttr.eventBus);
    }

    /**
     * 发送事件给订阅者
     * <pre>
     *     1 给当前进程所有逻辑服的订阅者发送事件消息
     *     2 给其他进程的订阅者发送事件消息
     * </pre>
     *
     * @param eventSource 事件源
     */
    default void fire(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);

        EventBus eventBus = this.getEventBus();
        eventBus.fire(eventBusMessage);
    }

    /**
     * 发送事件给订阅者
     * <pre>
     *     仅给当前进程所有逻辑服的订阅者发送事件消息
     * </pre>
     *
     * @param eventSource 事件源
     */
    default void fireLocal(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);

        EventBus eventBus = this.getEventBus();
        eventBus.fireLocal(eventBusMessage);
    }

    /**
     * 发送事件给订阅者
     * <pre>
     *     仅给当前进程所有逻辑服的订阅者发送事件消息
     *
     *     [同步]，在当前线程中调用订阅者
     * </pre>
     *
     * @param eventSource 事件源
     */
    default void fireLocalSync(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);

        EventBus eventBus = this.getEventBus();
        eventBus.fireLocalSync(eventBusMessage);
    }

    /**
     * 发送事件给订阅者
     * <pre>
     *     仅给当前 EventBus 的订阅者发送事件消息。
     *     订阅者指的是已注册到 {@link EventBus#register(EventBusSubscriber)} 的订阅者。
     * </pre>
     *
     * @param eventSource 事件源
     */
    default void fireMe(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);

        EventBus eventBus = this.getEventBus();
        eventBus.fireMe(eventBusMessage);
    }

    /**
     * 发送事件给订阅者
     * <pre>
     *     仅给当前 EventBus 的订阅者发送事件消息。
     *     订阅者指的是已注册到 {@link EventBus#register(EventBusSubscriber)} 的订阅者。
     *
     *     [同步]，在当前线程中调用订阅者
     * </pre>
     *
     * @param eventSource 事件源
     */
    default void fireMeSync(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);

        EventBus eventBus = this.getEventBus();
        eventBus.fireMeSync(eventBusMessage);
    }

    private EventBusMessage createEventBusMessage(Object eventSource) {
        HeadMetadata headMetadata = this.getHeadMetadata();
        long userId = headMetadata.getUserId();
        String traceId = headMetadata.getTraceId();

        var eventBusMessage = new EventBusMessage();
        eventBusMessage.setEventSource(eventSource);
        eventBusMessage.setUserId(userId);
        eventBusMessage.setTraceId(traceId);

        return eventBusMessage;
    }

    private Executor getVirtualExecutor() {
        final HeadMetadata headMetadata = this.getHeadMetadata();
        var executorIndex = ExecutorSelectKit.getExecutorIndex(headMetadata);

        var region = UserVirtualExecutorRegion.me();
        ThreadExecutor threadExecutor = region.getThreadExecutor(executorIndex);

        return threadExecutor.executor();
    }

    private <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
        return CompletableFuture.supplyAsync(supplier, this.getVirtualExecutor());
    }

    private long userId() {
        return this.getHeadMetadata().getUserId();
    }

    private ResponseMessage createResponseMessage(CmdInfo cmdInfo, Object data) {
        Objects.requireNonNull(data);
        return BarMessageKit.createResponseMessage(cmdInfo, data);
    }

    private void employTraceId(ResponseMessage responseMessage) {
        String traceId = this.getHeadMetadata().getTraceId();

        if (Objects.nonNull(traceId)) {
            HeadMetadata headMetadata = responseMessage.getHeadMetadata();
            headMetadata.setTraceId(traceId);
        }
    }
}
