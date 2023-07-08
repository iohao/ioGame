/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.iohao.game.external.client.core;

import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.common.kit.ExecutorKit;
import com.iohao.game.external.core.message.ExternalMessage;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @author 渔民小镇
 * @date 2023-05-29
 */
@Slf4j
@Setter
@UtilityClass
public class ClientCommands {
    final AtomicBoolean starting = new AtomicBoolean();
    final AtomicInteger msgIdSeq = new AtomicInteger(1);
    final Map<Integer, ClientCallback> listenBroadcastMap = new NonBlockingHashMap<>();
    final BlockingQueue<ClientRequest> blockingQueue = new LinkedBlockingQueue<>();
    /** 回调 */
    final Map<Integer, ClientCallback> callbackMap = new NonBlockingHashMap<>();

    public Consumer<ExternalMessage> clientChannel;
    /** 目标 ip （服务器 ip） */
    public InetSocketAddress inetSocketAddress;

    /** 接收服务器数据之前，可以做些打印 */
    public Consumer<ExternalMessage> channelRead0Before = externalMessage -> {
    };

    /** 发送数据到服务器之前，可以做些打印 */
    public Consumer<ExternalMessage> requestBefore = externalMessage -> {
        int cmdMerge = externalMessage.getCmdMerge();
        CmdInfo cmdInfo = CmdInfo.getCmdInfo(cmdMerge);

//        log.info("requestBefore --- {} --- msgId:{}", cmdInfo, externalMessage.getMsgId());
    };


    public void startup() {
        if (starting.get()) {
            return;
        }

        if (!starting.compareAndSet(false, true)) {
            return;
        }

        ExecutorKit.newSingleThreadExecutor("ClientCommands").execute(() -> {

            for (; ; ) {

                ClientRequest clientRequest;

                try {
                    clientRequest = blockingQueue.take();
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                    continue;
                }

                // 发送请求到游戏服务器
                writeAndFlush(clientRequest);

                long sleep = clientRequest.sleepMilliseconds;
                if (sleep > 0) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(sleep);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void writeAndFlush(ClientRequest clientRequest) {
        ExternalMessage externalMessage = clientRequest.externalMessage;
        if (Objects.nonNull(inetSocketAddress)) {
            externalMessage.setOther(inetSocketAddress);
        }

        // 发送之前
        requestBefore.accept(externalMessage);

        // 发送数据到游戏服务器
        clientChannel.accept(externalMessage);
    }

    /**
     * 将请求发送到游戏服务器
     *
     * <pre>
     *     无回调。
     *     或者说将回调用 client action 的方式来处理
     * </pre>
     *
     * @param externalMessage externalMessage
     */
    public void request(ExternalMessage externalMessage) {
        request(externalMessage, null, null);
    }

    /**
     * 将请求发送到游戏服务器
     * <pre>
     *     服务器响应数据后，将执行回调 responseCallback
     * </pre>
     *
     * @param externalMessage externalMessage
     * @param responseClass   响应后使用这个 class 来解析 data 数据
     * @param resultCallback  结果回调（游戏服务器回传的结果）
     */
    public void request(ExternalMessage externalMessage
            , Class<?> responseClass
            , Consumer<ClientCommandResult> resultCallback) {
        request(externalMessage, responseClass, resultCallback, 0);
    }

    /**
     * 将请求发送到游戏服务器
     * <pre>
     *     服务器响应数据后，将执行回调 responseCallback
     * </pre>
     *
     * @param externalMessage externalMessage
     * @param responseClass   响应后使用这个 class 来解析 data 数据
     * @param resultCallback  结果回调（游戏服务器回传的结果）
     * @param milliseconds    发送请求后，延迟 milliseconds 时间
     */
    public void request(ExternalMessage externalMessage
            , Class<?> responseClass
            , Consumer<ClientCommandResult> resultCallback
            , long milliseconds) {

        int msgId = msgIdSeq.incrementAndGet();
        externalMessage.setMsgId(msgId);

        ClientRequest clientRequest = new ClientRequest();
        clientRequest.msgId = msgId;
        clientRequest.externalMessage = externalMessage;
        clientRequest.sleepMilliseconds = milliseconds;
        blockingQueue.add(clientRequest);

        if (Objects.isNull(resultCallback) || Objects.isNull(responseClass)) {
            return;
        }

        // 回调
        ClientCallback clientCallback = new ClientCallback();
        clientCallback.msgId = msgId;
        clientCallback.responseClass = responseClass;
        clientCallback.responseCallback = resultCallback;
        callbackMap.put(msgId, clientCallback);
    }

    /**
     * 广播监听
     * <pre>
     *     监听游戏服务器广播的消息
     * </pre>
     *
     * @param cmdInfo        监听的路由
     * @param responseClass  响应后使用这个 class 来解析 data 数据
     * @param resultCallback 结果回调（游戏服务器回传的结果）
     */
    public void listenBroadcast(CmdInfo cmdInfo
            , Class<?> responseClass
            , Consumer<ClientCommandResult> resultCallback) {

        // 回调
        ClientCallback clientCallback = new ClientCallback();
        clientCallback.responseClass = responseClass;
        clientCallback.responseCallback = resultCallback;

        int cmdMerge = cmdInfo.getCmdMerge();

        if (listenBroadcastMap.containsKey(cmdMerge)) {
            throw new RuntimeException("相同的广播路由，只能监听一个");
        }

        listenBroadcastMap.put(cmdMerge, clientCallback);
    }

    public void read(ExternalMessage externalMessage, BarSkeleton barSkeleton) {
        channelRead0Before.accept(externalMessage);

        // 表示有异常消息;统一异常处理
        int responseStatus = externalMessage.getResponseStatus();
        int cmdMerge = externalMessage.getCmdMerge();
        CmdInfo cmdInfo = CmdInfo.getCmdInfo(cmdMerge);

        if (responseStatus != 0) {
            log.error("错误码:{} {} {}", responseStatus, externalMessage.getValidMsg(), cmdInfo);
            return;
        }

        // 有回调的，交给回调处理
        int msgId = externalMessage.getMsgId();

        if (msgId != 0) {
            // 如果有 callback ，优先交给 callback 处理
            ClientCallback clientCallback = callbackMap.get(msgId);
            if (Objects.nonNull(clientCallback)) {
                clientCallback.callback(externalMessage);
                return;
            }
        }

        var clientCallback = listenBroadcastMap.get(cmdMerge);
        if (Objects.nonNull(clientCallback)) {
            // 如果配置了广播监听，优先在这处理
            clientCallback.callback(externalMessage);
            return;
        }

        /*
         * 没有回调的，交给 client action 处理
         * 没有 msgId 的，一般是广播消息，交给 client action 处理
         */
        ClientActionProcess.action(externalMessage, barSkeleton);
    }
}
