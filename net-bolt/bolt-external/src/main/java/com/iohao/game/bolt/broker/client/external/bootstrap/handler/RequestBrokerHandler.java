/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
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
package com.iohao.game.bolt.broker.client.external.bootstrap.handler;

import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.bolt.broker.client.external.bootstrap.ExternalKit;
import com.iohao.game.bolt.broker.client.external.bootstrap.message.ExternalMessage;
import com.iohao.game.common.kit.log.IoGameLoggerFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;

/**
 * 负责把游戏端的请求 转发给 Broker（游戏网关）
 * <pre>
 *     实际上是在游戏对外服请求 Broker
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-02-14
 */
@ChannelHandler.Sharable
public final class RequestBrokerHandler extends SimpleChannelInboundHandler<ExternalMessage> {
    static final Logger log = IoGameLoggerFactory.getLoggerCommon();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ExternalMessage message) {
        // 将 message 转换成 RequestMessage
        RequestMessage requestMessage = ExternalKit.convertRequestMessage(message);

        try {
            // 由内部逻辑服转发用户请求到游戏网关，在由网关转到具体的业务逻辑服
            ExternalKit.requestGateway(ctx, requestMessage);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
