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

import com.iohao.game.action.skeleton.core.exception.ActionErrorEnum;
import com.iohao.game.bolt.broker.client.external.bootstrap.message.ExternalMessage;
import com.iohao.game.bolt.broker.client.external.config.ExternalGlobalConfig;
import com.iohao.game.bolt.broker.client.external.session.UserSession;
import com.iohao.game.bolt.broker.client.external.session.UserSessions;
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
        boolean reject = ExternalGlobalConfig.accessAuthenticationHook.reject(message.getCmdMerge());

        if (reject) {

            message.setResponseStatus(ActionErrorEnum.cmdInfoErrorCode.getCode());
            message.setValidMsg(ActionErrorEnum.cmdInfoErrorCode.getMsg());

            // 响应结果给玩家
            ctx.writeAndFlush(message);

            return true;
        }

        return false;
    }

    static boolean notPass(ChannelHandlerContext ctx, ExternalMessage message) {
        // 得到 session
        UserSession userSession = UserSessions.me().getUserSession(ctx);

        // 是否可以访问业务方法（action），true 表示可以访问该路由对应的业务方法
        boolean pass = ExternalGlobalConfig.accessAuthenticationHook.pass(userSession, message.getCmdMerge());

        if (pass) {
            return false;
        }

        // 当访问验证没通过，通知玩家
        message.setResponseStatus(ActionErrorEnum.verifyIdentity.getCode());
        message.setValidMsg(ActionErrorEnum.verifyIdentity.getMsg());
        // 响应结果给玩家
        ctx.writeAndFlush(message);

        return true;
    }
}
