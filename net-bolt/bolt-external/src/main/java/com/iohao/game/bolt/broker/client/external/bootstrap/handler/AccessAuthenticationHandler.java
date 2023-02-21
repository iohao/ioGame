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

import com.iohao.game.action.skeleton.core.exception.ActionErrorEnum;
import com.iohao.game.bolt.broker.client.external.bootstrap.message.ExternalMessage;
import com.iohao.game.bolt.broker.client.external.config.AccessAuthenticationHook;
import com.iohao.game.bolt.broker.client.external.config.ExternalGlobalConfig;
import com.iohao.game.bolt.broker.client.external.session.UserSession;
import com.iohao.game.bolt.broker.client.external.session.UserSessions;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 路由访问验证 Handler
 *
 * @author 渔民小镇
 * @date 2023-02-14
 */
@ChannelHandler.Sharable
public final class AccessAuthenticationHandler extends SimpleChannelInboundHandler<ExternalMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ExternalMessage message) {
        if (notPass(ctx, message)) {
            // 访问了需要登录才能访问的 action
            return;
        }

        if (reject(ctx, message)) {
            // 拒绝玩家直接访问 action
            return;
        }

        // 交给下一个业务处理 (handler) , 下一个业务指的是你编排 handler 时的顺序
        ctx.fireChannelRead(message);
    }

    static boolean reject(ChannelHandlerContext ctx, ExternalMessage message) {
        AccessAuthenticationHook accessAuthenticationHook = ExternalGlobalConfig.accessAuthenticationHook;
        boolean reject = accessAuthenticationHook.reject(message.getCmdMerge());

        if (reject) {
            UserSession userSession = UserSessions.me().getUserSession(ctx);

            message.setResponseStatus(ActionErrorEnum.cmdInfoErrorCode.getCode());
            message.setValidMsg(ActionErrorEnum.cmdInfoErrorCode.getMsg());

            // 响应结果给玩家
            Channel channel = userSession.getChannel();
            channel.writeAndFlush(message);

            return true;
        }

        return false;
    }

    static boolean notPass(ChannelHandlerContext ctx, ExternalMessage message) {
        // 得到 session
        UserSession userSession = UserSessions.me().getUserSession(ctx);

        // 是否可以访问业务方法（action），true 表示可以访问该路由对应的业务方法
        AccessAuthenticationHook accessAuthenticationHook = ExternalGlobalConfig.accessAuthenticationHook;
        boolean pass = accessAuthenticationHook.pass(userSession, message.getCmdMerge());

        if (pass) {
            return false;
        }

        // 当访问验证没通过，通知玩家
        message.setResponseStatus(ActionErrorEnum.verifyIdentity.getCode());
        message.setValidMsg(ActionErrorEnum.verifyIdentity.getMsg());
        // 响应结果给玩家
        Channel channel = userSession.getChannel();
        channel.writeAndFlush(message);

        return true;
    }
}
