/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License..
 */
package com.iohao.game.bolt.broker.client.external.bootstrap.initializer;

import com.iohao.game.bolt.broker.client.external.bootstrap.ExternalChannelInitializerCallback;
import com.iohao.game.bolt.broker.client.external.bootstrap.handler.codec.ExternalCodecSocket;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * ChannelPipeline 初始化 for tcp
 * <pre>
 *     给初始化的 channel 编排 handler
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-01-09
 */
public class ExternalChannelInitializerCallbackTcp extends ChannelInitializer<SocketChannel> implements ExternalChannelInitializerCallback {
    ExternalChannelInitializerCallbackOption option;

    @Override
    public void initChannelPipeline(SocketChannel ch) {

        // 编排网关业务
        ChannelPipeline pipeline = ch.pipeline();

        // 数据包长度 = 长度域的值 + lengthFieldOffset + lengthFieldLength + lengthAdjustment。
        pipeline.addLast(new LengthFieldBasedFrameDecoder(option.packageMaxSize,
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
        pipeline.addLast("codec", new ExternalCodecSocket());

        // 添加其他 handler 到 pipeline 中 (业务编排)
        option.channelHandler(pipeline);
    }

    @Override
    public ExternalChannelInitializerCallback setOption(ExternalChannelInitializerCallbackOption option) {
        this.option = option;
        return this;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        this.initChannelPipeline(ch);
    }

    @Override
    public ServerBootstrapSetting createServerBootstrapSetting() {
        return serverBootstrap -> serverBootstrap.handler(new LoggingHandler(LogLevel.INFO))
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
}
