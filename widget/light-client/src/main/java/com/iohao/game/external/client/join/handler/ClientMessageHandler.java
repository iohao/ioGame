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
package com.iohao.game.external.client.join.handler;

import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.external.client.user.ClientUser;
import com.iohao.game.external.client.user.ClientUserChannel;
import com.iohao.game.external.core.message.ExternalMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author 渔民小镇
 * @date 2023-05-31
 */
@ChannelHandler.Sharable
public class ClientMessageHandler extends SimpleChannelInboundHandler<ExternalMessage> {
    final BarSkeleton barSkeleton;

    final ClientUserChannel clientUserChannel;

    public ClientMessageHandler(BarSkeleton barSkeleton, ClientUser clientUser) {
        this.barSkeleton = barSkeleton;
        this.clientUserChannel = clientUser.getClientUserChannel();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ExternalMessage externalMessage) {
        clientUserChannel.read(externalMessage, barSkeleton);
    }
}