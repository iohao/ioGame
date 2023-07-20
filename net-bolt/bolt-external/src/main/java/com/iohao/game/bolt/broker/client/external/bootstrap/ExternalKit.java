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
package com.iohao.game.bolt.broker.client.external.bootstrap;

import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcCommandType;
import com.iohao.game.action.skeleton.core.CmdKit;
import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.action.skeleton.core.commumication.BrokerClientContext;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.bolt.broker.client.external.ExternalHelper;
import com.iohao.game.bolt.broker.client.external.bootstrap.message.ExternalMessage;
import com.iohao.game.bolt.broker.client.external.bootstrap.message.ExternalMessageCmdCode;
import com.iohao.game.bolt.broker.client.external.config.ExternalGlobalConfig;
import com.iohao.game.bolt.broker.client.external.session.UserSession;
import com.iohao.game.bolt.broker.client.external.session.UserSessions;
import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.message.BroadcastMessage;
import com.iohao.game.bolt.broker.core.message.BrokerClientModuleMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Objects;

/**
 * @author 渔民小镇
 * @date 2022-01-18
 */
@UtilityClass
public class ExternalKit {
    public RequestMessage convertRequestMessage(ExternalMessage message) {

        int cmdMerge = message.getCmdMerge();
        byte[] data = message.getData();

        RequestMessage requestMessage = createRequestMessage(cmdMerge, data);
        HeadMetadata headMetadata = requestMessage.getHeadMetadata();
        headMetadata.setMsgId(message.getMsgId());

        return requestMessage;
    }

    /**
     * 创建请求消息
     *
     * @param cmdMerge 路由 {@link CmdKit#merge(int, int)}
     * @param data     业务数据
     * @return 请求消息
     */
    public RequestMessage createRequestMessage(int cmdMerge, Object data) {
        byte[] bytes = null;

        if (data != null) {
            bytes = DataCodecKit.encode(data);
        }

        return createRequestMessage(cmdMerge, bytes);
    }

    /**
     * 创建请求消息
     *
     * @param cmdMerge 路由 {@link CmdKit#merge(int, int)}
     * @return 请求消息
     */
    public RequestMessage createRequestMessage(int cmdMerge) {
        return createRequestMessage(cmdMerge, null);
    }

    /**
     * 创建请求消息
     *
     * @param cmdMerge 路由 {@link CmdKit#merge(int, int)}
     * @param data     业务数据 byte[]
     * @return 请求消息
     */
    public RequestMessage createRequestMessage(int cmdMerge, byte[] data) {

        BrokerClient brokerClient = (BrokerClient) ExternalHelper.me().getBrokerClient();

        BrokerClientModuleMessage brokerClientModuleMessage = brokerClient.getBrokerClientModuleMessage();

        int idHash = brokerClientModuleMessage.getIdHash();

        // 元信息
        HeadMetadata headMetadata = new HeadMetadata()
                .setCmdMerge(cmdMerge)
                .setRpcCommandType(RpcCommandType.REQUEST_ONEWAY)
                .setSourceClientId(idHash);

        // 请求
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setHeadMetadata(headMetadata);

        requestMessage.setData(data);

        return requestMessage;
    }

    public ExternalMessage convertExternalMessage(ResponseMessage responseMessage) {
        HeadMetadata headMetadata = responseMessage.getHeadMetadata();

        // 路由
        int cmdMerge = headMetadata.getCmdMerge();
        // 业务数据
        byte[] data = responseMessage.getData();

        // 游戏框架内置的协议， 与游戏前端相互通讯的协议
        ExternalMessage externalMessage = createExternalMessage();
        externalMessage.setCmdMerge(cmdMerge);
        externalMessage.setData(data);
        // 状态码
        externalMessage.setResponseStatus(responseMessage.getResponseStatus());
        // 验证信息（异常消息）
        externalMessage.setValidMsg(responseMessage.getValidatorMsg());
        externalMessage.setMsgId(headMetadata.getMsgId());

        return externalMessage;
    }

    public ExternalMessage createExternalMessage() {
        // 游戏框架内置的协议， 与游戏前端相互通讯的协议
        ExternalMessage externalMessage = new ExternalMessage();
        // 请求命令类型: 0 心跳，1 业务
        externalMessage.setCmdCode(ExternalMessageCmdCode.biz);
        // 协议开关，用于一些协议级别的开关控制，比如 安全加密校验等。 : 0 不校验
        externalMessage.setProtocolSwitch(ExternalGlobalConfig.protocolSwitch);
        return externalMessage;
    }

    public ExternalMessage createExternalMessage(int cmd, int subCmd) {
        ExternalMessage externalMessage = ExternalKit.createExternalMessage();
        externalMessage.setCmdMerge(cmd, subCmd);
        return externalMessage;
    }

    public ExternalMessage createExternalMessage(int cmd, int subCmd, Object object) {
        byte[] data = null;

        if (object != null) {
            data = DataCodecKit.encode(object);
        }

        return ExternalKit.createExternalMessage(cmd, subCmd, data);
    }

    public ExternalMessage createExternalMessage(int cmd, int subCmd, byte[] data) {
        // 游戏框架内置的协议， 与游戏前端相互通讯的协议
        ExternalMessage externalMessage = ExternalKit.createExternalMessage(cmd, subCmd);

        // 业务数据
        externalMessage.setData(data);

        return externalMessage;
    }

    public void writeAndFlush(long userId, ExternalMessage message) {

        if (!UserSessions.me().existUserSession(userId)) {
            return;
        }

        UserSession userSession = UserSessions.me().getUserSession(userId);

        Channel channel = userSession.getChannel();

        channel.writeAndFlush(message);
    }

    /**
     * 请求游戏网关
     *
     * @param ctx            ctx
     * @param requestMessage 请求消息
     * @throws RemotingException e
     */

    public void requestGateway(ChannelHandlerContext ctx, RequestMessage requestMessage) throws RemotingException {
        UserSession userSession = UserSessions.me().getUserSession(ctx);
        requestGateway(userSession, requestMessage);
    }

    public void requestGateway(UserSession userSession, RequestMessage requestMessage) throws RemotingException {
        // 给请求消息加上一些 user 自身的数据
        userSession.employ(requestMessage);

        // 由内部逻辑服转发用户请求到游戏网关，在由网关转到具体的业务逻辑服
        BrokerClientContext brokerClient = ExternalHelper.me().getBrokerClient();

        try {
            brokerClient.oneway(requestMessage);
        } catch (Exception e) {
            // TODO: 目前为了兼容，暂时这样写。下个大版本在动这里。
            if (e instanceof RemotingException remotingException) {
                throw remotingException;
            }
        }
    }

    public void broadcast(BroadcastMessage message) {

        ResponseMessage responseMessage = message.getResponseMessage();
        ExternalMessage externalMessage = ExternalKit.convertExternalMessage(responseMessage);

        // 推送消息给全服真实用户
        if (message.isBroadcastAll()) {
            // 给全体推送
            UserSessions.me().broadcast(externalMessage);
            return;
        }

        // 推送消息给指定的真实用户列表
        Collection<Long> userIdList = message.getUserIdList();
        if (Objects.nonNull(userIdList)) {
            for (Long userId : userIdList) {
                ExternalKit.writeAndFlush(userId, externalMessage);
            }

            return;
        }

        // 推送消息给单个真实用户
        HeadMetadata headMetadata = responseMessage.getHeadMetadata();
        long userId = headMetadata.getUserId();
        ExternalKit.writeAndFlush(userId, externalMessage);
    }
}
