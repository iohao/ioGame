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
package com.iohao.game.external.core.netty.handler.ws;

import com.iohao.game.external.core.aware.UserSessionsAware;
import com.iohao.game.external.core.netty.session.SocketUserSession;
import com.iohao.game.external.core.netty.session.SocketUserSessions;
import com.iohao.game.external.core.session.UserSessionOption;
import com.iohao.game.external.core.session.UserSessions;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;

/**
 * 获取玩家真实 ip
 * <pre>
 *     nginx 配置
 *
 *     proxy_http_version 1.1;
 *     proxy_set_header Upgrade websocket;
 *     proxy_set_header Connection "Upgrade";
 *     # 将客户端的 Host 和 IP 信息一并转发到对应节点
 *     proxy_set_header X-Real-IP $remote_addr;
 *     proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
 *     proxy_set_header Host $http_host;
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-08-16
 */
public class HttpRealIpHandler extends ChannelInboundHandlerAdapter
        implements UserSessionsAware {

    protected SocketUserSessions userSessions;

    @Override
    public void setUserSessions(UserSessions<?, ?> userSessions) {
        this.userSessions = (SocketUserSessions) userSessions;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest request) {
            HttpHeaders headers = request.headers();

            String realIp = headers.get("X-Real-IP");
            SocketUserSession userSession = userSessions.getUserSession(ctx);
            userSession.option(UserSessionOption.realIp, realIp);
            ctx.pipeline().remove(this);
        }

        super.channelRead(ctx, msg);
    }
}
