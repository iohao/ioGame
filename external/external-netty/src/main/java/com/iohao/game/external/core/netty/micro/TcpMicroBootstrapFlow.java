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
package com.iohao.game.external.core.netty.micro;

import com.iohao.game.external.core.config.ExternalGlobalConfig;
import com.iohao.game.external.core.netty.handler.codec.TcpExternalCodec;
import com.iohao.game.external.core.micro.PipelineContext;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * tcp 与真实玩家连接服务器的启动流程
 *
 * @author 渔民小镇
 * @date 2023-05-28
 */
public class TcpMicroBootstrapFlow extends SocketMicroBootstrapFlow {

    @Override
    public void option(ServerBootstrap bootstrap) {
        bootstrap
                // 客户端保持活动连接
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                /*
                 * 是否启用心跳保活机制。在双方TCP套接字建立连接后（即都进入ESTABLISHED状态）
                 * 并且在两个小时左右上层没有任何数据传输的情况下，这套机制才会被激活
                 */
                .option(ChannelOption.SO_KEEPALIVE, true)
                /*
                 * BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，
                 * 用于临时存放已完成三次握手的请求的队列的最大长度。如果未设置或所设置的值小于1，
                 * 使用默认值100
                 */
                .option(ChannelOption.SO_BACKLOG, 100)
                /*
                 * 在TCP/IP协议中，无论发送多少数据，总是要在数据前面加上协议头，
                 * 同时，对方接收到数据，也需要发送ACK表示确认。
                 * 为了尽可能的利用网络带宽，TCP总是希望尽可能的发送足够大的数据。
                 * 这里就涉及到一个名为Nagle的算法，该算法的目的就是为了尽可能发送大块数据，避免网络中充斥着许多小数据块。
                 */
                .option(ChannelOption.TCP_NODELAY, true);
    }

    @Override
    public void pipelineCodec(PipelineContext context) {
        // 数据包长度 = 长度域的值 + lengthFieldOffset + lengthFieldLength + lengthAdjustment。
        context.addLast(new LengthFieldBasedFrameDecoder(
                ExternalGlobalConfig.CoreOption.packageMaxSize,
                // 长度字段的偏移量， 从 0 开始
                0,
                // 字段的长度, 如果使用的是 short ，占用2位；（消息头用的 byteBuf.writeShort 来记录长度的）
                // 字段的长度, 如果使用的是 int   ，占用4位；（消息头用的 byteBuf.writeInt   来记录长度的）
                4,
                // 要添加到长度字段值的补偿值：长度调整值 = 内容字段偏移量 - 长度字段偏移量 - 长度字段的字节数
                0,
                // 跳过的初始字节数： 跳过0位; (跳过消息头的 0 位长度)
                0));

        // tcp socket 编解码
        context.addLast("codec", new TcpExternalCodec());
    }
}
