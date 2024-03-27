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
package com.iohao.game.external.client.join;

import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.action.skeleton.protocol.BarMessage;
import com.iohao.game.external.core.message.ExternalCodecKit;
import com.iohao.game.external.core.message.ExternalMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * @author 渔民小镇
 * @date 2023-12-15
 */
public class ClientTcpExternalCodec extends MessageToMessageCodec<ByteBuf, BarMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, BarMessage message, List<Object> out) throws Exception {
        /*
         * 编码器 - 将消息发送到【游戏对外服】
         * ResponseMessage ---> ExternalMessage ---> 字节数组
         */
        var externalMessage = ExternalCodecKit.convertExternalMessage(message);
        byte[] bytes = DataCodecKit.encode(externalMessage);
        ByteBuf buffer = ctx.alloc().buffer(bytes.length + 4);
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
        out.add(buffer);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        /*
         * 解码器 - 接收【游戏对外服】的消息
         * 字节数组 ---> ExternalMessage ---> RequestMessage
         */
        // 消息
        byte[] msgBytes = new byte[msg.readInt()];
        msg.readBytes(msgBytes);

        ExternalMessage externalMessage = DataCodecKit.decode(msgBytes, ExternalMessage.class);
        BarMessage message = ExternalCodecKit.convertRequestMessage(externalMessage);
        //【游戏对外服】接收【游戏客户端】的消息
        out.add(message);
    }
}
