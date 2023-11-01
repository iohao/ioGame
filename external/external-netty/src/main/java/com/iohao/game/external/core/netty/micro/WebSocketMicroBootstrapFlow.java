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
import com.iohao.game.external.core.micro.PipelineContext;
import com.iohao.game.external.core.netty.handler.codec.WebSocketExternalCodec;
import com.iohao.game.external.core.netty.handler.ws.WebSocketVerifyHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolConfig;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;

import java.util.Objects;

/**
 * websocket 与真实玩家连接服务器的启动流程
 *
 * @author 渔民小镇
 * @date 2023-05-31
 */
public class WebSocketMicroBootstrapFlow extends SocketMicroBootstrapFlow {
    @Override
    public void option(ServerBootstrap bootstrap) {
        bootstrap
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

    @Override
    public void pipelineCodec(PipelineContext context) {
        // 添加 http 相关 handler
        this.httpHandler(context);

        // 建立连接前的验证 handler
        this.verifyHandler(context);

        // 添加 websocket 相关 handler
        this.websocketHandler(context);

        // websocket 编解码
        context.addLast("codec", new WebSocketExternalCodec());
    }

    private void verifyHandler(PipelineContext context) {
        WebSocketVerifyHandler verifyHandler = this.createVerifyHandler();
        if (Objects.nonNull(verifyHandler)) {
            context.addLast("WebSocketVerifyHandler", verifyHandler);
        }
    }

    protected WebSocketVerifyHandler createVerifyHandler() {
        // ws verify 验证; 参考 https://www.yuque.com/iohao/game/tb1126szmgfu6u55
        return null;
    }

    protected void websocketHandler(PipelineContext context) {
        // WebSocket 数据压缩
        context.addLast("compression", new WebSocketServerCompressionHandler());

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
                .websocketPath(ExternalGlobalConfig.CoreOption.websocketPath)
                // 默认数据包大小
                .maxFramePayloadLength(ExternalGlobalConfig.CoreOption.packageMaxSize)
                .checkStartsWith(true)
                // 开启 WebSocket 扩展
                .allowExtensions(true)
                .build();

        context.addLast("WebSocketServerProtocolHandler", new WebSocketServerProtocolHandler(config));
    }

    protected void httpHandler(PipelineContext context) {
        /*
         * 将请求和应答消息解码为HTTP消息
         * 将字节码解码为 HttpRequest,HttpContent和LastHttpContent.
         * 并将 HttpRequest, HttpContent和LastHttpContent 编码为字节码
         */
        context.addLast("http-codec", new HttpServerCodec());

        /*
         * 将一个 HttpMessage 和跟随它的多个 HttpContent 聚合为
         * 单个 FullHTTPRequest 或者 FullHTTPResponse(取决于它是被用来处理请求还是响应).
         *
         * 安装了这个之后, ChannelPipeline 中的下一个 ChannelHandler 将只会接收
         * 到完整的 Http 请求或响应
         */
        context.addLast("aggregator", new HttpObjectAggregator(65536));
    }
}