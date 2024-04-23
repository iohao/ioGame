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
package com.iohao.game.external.client.user;

import com.iohao.game.action.skeleton.core.BarMessageKit;
import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.core.CmdKit;
import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.action.skeleton.protocol.BarMessage;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.external.client.command.*;
import com.iohao.game.external.client.kit.ClientUserConfigs;
import com.iohao.game.external.core.message.ExternalMessageCmdCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * 玩家通信 channel
 * <pre>
 *     发送请求，接收服务器响应
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-07-13
 */
@Slf4j
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientUserChannel {
    final AtomicInteger msgIdSeq = new AtomicInteger(1);

    final AtomicBoolean starting = new AtomicBoolean();
    /**
     * 请求回调
     * <pre>
     *     key : msgId
     * </pre>
     */
    final Map<Integer, RequestCommand> callbackMap = new NonBlockingHashMap<>();
    /**
     * 广播监听
     * <pre>
     *     key : cmdMerge
     * </pre>
     */
    final Map<Integer, ListenCommand> listenMap = new NonBlockingHashMap<>();

    ClientChannelRead channelRead = new DefaultChannelRead();

    final DefaultClientUser clientUser;

    public Runnable closeChannel;
    public Consumer<BarMessage> clientChannel;
    /** 目标 ip （服务器 ip） */
    public InetSocketAddress inetSocketAddress;

    public ClientUserChannel(DefaultClientUser clientUser) {
        this.clientUser = clientUser;
    }

    public void request(InputCommand inputCommand) {
        CmdInfo cmdInfo = inputCommand.getCmdInfo();
        // 生成请求参数
        RequestDataDelegate requestData = inputCommand.getRequestData();

        CallbackDelegate callback = inputCommand.getCallback();

        RequestCommand requestCommand = new RequestCommand()
                .setCmdMerge(cmdInfo.getCmdMerge())
                .setTitle(inputCommand.getTitle())
                .setRequestData(requestData)
                .setCallback(callback);

        this.execute(requestCommand);
    }

    public void execute(RequestCommand requestCommand) {
        int msgId = this.msgIdSeq.incrementAndGet();
        this.callbackMap.put(msgId, requestCommand);
        CmdInfo cmdInfo = CmdInfo.of(requestCommand.getCmdMerge());

        RequestMessage requestMessage = BarMessageKit.createRequestMessage(cmdInfo);
        HeadMetadata headMetadata = requestMessage.getHeadMetadata();
        headMetadata.setMsgId(msgId);

        RequestDataDelegate requestData = requestCommand.getRequestData();
        Object data = "";
        if (Objects.nonNull(requestData)) {
            data = requestData.createRequestData();
            byte[] encode = DataCodecKit.encode(data);
            requestMessage.setData(encode);
        }

        if (ClientUserConfigs.openLogRequestCommand) {
            long userId = clientUser.getUserId();
            log.info("玩家[{}] 发起【{}】请求 - [msgId:{}] {} {}"
                    , userId
                    , requestCommand.getTitle()
                    , msgId
                    , CmdKit.mergeToShort(cmdInfo.getCmdMerge())
                    , data
            );
        }

        this.writeAndFlush(requestMessage);
    }

    public void readMessage(BarMessage message) {
        channelRead.read(message);
    }

    public void writeAndFlush(BarMessage message) {
        if (Objects.isNull(this.clientChannel)) {
            return;
        }

        InetSocketAddress inetSocketAddress = this.inetSocketAddress;
        if (Objects.nonNull(inetSocketAddress)) {
            message.getHeadMetadata().setInetSocketAddress(inetSocketAddress);
        }

        // 发送数据到游戏服务器
        this.clientChannel.accept(message);
    }

    public void addListen(ListenCommand listenCommand) {
        int cmdMerge = listenCommand.getCmdInfo().getCmdMerge();
        this.listenMap.put(cmdMerge, listenCommand);
    }

    public void closeChannel() {
        this.clientUser.setActive(false);
        Optional.ofNullable(closeChannel).ifPresent(Runnable::run);
    }

    class DefaultChannelRead implements ClientChannelRead {
        @Override
        public void read(BarMessage message) {
            HeadMetadata headMetadata = message.getHeadMetadata();
            int responseStatus = message.getResponseStatus();

            // 表示有异常消息，统一异常处理
            if (responseStatus != 0) {
                log.error("[错误码:{}] - [消息:{}] - {}", responseStatus, message.getValidatorMsg(), headMetadata.getCmdInfo());
                return;
            }

            if (headMetadata.getCmdCode() == ExternalMessageCmdCode.idle) {
                printLog(message);
                return;
            }

            CommandResult commandResult = new CommandResult(message);
            // 有回调的，交给回调处理
            int msgId = headMetadata.getMsgId();
            RequestCommand requestCommand = callbackMap.remove(msgId);

            if (Objects.nonNull(requestCommand)) {
                printLog(headMetadata, requestCommand);

                Optional.ofNullable(requestCommand.getCallback()).ifPresent(callback -> callback.callback(commandResult));

                return;
            }

            // 广播监听
            int cmdMerge = headMetadata.getCmdMerge();
            ListenCommand listenCommand = listenMap.get(cmdMerge);

            if (Objects.nonNull(listenCommand)) {
                printLog(listenCommand, cmdMerge);
                listenCommand.getCallback().callback(commandResult);
            }
        }

        private void printLog(BarMessage message) {
            if (ClientUserConfigs.openLogIdle) {
                log.info("接收服务器心跳回调 : {}", message);
            }
        }

        private void printLog(ListenCommand listenCommand, int cmdMerge) {
            if (ClientUserConfigs.openLogListenBroadcast) {
                log.info("广播监听回调 [{}] 通知 {}"
                        , listenCommand.getTitle()
                        , CmdKit.mergeToShort(cmdMerge)
                );
            }
        }

        private void printLog(HeadMetadata headMetadata, RequestCommand requestCommand) {
            if (ClientUserConfigs.openLogRequestCallback) {
                // 玩家接收服务器的响应数据
                long userId = clientUser.getUserId();
                int cmdMerge = headMetadata.getCmdMerge();

                log.info("玩家[{}] 接收【{}】回调 - [msgId:{}] {}"
                        , userId
                        , requestCommand.getTitle()
                        , headMetadata.getMsgId()
                        , CmdKit.mergeToShort(cmdMerge)
                );

                CallbackDelegate callback = requestCommand.getCallback();
                if (Objects.isNull(callback)) {
                    log.warn("callback is null");
                }
            }
        }
    }
}
