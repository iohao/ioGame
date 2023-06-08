/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.iohao.game.external.core.netty.handler;

import com.iohao.game.action.skeleton.core.exception.ActionErrorEnum;
import com.iohao.game.external.core.aware.UserSessionsAware;
import com.iohao.game.external.core.config.ExternalGlobalConfig;
import com.iohao.game.external.core.hook.AccessAuthenticationHook;
import com.iohao.game.external.core.kit.ExternalKit;
import com.iohao.game.external.core.message.ExternalMessage;
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
public class SocketCmdAccessAuthHandler extends SimpleChannelInboundHandler<ExternalMessage>
        implements UserSessionsAware {
    protected UserSessions<?, ?> userSessions;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ExternalMessage message) {
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

    protected boolean reject(ChannelHandlerContext ctx, ExternalMessage message) {
        AccessAuthenticationHook accessAuthenticationHook = ExternalGlobalConfig.accessAuthenticationHook;
        boolean reject = accessAuthenticationHook.reject(message.getCmdMerge());

        if (reject) {
            ExternalKit.employError(message, ActionErrorEnum.cmdInfoErrorCode);
            // 响应结果给玩家
            ctx.writeAndFlush(message);

            return true;
        }

        return false;
    }

    protected boolean notPass(ChannelHandlerContext ctx, ExternalMessage message, boolean loginSuccess) {
        // 是否可以访问业务方法（action），true 表示可以访问该路由对应的业务方法
        AccessAuthenticationHook accessAuthenticationHook = ExternalGlobalConfig.accessAuthenticationHook;
        boolean pass = accessAuthenticationHook.pass(loginSuccess, message.getCmdMerge());

        if (pass) {
            return false;
        }

        // 当访问验证没通过，通知玩家
        ExternalKit.employError(message, ActionErrorEnum.verifyIdentity);

        // 响应结果给玩家
        ctx.writeAndFlush(message);

        return true;
    }

    @Override
    public void setUserSessions(UserSessions<?, ?> userSessions) {
        this.userSessions = userSessions;
    }
}
