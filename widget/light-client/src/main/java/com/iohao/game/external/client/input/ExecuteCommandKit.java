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
package com.iohao.game.external.client.input;

import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.common.kit.ExecutorKit;
import com.iohao.game.common.kit.StrKit;
import com.iohao.game.external.core.kit.ExternalKit;
import com.iohao.game.external.core.message.ExternalMessage;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;

import java.net.InetSocketAddress;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 执行客户端请求命令
 *
 * @author 渔民小镇
 * @date 2023-07-08
 */
@Slf4j
@UtilityClass
public class ExecuteCommandKit {
    final AtomicBoolean starting = new AtomicBoolean();
    final AtomicInteger msgIdSeq = new AtomicInteger(1);
    final Map<Integer, ListenBroadcastCommand> listenBroadcastMap = new LinkedHashMap<>();
    final BlockingQueue<CommandRequest> blockingQueue = new LinkedBlockingQueue<>();
    /** 回调 */
    final Map<Integer, CommandCallback> callbackMap = new NonBlockingHashMap<>();
    ClientChannelRead channelRead = new DefaultChannelRead();

    void request(InputCommand inputCommand) {
        CmdInfo cmdInfo = inputCommand.getCmdInfo();
        // 生成请求参数
        Object requestData = inputCommand.getRequestData();


        Class<?> responseClass = inputCommand.getResponseClass();
        InputCallback callback = inputCommand.getCallback();
        request(cmdInfo, requestData, responseClass, callback);
    }

    public void request(CmdInfo cmdInfo, Object requestData, Class<?> responseClass, InputCallback callback) {
        int msgId = msgIdSeq.incrementAndGet();

        ExternalMessage externalMessage = ExternalKit.createExternalMessage(cmdInfo, requestData);
        externalMessage.setMsgId(msgId);

        // 请求命令
        CommandRequest commandRequest = new CommandRequest(msgId, externalMessage);
        blockingQueue.add(commandRequest);

        // 回调命令
        CommandCallback commandCallback = new CommandCallback();
        commandCallback.msgId = msgId;
        commandCallback.responseClass = responseClass;
        commandCallback.callback = callback;
        commandCallback.requestData = requestData;

        callbackMap.put(msgId, commandCallback);
    }

    public void read(ExternalMessage externalMessage, BarSkeleton barSkeleton) {
        channelRead.read(externalMessage, barSkeleton);
    }

    public void startup() {
        if (starting.get()) {
            return;
        }

        if (!starting.compareAndSet(false, true)) {
            return;
        }

        String simpleName = ExecuteCommandKit.class.getSimpleName();
        ExecutorKit.newSingleThreadExecutor(simpleName).execute(() -> {

            for (; ; ) {

                try {
                    var commandRequest = blockingQueue.take();
                    // 发送请求到游戏服务器
                    writeAndFlush(commandRequest);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
            }
        });

        InputCommands.start();
    }

    private void writeAndFlush(CommandRequest clientRequest) {
        ExternalMessage externalMessage = clientRequest.externalMessage();

        InetSocketAddress inetSocketAddress = ClientChannelInfo.inetSocketAddress;
        if (Objects.nonNull(inetSocketAddress)) {
            externalMessage.setOther(inetSocketAddress);
        }

        // 发送数据到游戏服务器
        ClientChannelInfo.clientChannel.accept(externalMessage);
    }

    /**
     * 广播监听
     * <pre>
     *     监听游戏服务器广播的消息
     * </pre>
     *
     * @param cmdInfo       监听的路由
     * @param responseClass 响应后使用这个 class 来解析 data 数据
     * @param callback      结果回调（游戏服务器回传的结果）
     * @param description   广播描述
     */
    public void listenBroadcast(CmdInfo cmdInfo
            , Class<?> responseClass
            , InputCallback callback
            , String description
    ) {

        // 回调
        CommandCallback commandCallback = new CommandCallback();
        commandCallback.responseClass = responseClass;
        commandCallback.callback = callback;

        int cmdMerge = cmdInfo.getCmdMerge();

        if (listenBroadcastMap.containsKey(cmdMerge)) {
            throw new RuntimeException("相同的广播路由，只能监听一个");
        }

        ListenBroadcastCommand broadcastCommand = new ListenBroadcastCommand(cmdInfo);
        broadcastCommand.setCommandCallback(commandCallback);

        if (StrKit.isNotEmpty(description)) {
            broadcastCommand.setDescription(description);
        }

        listenBroadcastMap.put(cmdMerge, broadcastCommand);
    }

    class DefaultChannelRead implements ClientChannelRead {
        @Override
        public void read(ExternalMessage externalMessage, BarSkeleton barSkeleton) {
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
                CommandCallback commandCallback = callbackMap.get(msgId);
                if (Objects.nonNull(commandCallback)) {
                    commandCallback.callback(externalMessage);
                    return;
                }
            }

            // 有广播监听的，
            ListenBroadcastCommand broadcastCommand = listenBroadcastMap.get(cmdMerge);

            if (Objects.nonNull(broadcastCommand)) {
                // 如果配置了广播监听，优先在这处理
                CommandCallback commandCallback = broadcastCommand.getCommandCallback();
                commandCallback.callback(externalMessage);
                return;
            }

            /*
             * 没有回调的，交给 client action 处理
             * 没有 msgId 的，一般是广播消息，交给 client action 处理
             */
            ClientActionProcess.action(externalMessage, barSkeleton);
        }
    }
}
