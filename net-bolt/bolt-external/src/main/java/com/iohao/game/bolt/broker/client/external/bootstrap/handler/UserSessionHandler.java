/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iohao.game.bolt.broker.client.external.bootstrap.handler;

import com.iohao.game.bolt.broker.client.external.session.UserSession;
import com.iohao.game.bolt.broker.client.external.session.UserSessions;
import com.iohao.game.common.kit.log.IoGameLoggerFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;

/**
 * 管理 UserSession 的 Handler
 *
 * @author 渔民小镇
 * @date 2023-02-14
 */
@ChannelHandler.Sharable
public final class UserSessionHandler extends ChannelInboundHandlerAdapter {
    static final Logger log = IoGameLoggerFactory.getLoggerCommon();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 加入到 session 管理
        UserSessions.me().add(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        // 从 session 管理中移除
        UserSession userSession = UserSessions.me().getUserSession(ctx);
        UserSessions.me().removeUserSession(userSession);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(cause.getMessage(), cause);

        // 从 session 管理中移除
        UserSession userSession = UserSessions.me().getUserSession(ctx);
        UserSessions.me().removeUserSession(userSession);
    }
}
