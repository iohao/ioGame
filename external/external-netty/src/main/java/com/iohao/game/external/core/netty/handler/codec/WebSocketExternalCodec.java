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
package com.iohao.game.external.core.netty.handler.codec;

import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.action.skeleton.protocol.BarMessage;
import com.iohao.game.external.core.message.ExternalCodecKit;
import com.iohao.game.external.core.message.ExternalMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.util.List;

/**
 * WebSocket 编解码器
 *
 * @author 渔民小镇
 * @date 2023-02-21
 */
public class WebSocketExternalCodec extends MessageToMessageCodec<BinaryWebSocketFrame, BarMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, BarMessage message, List<Object> out) {
        /*
         * 编码器 - 将消息发送到请求端（客户端）；【游戏对外服】发送消息给【游戏客户端】
         * ResponseMessage ---> ExternalMessage ---> 字节数组
         */
        ExternalMessage externalMessage = ExternalCodecKit.convertExternalMessage(message);

        byte[] bytes = DataCodecKit.encode(externalMessage);

        // 使用默认 buffer 。如果没有做任何配置，通常默认实现为池化的 direct （直接内存，也称为堆外内存）
        ByteBuf byteBuf = ctx.alloc().buffer(bytes.length);
        byteBuf.writeBytes(bytes);

        BinaryWebSocketFrame socketFrame = new BinaryWebSocketFrame(byteBuf);
        out.add(socketFrame);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, BinaryWebSocketFrame binary, List<Object> out) {
        /*
         * 解码器 - 接收请求端的消息（客户端）；
         * 字节数组 ---> ExternalMessage ---> RequestMessage
         */
        ByteBuf contentBuf = binary.content();
        byte[] bytes = new byte[contentBuf.readableBytes()];
        contentBuf.readBytes(bytes);

        ExternalMessage externalMessage = DataCodecKit.decode(bytes, ExternalMessage.class);

        BarMessage message = ExternalCodecKit.convertRequestMessage(externalMessage);

        //【游戏对外服】接收【游戏客户端】的消息
        out.add(message);
    }
}
