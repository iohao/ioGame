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
package com.iohao.game.bolt.broker.client.external.bootstrap;

import com.iohao.game.bolt.broker.client.external.bootstrap.initializer.ExternalChannelInitializerCallbackOption;
import com.iohao.game.bolt.broker.client.external.bootstrap.initializer.ServerBootstrapSetting;
import io.netty.channel.socket.SocketChannel;

/**
 * Channel 初始化的业务编排 (自定义业务编排)
 *
 * @author 渔民小镇
 * @date 2022-01-09
 */
public interface ExternalChannelInitializerCallback {
    /**
     * 编排业务
     *
     * @param ch ch
     */
    void initChannelPipeline(SocketChannel ch);

    /**
     * 设置选项
     *
     * @param option option
     * @return me
     */
    ExternalChannelInitializerCallback setOption(ExternalChannelInitializerCallbackOption option);

    /**
     * ServerBootstrap
     *
     * @return ServerBootstrap
     */
    ServerBootstrapSetting createServerBootstrapSetting();
}
