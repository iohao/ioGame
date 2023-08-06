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
package com.iohao.game.external.core.netty.handler;

import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.bolt.broker.core.aware.BrokerClientAware;
import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.message.BrokerClientModuleMessage;
import com.iohao.game.common.consts.IoGameLogName;
import com.iohao.game.external.core.aware.UserSessionsAware;
import com.iohao.game.external.core.kit.ExternalKit;
import com.iohao.game.external.core.message.ExternalMessage;
import com.iohao.game.external.core.netty.session.SocketUserSession;
import com.iohao.game.external.core.netty.session.SocketUserSessions;
import com.iohao.game.external.core.session.UserSessions;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 渔民小镇
 * @date 2023-02-19
 */
@ChannelHandler.Sharable
@Slf4j(topic = IoGameLogName.ExternalTopic)
public final class SocketRequestBrokerHandler extends SimpleChannelInboundHandler<ExternalMessage>
        implements UserSessionsAware, BrokerClientAware {
    BrokerClient brokerClient;
    SocketUserSessions userSessions;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ExternalMessage message) {
        // 将 message 转换成 RequestMessage
        RequestMessage requestMessage = this.convertRequestMessage(message);

        // 给请求消息加上一些 user 自身的数据
        SocketUserSession userSession = this.userSessions.getUserSession(ctx);
        userSession.employ(requestMessage);

        try {
            // 请求游戏网关，在由网关转到具体的业务逻辑服
            brokerClient.oneway(requestMessage);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void setBrokerClient(BrokerClient brokerClient) {
        this.brokerClient = brokerClient;
    }

    @Override
    public void setUserSessions(UserSessions<?, ?> userSessions) {
        this.userSessions = (SocketUserSessions) userSessions;
    }

    private RequestMessage convertRequestMessage(ExternalMessage externalMessage) {

        BrokerClientModuleMessage brokerClientModuleMessage = brokerClient.getBrokerClientModuleMessage();
        int idHash = brokerClientModuleMessage.getIdHash();

        return ExternalKit.convertRequestMessage(externalMessage, idHash);
    }
}
