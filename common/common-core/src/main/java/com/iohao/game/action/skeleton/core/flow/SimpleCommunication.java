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

import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.core.commumication.*;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.action.skeleton.protocol.collect.ResponseCollectMessage;
import com.iohao.game.action.skeleton.protocol.external.RequestCollectExternalMessage;
import com.iohao.game.action.skeleton.protocol.external.ResponseCollectExternalMessage;
import com.iohao.game.common.kit.TraceKit;
import com.iohao.game.common.kit.concurrent.TaskKit;
import org.slf4j.MDC;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

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
interface SimpleCommunication {
    /**
     * 创建请求对象
     *
     * @param cmdInfo 路由
     * @param data    业务数据
     * @return 请求对象
     */
    RequestMessage createRequestMessage(CmdInfo cmdInfo, Object data);

    /**
     * FlowContext request HeadMetadata
     *
     * @return HeadMetadata
     */
    HeadMetadata getHeadMetadata();

    /**
     * 框架网络通讯聚合接口
     *
     * @return 框架网络通讯聚合接口
     */
    CommunicationAggregationContext aggregationContext();

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
        RequestMessage requestMessage = this.createRequestMessage(cmdInfo, data);
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
        InvokeModuleContext invokeModuleContext = this.getInvokeModuleContext();
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
        RequestMessage requestMessage = this.createRequestMessage(cmdInfo, data);
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
        return TaskKit.supplyAsync(() -> this.invokeModuleMessage(requestMessage));
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
        RequestMessage requestMessage = this.createRequestMessage(cmdInfo, data);
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
        HeadMetadata headMetadata = requestMessage.getHeadMetadata();
        String traceId = headMetadata.getTraceId();

        if (Objects.isNull(traceId)) {
            this.invokeModuleMessageFuture(requestMessage).thenAcceptAsync(callback, TaskKit.getVirtualExecutor());
            return;
        }

        this.invokeModuleMessageFuture(requestMessage).thenAcceptAsync(responseMessage -> {
            try {
                MDC.put(TraceKit.traceName, traceId);
                callback.accept(responseMessage);
            } finally {
                MDC.clear();
            }
        }, TaskKit.getVirtualExecutor());
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
        RequestMessage requestMessage = createRequestMessage(cmdInfo, data);
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
        InvokeModuleContext invokeModuleContext = this.getInvokeModuleContext();
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
        RequestMessage requestMessage = createRequestMessage(cmdInfo, data);
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
        return TaskKit.supplyAsync(() -> this.invokeModuleCollectMessage(requestMessage));
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
        RequestMessage requestMessage = this.createRequestMessage(cmdInfo, data);
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
        HeadMetadata headMetadata = requestMessage.getHeadMetadata();
        String traceId = headMetadata.getTraceId();

        if (Objects.isNull(traceId)) {
            this.invokeModuleCollectMessageFuture(requestMessage).thenAcceptAsync(callback, TaskKit.getVirtualExecutor());
            return;
        }

        this.invokeModuleCollectMessageFuture(requestMessage).thenAcceptAsync(responseCollectMessage -> {
            try {
                MDC.put(TraceKit.traceName, traceId);
                callback.accept(responseCollectMessage);
            } finally {
                MDC.clear();
            }
        }, TaskKit.getVirtualExecutor());
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
        RequestMessage requestMessage = this.createRequestMessage(cmdInfo, data);
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
        InvokeModuleContext invokeModuleContext = this.getInvokeModuleContext();
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
        RequestCollectExternalMessage request = createRequestCollectExternalMessage(bizCode, data);
        return this.invokeExternalModuleCollectMessage(request);
    }

    private RequestCollectExternalMessage createRequestCollectExternalMessage(int bizCode, Serializable data) {
        // 得到发起请求的游戏对外服 id
        HeadMetadata headMetadata = this.getHeadMetadata();
        int sourceClientId = headMetadata.getSourceClientId();

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
    default ResponseCollectExternalMessage invokeExternalModuleCollectMessage(RequestCollectExternalMessage request) {
        // MDC
        String traceId = this.getHeadMetadata().getTraceId();
        request.setTraceId(traceId);

        // 【游戏逻辑服】与【游戏对外服】通讯上下文
        InvokeExternalModuleContext invokeExternalModuleContext = this.getInvokeExternalModuleContext();
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
    default CompletableFuture<ResponseCollectExternalMessage> invokeExternalModuleCollectMessageFuture(int bizCode, Serializable data) {
        RequestCollectExternalMessage requestCollectExternalMessage = this.createRequestCollectExternalMessage(bizCode, data);
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
    default CompletableFuture<ResponseCollectExternalMessage> invokeExternalModuleCollectMessageFuture(RequestCollectExternalMessage request) {
        return TaskKit.supplyAsync(() -> this.invokeExternalModuleCollectMessage(request));
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
    default void invokeExternalModuleCollectMessageAsync(int bizCode, Consumer<ResponseCollectExternalMessage> callback) {
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
    default void invokeExternalModuleCollectMessageAsync(int bizCode, Serializable data, Consumer<ResponseCollectExternalMessage> callback) {
        RequestCollectExternalMessage requestCollectExternalMessage = this.createRequestCollectExternalMessage(bizCode, data);
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
    default void invokeExternalModuleCollectMessageAsync(RequestCollectExternalMessage request, Consumer<ResponseCollectExternalMessage> callback) {
        String traceId = request.getTraceId();

        if (Objects.isNull(traceId)) {
            this.invokeExternalModuleCollectMessageFuture(request).thenAcceptAsync(callback, TaskKit.getVirtualExecutor());
            return;
        }

        this.invokeExternalModuleCollectMessageFuture(request).thenAcceptAsync(responseCollectExternalMessage -> {
            try {
                MDC.put(TraceKit.traceName, traceId);
                callback.accept(responseCollectExternalMessage);
            } finally {
                MDC.clear();
            }
        }, TaskKit.getVirtualExecutor());
    }
}
