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

import com.iohao.game.action.skeleton.protocol.BarMessage;
import com.iohao.game.external.core.ExternalCoreSetting;
import com.iohao.game.external.core.aware.ExternalCoreSettingAware;
import com.iohao.game.external.core.hook.IdleHook;
import com.iohao.game.external.core.hook.internal.IdleProcessSetting;
import com.iohao.game.external.core.message.ExternalMessageCmdCode;
import com.iohao.game.external.core.netty.DefaultExternalCoreSetting;
import com.iohao.game.external.core.netty.session.SocketUserSession;
import com.iohao.game.external.core.session.UserSessions;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.Objects;

/**
 * 心跳相关的 Handler
 *
 * @author 渔民小镇
 * @date 2023-02-18
 */
@ChannelHandler.Sharable
public final class SocketIdleHandler extends ChannelInboundHandlerAdapter
        implements ExternalCoreSettingAware {
    /** 心跳事件回调 */
    IdleHook<IdleStateEvent> idleHook;
    /** true : 响应心跳给客户端 */
    boolean pong;
    UserSessions<ChannelHandlerContext, SocketUserSession> userSessions;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        BarMessage message = (BarMessage) msg;

        int cmdCode = message.getHeadMetadata().getCmdCode();
        if (cmdCode != ExternalMessageCmdCode.idle) {
            // 不是心跳请求. 交给下一个业务处理 (handler) , 下一个业务指的是你编排 handler 时的顺序
            ctx.fireChannelRead(msg);
            return;
        }

        // 心跳处理
        if (this.pong) {

            if (Objects.nonNull(this.idleHook)) {
                // 心跳响应前的处理
                this.idleHook.pongBefore(message);
            }

            ctx.writeAndFlush(message);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent event) {

            boolean close = true;

            var userSession = userSessions.getUserSession(ctx);

            // 执行用户定义的心跳回调事件处理
            if (Objects.nonNull(idleHook)) {
                close = idleHook.callback(userSession, event);
            }

            // close ctx
            if (close) {
                this.userSessions.removeUserSession(userSession);
            }

        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setExternalCoreSetting(ExternalCoreSetting externalCoreSetting) {

        if (Objects.nonNull(this.userSessions)) {
            return;
        }

        DefaultExternalCoreSetting setting = (DefaultExternalCoreSetting) externalCoreSetting;
        IdleProcessSetting idleProcessSetting = setting.getIdleProcessSetting();
        this.idleHook = idleProcessSetting.getIdleHook();
        this.pong = idleProcessSetting.isPong();
        this.userSessions = (UserSessions<ChannelHandlerContext, SocketUserSession>) setting.getUserSessions();
    }
}
