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
