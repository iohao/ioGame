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
package com.iohao.game.external.core.netty.handler;

import com.iohao.game.bolt.broker.core.aware.BrokerClientAware;
import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.message.BrokerClientModuleMessage;
import com.iohao.game.common.consts.IoGameLogName;
import com.iohao.game.external.core.aware.UserSessionsAware;
import com.iohao.game.external.core.netty.session.SocketUserSession;
import com.iohao.game.external.core.netty.session.SocketUserSessions;
import com.iohao.game.external.core.session.UserSessions;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 渔民小镇
 * @date 2023-02-19
 */
@Setter
@ChannelHandler.Sharable
@Slf4j(topic = IoGameLogName.ExternalTopic)
public final class SocketUserSessionHandler extends ChannelInboundHandlerAdapter
        implements UserSessionsAware, BrokerClientAware {

    SocketUserSessions userSessions;
    BrokerClient brokerClient;

    @Override
    public void setUserSessions(UserSessions<?, ?> userSessions) {
        this.userSessions = (SocketUserSessions) userSessions;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        BrokerClientModuleMessage moduleMessage = brokerClient.getBrokerClientModuleMessage();
        int idHash = moduleMessage.getIdHash();

        // 加入到 session 管理
        SocketUserSession userSession = userSessions.add(ctx);
        userSession.setExternalClientId(idHash);

        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 从 session 管理中移除
        var userSession = this.userSessions.getUserSession(ctx);
        this.userSessions.removeUserSession(userSession);

        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 从 session 管理中移除
        var userSession = this.userSessions.getUserSession(ctx);
        this.userSessions.removeUserSession(userSession);

        super.exceptionCaught(ctx, cause);
    }
}
