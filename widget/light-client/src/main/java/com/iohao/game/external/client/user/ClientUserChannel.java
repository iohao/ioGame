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

    public Consumer<BarMessage> clientChannel;
    /** 目标 ip （服务器 ip） */
    public InetSocketAddress inetSocketAddress;

    public ClientUserChannel(DefaultClientUser clientUser) {
        this.clientUser = clientUser;
    }

    @Deprecated
    public void request(InputCommand inputCommand) {
        CmdInfo cmdInfo = inputCommand.getCmdInfo();
        // 生成请求参数
        RequestDataDelegate requestData = inputCommand.getRequestData();

        // 回调相关
        Class<?> responseClass = inputCommand.getResponseClass();
        CallbackDelegate callback = inputCommand.getCallback();

        RequestCommand requestCommand = new RequestCommand()
                .setCmdMerge(cmdInfo.getCmdMerge())
                .setTitle(inputCommand.getTitle())
                .setRequestData(requestData)
                .setResponseClass(responseClass)
                .setCallback(callback);

        this.execute(requestCommand);
    }

    @Deprecated
    public void request(RequestCommand command, Object data) {
        RequestCommand requestCommand = new RequestCommand()
                .setTitle(command.getTitle())
                .setCmdMerge(command.getCmdMerge())
                .setResponseClass(command.getResponseClass())
                .setCallback(command.getCallback());

        if (Objects.nonNull(data)) {
            if (data instanceof RequestDataDelegate requestDataDelegate) {
                requestCommand.setRequestData(requestDataDelegate);
            } else {
                requestCommand.setRequestData(() -> data);
            }
        }

        this.execute(requestCommand);
    }

    @Deprecated
    public void request(CmdInfo cmdInfo, Object data, Class<?> responseClass, CallbackDelegate callback) {
        RequestCommand requestCommand = new RequestCommand()
                .setCmdMerge(cmdInfo.getCmdMerge())
                .setResponseClass(responseClass)
                .setCallback(callback);

        if (Objects.nonNull(data)) {
            if (data instanceof RequestDataDelegate requestDataDelegate) {
                requestCommand.setRequestData(requestDataDelegate);
            } else {
                requestCommand.setRequestData(() -> data);
            }
        }

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

    /**
     * 广播监听
     * <pre>
     *     监听游戏服务器广播的消息
     * </pre>
     *
     * @param cmdInfo       监听的路由
     * @param responseClass 响应后使用这个 class 来解析 data 数据
     * @param callback      结果回调（游戏服务器回传的结果）
     * @param title         广播描述
     */
    @Deprecated
    public void listenBroadcast(CmdInfo cmdInfo
            , Class<?> responseClass
            , CallbackDelegate callback
            , String title
    ) {

        ListenCommand listenCommand = new ListenCommand(cmdInfo)
                .setTitle(title)
                .setResponseClass(responseClass)
                .setCallback(callback);

        this.addListen(listenCommand);
    }

    public void addListen(ListenCommand listenCommand) {
        int cmdMerge = listenCommand.getCmdInfo().getCmdMerge();
        this.listenMap.put(cmdMerge, listenCommand);
    }

    class DefaultChannelRead implements ClientChannelRead {
        @Override
        public void read(BarMessage message) {
            HeadMetadata headMetadata = message.getHeadMetadata();

            // 表示有异常消息;统一异常处理
            int responseStatus = message.getResponseStatus();
            int cmdMerge = headMetadata.getCmdMerge();
            CmdInfo cmdInfo = headMetadata.getCmdInfo();
            int msgId = headMetadata.getMsgId();
            RequestCommand requestCommand = callbackMap.remove(msgId);

            if (responseStatus != 0) {
                log.error("[错误码:{}] - [消息:{}] - {}", responseStatus, message.getValidatorMsg(), cmdInfo);
                return;
            }

            if (headMetadata.getCmdCode() == ExternalMessageCmdCode.idle) {
                if (ClientUserConfigs.openLogIdle) {
                    log.info("接收服务器心跳回调 : {}", message);
                }

                return;
            }

            CommandResult commandResult = new CommandResult(message);

            // 有回调的，交给回调处理
            if (Objects.nonNull(requestCommand)) {

                if (ClientUserConfigs.openLogRequestCallback) {
                    // 玩家接收服务器的响应数据
                    long userId = clientUser.getUserId();

                    log.info("玩家[{}] 接收【{}】回调 - [msgId:{}] {}"
                            , userId
                            , requestCommand.getTitle()
                            , msgId
                            , CmdKit.mergeToShort(cmdMerge)
                    );
                }

                CallbackDelegate callback = requestCommand.getCallback();
                callback.callback(commandResult);

                return;
            }

            // 广播监听
            ListenCommand listenCommand = listenMap.get(cmdMerge);
            if (Objects.nonNull(listenCommand)) {
                if (ClientUserConfigs.openLogListenBroadcast) {
                    log.info("广播监听回调 [{}] 通知 {}"
                            , listenCommand.getTitle()
                            , CmdKit.mergeToShort(cmdMerge)
                    );
                }

                CallbackDelegate callback = listenCommand.getCallback();
                callback.callback(commandResult);
            }
        }
    }
}
