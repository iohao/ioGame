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
import com.iohao.game.external.core.session.UserSessions;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * WebSocket 连接前的 token 验证 handler
 *
 * @author 渔民小镇
 * @date 2023-08-03
 */
public class WebSocketVerifyHandler extends ChannelInboundHandlerAdapter
        implements UserSessionsAware {

    protected SocketUserSessions userSessions;

    @Override
    public void setUserSessions(UserSessions<?, ?> userSessions) {
        this.userSessions = (SocketUserSessions) userSessions;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest request) {
            // 从 uri 中解析参数
            String uri = request.uri();
            Map<String, String> params = getParams(uri);

            // 开发者可以重写 verify 方法来扩展
            SocketUserSession userSession = userSessions.getUserSession(ctx);
            boolean verify = verify(userSession, params);

            if (verify) {
                //  验证通过后，移除自身；减少消息在 handler 中流动的次数
                ctx.pipeline().remove(this);
            } else {
                // 验证失败，关闭连接或返回错误响应
                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED);
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                return;
            }
        }

        super.channelRead(ctx, msg);
    }

    /**
     * verify
     *
     * @param userSession ctx
     * @param params      params
     * @return 返回 false 表示验证没通过，框架会关闭连接
     */
    protected boolean verify(SocketUserSession userSession, Map<String, String> params) {
        /*
         * 保存一份验证完成的数据，后续使用。
         * 开发者如果有想把解析后的数据传递到游戏逻辑服中的，
         * 可以先在这里保存一份想要传递的数据。
         */

        // 参考文档 https://www.yuque.com/iohao/game/tb1126szmgfu6u55
        return true;
    }

    protected Map<String, String> getParams(String uri) {
        return new QueryStringDecoder(uri)
                .parameters()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get(0)));
    }

}