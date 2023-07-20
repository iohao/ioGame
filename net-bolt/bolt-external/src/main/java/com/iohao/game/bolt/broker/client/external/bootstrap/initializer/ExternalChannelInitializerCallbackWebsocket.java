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
package com.iohao.game.bolt.broker.client.external.bootstrap.initializer;

import com.iohao.game.bolt.broker.client.external.bootstrap.ExternalChannelInitializerCallback;
import com.iohao.game.bolt.broker.client.external.bootstrap.handler.codec.ExternalCodecWebsocket;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolConfig;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;

/**
 * ChannelPipeline 初始化 for Websocket
 * <pre>
 *     给初始化的 channel 编排 handler
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-01-13
 */
public class ExternalChannelInitializerCallbackWebsocket extends ChannelInitializer<SocketChannel> implements ExternalChannelInitializerCallback {
    ExternalChannelInitializerCallbackOption option;

    @Override
    public void initChannelPipeline(SocketChannel ch) {
        // 业务编排
        ChannelPipeline pipeline = ch.pipeline();

        /*
         * 将请求和应答消息解码为HTTP消息
         * 将字节码解码为 HttpRequest,HttpContent和LastHttpContent.
         * 并将 HttpRequest, HttpContent和LastHttpContent 编码为字节码
         */
        pipeline.addLast("http-codec", new HttpServerCodec());

        /*
         * 将一个 HttpMessage 和跟随它的多个 HttpContent 聚合为
         * 单个 FullHTTPRequest 或者 FullHTTPResponse(取决于它是被用来处理请求还是响应).
         *
         * 安装了这个之后, ChannelPipeline 中的下一个 ChannelHandler 将只会接收
         * 到完整的 Http 请求或响应
         */
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));

        // WebSocket 数据压缩
        pipeline.addLast("compression", new WebSocketServerCompressionHandler());

        /*
         * 处理 websocket 的解码器
         * 1 协议包长度限制
         * 2 验证协议url。
         *
         * 按照 WebSocket 规范的要求, 处理 :
         * 1 WebSocket 升级握手, 验证GET的请求升级
         * 2 PingWebSocketFrame
         * 3 PongWebSocketFrame
         * 4 CloseWebSocketFrame
         *
         * 移除 HTTPRequest HTTPResponse
         */
        WebSocketServerProtocolConfig config = WebSocketServerProtocolConfig.newBuilder()
                // 验证 URL
                .websocketPath(option.websocketPath)
                // 默认数据包大小
                .maxFramePayloadLength(option.packageMaxSize)
                .checkStartsWith(true)
                // 开启 WebSocket 扩展
                .allowExtensions(true)
                .build();

        pipeline.addLast("WebSocketServerProtocolHandler", new WebSocketServerProtocolHandler(config));

        // websocket 编解码
        pipeline.addLast("codec", new ExternalCodecWebsocket());

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
        return serverBootstrap -> serverBootstrap
                // 客户端保持活动连接
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                /*
                 * BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，
                 * 用于临时存放已完成三次握手的请求的队列的最大长度。如果未设置或所设置的值小于1，
                 * 使用默认值100
                 */
                .option(ChannelOption.SO_BACKLOG, 100)

                .childOption(ChannelOption.SO_REUSEADDR, true);
    }
}
