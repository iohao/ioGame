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

import java.util.List;

/**
 * Tcp 编解码器
 *
 * @author 渔民小镇
 * @date 2023-02-21
 */
public final class TcpExternalCodec extends MessageToMessageCodec<ByteBuf, BarMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, BarMessage message, List<Object> out) {
        /*
         * 编码器 - 【游戏对外服】发送消息给【游戏客户端、请求端】
         * ResponseMessage ---> ExternalMessage ---> 字节数组
         */
        ExternalMessage externalMessage = ExternalCodecKit.convertExternalMessage(message);

        byte[] bytes = DataCodecKit.encode(externalMessage);

        /*
         * 使用默认 buffer 。如果没有做任何配置，通常默认实现为池化的 direct （直接内存，也称为堆外内存）
         * 优点：使用的系统内存，读写效率高（少一次拷贝），且不受 GC 影响
         * 缺点：分配效率低
         */
        ByteBuf buffer = ctx.alloc().buffer(bytes.length + 4);
        // 消息长度
        buffer.writeInt(bytes.length);
        // 消息
        buffer.writeBytes(bytes);

        out.add(buffer);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        /*
         * 解码器 - 接收【游戏客户端、请求端】的消息
         * 字节数组 ---> ExternalMessage ---> RequestMessage
         */
        // 读取消息长度
        int length = msg.readInt();
        // 消息
        byte[] msgBytes = new byte[length];
        msg.readBytes(msgBytes);

        ExternalMessage externalMessage = DataCodecKit.decode(msgBytes, ExternalMessage.class);

        BarMessage message = ExternalCodecKit.convertRequestMessage(externalMessage);

        //【游戏对外服】接收【游戏客户端】的消息
        out.add(message);
    }
}
