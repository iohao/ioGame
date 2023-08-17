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
import com.iohao.game.external.core.session.UserSessionOption;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * 长连接的 UserSession
 * <pre>
 *     tcp、websocket
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-02-18
 */
public final class SocketUserSession extends AbstractUserSession {

    public SocketUserSession(Channel channel) {
        this.channel = channel;
        // asLongText 使用空间换时间的策略；因为在登录后 channelId 将不会用于传输
        String channelId = this.channel.id().asLongText();
        this.userChannelId = new UserChannelId(channelId);
    }

    @Override
    public boolean isActive() {
        return Objects.nonNull(this.channel) && this.channel.isActive();
    }

    @Override
    @SuppressWarnings("unchecked")
    public ChannelFuture writeAndFlush(Object message) {
        return this.channel.writeAndFlush(message);
    }

    @Override
    public String getIp() {

        // 优先拿玩家真实 ip
        String realIp = this.option(UserSessionOption.realIp);

        if (realIp.isEmpty()) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) channel.remoteAddress();
            return inetSocketAddress.getHostString();
        }

        return realIp;
    }
}
