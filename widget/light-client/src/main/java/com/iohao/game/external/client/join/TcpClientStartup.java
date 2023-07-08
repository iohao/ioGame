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
package com.iohao.game.external.client.join;

import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.common.kit.InternalKit;
import com.iohao.game.common.kit.log.IoGameLoggerFactory;
import com.iohao.game.external.client.ClientConnectOption;
import com.iohao.game.external.client.ClientMessageCreate;
import com.iohao.game.external.client.core.ClientCommands;
import com.iohao.game.external.client.join.handler.ClientMessageHandler;
import com.iohao.game.external.core.netty.handler.codec.TcpExternalCodec;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author 渔民小镇
 * @date 2023-07-05
 */
class TcpClientStartup implements ClientConnect {
    static final Logger log = IoGameLoggerFactory.getLoggerCommonStdout();

    static int PACKAGE_MAX_SIZE = 1024 * 1024;

    @Override
    public void connect(ClientConnectOption option) {

        ClientMessageCreate clientMessageCreate = option.getClientMessageCreate();

        BarSkeleton barSkeleton = option.getBarSkeleton();

        ClientMessageHandler clientMessageHandler = new ClientMessageHandler(barSkeleton);

        EventLoopGroup group = new NioEventLoopGroup();
        var bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        // 编排网关业务
                        ChannelPipeline pipeline = ch.pipeline();

                        // 数据包长度 = 长度域的值 + lengthFieldOffset + lengthFieldLength + lengthAdjustment。
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(PACKAGE_MAX_SIZE,
                                // 长度字段的偏移量， 从 0 开始
                                0,
                                // 字段的长度, 如果使用的是 short ，占用2位；（消息头用的 byteBuf.writeShort 来记录长度的）
                                // 字段的长度, 如果使用的是 int   ，占用4位；（消息头用的 byteBuf.writeInt   来记录长度的）
                                4,
                                // 要添加到长度字段值的补偿值：长度调整值 = 内容字段偏移量 - 长度字段偏移量 - 长度字段的字节数
                                0,
                                // 跳过的初始字节数： 跳过0位; (跳过消息头的 0 位长度)
                                0));

                        // 编解码
                        pipeline.addLast("codec", TcpExternalCodec.me());

                        pipeline.addLast(clientMessageHandler);
                    }
                });

        InetSocketAddress address = option.getSocketAddress();
        String hostName = address.getHostName();
        int port = address.getPort();
        final ChannelFuture channelFuture = bootstrap.connect(hostName, port);

        try {
            Channel channel = channelFuture.sync().channel();
            ClientCommands.clientChannel = channel::writeAndFlush;

            InternalKit.newTimeout(timeout -> {
                clientMessageCreate.requestMessagePipeline();
                ClientCommands.startup();
            }, 100, TimeUnit.MILLISECONDS);

            channel.closeFuture().await();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        } finally {
            group.shutdownGracefully();
        }
    }
}
