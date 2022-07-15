/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
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

import com.iohao.game.bolt.broker.client.external.bootstrap.heart.IdleHook;
import com.iohao.game.bolt.broker.client.external.bootstrap.message.ExternalMessage;
import com.iohao.game.bolt.broker.client.external.bootstrap.message.ExternalMessageCmdCode;
import com.iohao.game.bolt.broker.client.external.session.UserSessions;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * 心跳 handler
 *
 * @author 渔民小镇
 * @date 2022-03-13
 */
@Slf4j
@ChannelHandler.Sharable
public class IdleHandler extends ChannelInboundHandlerAdapter {

    /** 心跳事件回调 */
    final IdleHook idleHook;
    /** true : 响应心跳给客户端 */
    final boolean pong;

    public IdleHandler(IdleHook idleHook, boolean pong) {
        this.idleHook = idleHook;
        this.pong = pong;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        ExternalMessage externalMessage = (ExternalMessage) msg;

        // 心跳处理
        int cmdCode = externalMessage.getCmdCode();

        if (cmdCode == ExternalMessageCmdCode.idle) {

            if (this.pong) {
                ctx.writeAndFlush(externalMessage);
            }

            return;
        }

        // 不是心跳请求. 交给下一个业务处理 (handler) , 下一个业务指的是你编排 handler 时的顺序
        ctx.fireChannelRead(msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent event) {

            boolean close = true;

            var userSession = UserSessions.me().getUserSession(ctx);

            // 执行用户定义的心跳回调事件处理
            if (Objects.nonNull(idleHook)) {
                close = idleHook.callback(ctx, event, userSession);
            }

            // close ctx
            if (close) {
                UserSessions.me().removeUserSession(userSession);
            }

        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
