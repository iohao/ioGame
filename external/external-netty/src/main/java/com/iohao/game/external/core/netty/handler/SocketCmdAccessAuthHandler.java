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

import com.iohao.game.action.skeleton.core.exception.ActionErrorEnum;
import com.iohao.game.action.skeleton.protocol.BarMessage;
import com.iohao.game.external.core.aware.UserSessionsAware;
import com.iohao.game.external.core.config.ExternalGlobalConfig;
import com.iohao.game.external.core.hook.AccessAuthenticationHook;
import com.iohao.game.external.core.message.ExternalCodecKit;
import com.iohao.game.external.core.netty.session.SocketUserSessions;
import com.iohao.game.external.core.session.UserSession;
import com.iohao.game.external.core.session.UserSessions;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 路由访问权限相关处理
 *
 * @author 渔民小镇
 * @date 2023-05-05
 */
@ChannelHandler.Sharable
public class SocketCmdAccessAuthHandler extends SimpleChannelInboundHandler<BarMessage>
        implements UserSessionsAware {
    protected UserSessions<?, ?> userSessions;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BarMessage message) {
        if (reject(ctx, message)) {
            // 拒绝玩家直接访问 action
            return;
        }

        SocketUserSessions socketUserSessions = (SocketUserSessions) this.userSessions;
        UserSession userSession = socketUserSessions.getUserSession(ctx);
        boolean loginSuccess = userSession.isVerifyIdentity();
        if (notPass(ctx, message, loginSuccess)) {
            // 访问了需要登录才能访问的 action
            return;
        }

        // 交给下一个业务处理 (handler) , 下一个业务指的是你编排 handler 时的顺序
        ctx.fireChannelRead(message);
    }

    protected boolean reject(ChannelHandlerContext ctx, BarMessage message) {
        AccessAuthenticationHook accessAuthenticationHook = ExternalGlobalConfig.accessAuthenticationHook;
        int cmdMerge = message.getHeadMetadata().getCmdMerge();
        boolean reject = accessAuthenticationHook.reject(cmdMerge);

        if (reject) {
            ExternalCodecKit.employError(message, ActionErrorEnum.cmdInfoErrorCode);
            // 响应结果给玩家
            ctx.writeAndFlush(message);

            return true;
        }

        return false;
    }

    protected boolean notPass(ChannelHandlerContext ctx, BarMessage message, boolean loginSuccess) {
        // 是否可以访问业务方法（action），true 表示可以访问该路由对应的业务方法
        AccessAuthenticationHook accessAuthenticationHook = ExternalGlobalConfig.accessAuthenticationHook;
        int cmdMerge = message.getHeadMetadata().getCmdMerge();
        boolean pass = accessAuthenticationHook.pass(loginSuccess, cmdMerge);

        if (pass) {
            return false;
        }

        // 当访问验证没通过，通知玩家
        ExternalCodecKit.employError(message, ActionErrorEnum.verifyIdentity);

        // 响应结果给玩家
        ctx.writeAndFlush(message);

        return true;
    }

    @Override
    public void setUserSessions(UserSessions<?, ?> userSessions) {
        this.userSessions = userSessions;
    }
}
