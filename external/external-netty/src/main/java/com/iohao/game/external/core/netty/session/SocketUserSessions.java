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
package com.iohao.game.external.core.netty.session;


import com.iohao.game.external.core.session.UserChannelId;
import com.iohao.game.external.core.session.UserSessionState;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

/**
 * tcp、websocket 使用的 session 管理器
 * <pre>
 *     具备长连接的 session 管理器
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-02-18
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class SocketUserSessions extends AbstractUserSessions<ChannelHandlerContext, SocketUserSession> {
    /** 用户 session，与channel是 1:1 的关系 */
    static final AttributeKey<SocketUserSession> userSessionKey = AttributeKey.valueOf("userSession");

    final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public SocketUserSession add(ChannelHandlerContext channelHandlerContext) {

        Channel channel = channelHandlerContext.channel();

        SocketUserSession userSession = new SocketUserSession(channel);
        userSession.cmdRegions = this.cmdRegions;

        // channel 中也保存 UserSession 的引用
        channel.attr(SocketUserSessions.userSessionKey).set(userSession);

        UserChannelId userChannelId = userSession.getUserChannelId();
        this.userChannelIdMap.putIfAbsent(userChannelId, userSession);
        this.channelGroup.add(channel);

        this.settingDefault(userSession);

        return userSession;
    }

    @Override
    public SocketUserSession getUserSession(ChannelHandlerContext channelHandlerContext) {
        Channel channel = channelHandlerContext.channel();
        return channel.attr(userSessionKey).get();
    }

    @Override
    public boolean settingUserId(UserChannelId userChannelId, long userId) {

        SocketUserSession userSession = this.getUserSession(userChannelId);
        if (Objects.isNull(userSession)) {
            return false;
        }

        if (Boolean.FALSE.equals(userSession.isActive())) {
            removeUserSession(userSession);
            return false;
        }

        userSession.setUserId(userId);

        this.userIdMap.put(userId, userSession);

        // 上线通知
        if (userSession.isVerifyIdentity()) {
            this.userHookInto(userSession);
        }

        return true;
    }

    @Override
    public void removeUserSession(SocketUserSession userSession) {

        if (Objects.isNull(userSession)) {
            return;
        }

        if (userSession.getState() == UserSessionState.DEAD) {
            return;
        }

        UserChannelId userChannelId = userSession.getUserChannelId();
        long userId = userSession.getUserId();

        Channel channel = userSession.getChannel();
        this.userIdMap.remove(userId);
        this.userChannelIdMap.remove(userChannelId);
        this.channelGroup.remove(channel);

        if (userSession.getState() == UserSessionState.ACTIVE && userSession.isVerifyIdentity()) {
            userSession.setState(UserSessionState.DEAD);
            this.userHookQuit(userSession);
        }

        // 关闭用户的连接
        channel.close();
    }

    @Override
    public int countOnline() {
        return this.channelGroup.size();
    }

    @Override
    public void broadcast(Object msg) {
        this.channelGroup.writeAndFlush(msg);
    }
}
