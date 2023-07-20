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

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import com.iohao.game.bolt.broker.client.external.bootstrap.*;

/**
 * netty 编排业务
 * <pre>
 *     在调用 hook 前，会经过 {@link ExternalChannelInitializerCallback#initChannelPipeline(SocketChannel)} 。
 *
 *     ExternalChannelInitializerCallback 接口的实现类有
 *     {@link ExternalChannelInitializerCallbackWebsocket}
 *     {@link ExternalChannelInitializerCallbackTcp}
 *
 *     这些实现类中，会给 ChannelPipeline 添加上一些默认的处理器，通常是编解码相关的。
 *
 *     开发者通过这个钩子接口，可以自定义 netty 处理器的编排。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-12-05
 */
public interface ChannelPipelineHook {
    /**
     * 自定义 - 编排业务
     *
     * @param pipeline
     */
    void initChannelPipeline(ChannelPipeline pipeline);
}
