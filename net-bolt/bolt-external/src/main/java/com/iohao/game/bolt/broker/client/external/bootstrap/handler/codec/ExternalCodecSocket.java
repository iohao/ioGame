/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iohao.game.bolt.broker.client.external.bootstrap.handler.codec;

import com.iohao.game.common.kit.ProtoKit;
import com.iohao.game.bolt.broker.client.external.bootstrap.message.ExternalMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

/**
 * tcp socket  编解码
 *
 * @author 渔民小镇
 * @date 2022-04-13
 */
@Slf4j
@ChannelHandler.Sharable
public class ExternalCodecSocket extends MessageToMessageCodec<ByteBuf, ExternalMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ExternalMessage msg, List<Object> out) throws Exception {
        // 【对外服】 发送消息 给 游戏客户端
        if (Objects.isNull(msg)) {
            throw new Exception("The encode ExternalMessage is null");
        }

        // 编码器 - ExternalMessage ---> 字节数组
        byte[] bytes = ProtoKit.toBytes(msg);

        ByteBuf buffer = ctx.alloc().buffer();
        // 消息长度
        buffer.writeInt(bytes.length);
        // 消息
        buffer.writeBytes(bytes);

        out.add(buffer);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        // 解码器 - 字节数组 ---> ExternalMessage

        // 读取消息长度
        int length = msg.readInt();
        // 消息
        byte[] msgBytes = new byte[length];
        msg.readBytes(msgBytes);

        ExternalMessage message = ProtoKit.parseProtoByte(msgBytes, ExternalMessage.class);
        // 【对外服】 接收 游戏客户端的消息
        out.add(message);
    }
}
