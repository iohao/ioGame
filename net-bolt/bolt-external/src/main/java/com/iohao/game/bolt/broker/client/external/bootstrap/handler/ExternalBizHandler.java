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
package com.iohao.game.bolt.broker.client.external.bootstrap.handler;

import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.bolt.broker.client.external.bootstrap.ExternalKit;
import com.iohao.game.bolt.broker.client.external.bootstrap.message.ExternalMessage;
import com.iohao.game.bolt.broker.client.external.session.UserSession;
import com.iohao.game.bolt.broker.client.external.session.UserSessions;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 对外服 业务处理类
 * <pre>
 *     负责把游戏端的请求转发给网关
 * </pre>
 *
 * <pre>
 *     已经废弃，将在下个大版本中移除，由
 *     UserSessionHandler、
 *     AccessAuthenticationHandler、
 *     RequestBrokerHandler、
 *     三个 Handler 代替，这样更符合单一职责。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-01-19
 */
@Slf4j
@Deprecated
@ChannelHandler.Sharable
public class ExternalBizHandler extends SimpleChannelInboundHandler<ExternalMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ExternalMessage message) {

        // 访问了需要登录才能访问的 action
        if (AccessAuthenticationHandler.notPass(ctx, message)) {
            return;
        }

        // 拒绝玩家直接访问 action
        if (AccessAuthenticationHandler.reject(ctx, message)) {
            return;
        }

        // 将 message 转换成 RequestMessage
        RequestMessage requestMessage = ExternalKit.convertRequestMessage(message);

        try {
            // 由内部逻辑服转发用户请求到游戏网关，在由网关转到具体的业务逻辑服
            ExternalKit.requestGateway(ctx, requestMessage);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        // 从 session 管理中移除
        UserSession userSession = UserSessions.me().getUserSession(ctx);
        UserSessions.me().removeUserSession(userSession);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 加入到 session 管理
        UserSessions.me().add(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(cause.getMessage(), cause);

        // 从 session 管理中移除
        UserSession userSession = UserSessions.me().getUserSession(ctx);
        UserSessions.me().removeUserSession(userSession);
    }
}
