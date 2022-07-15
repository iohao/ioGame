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

import com.alipay.remoting.NamedThreadFactory;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;

import java.util.concurrent.ThreadFactory;

/**
 * Server Bootstrap 优化项
 *
 * @author 渔民小镇
 * @date 2022-01-09
 */
public interface ServerBootstrapEventLoopGroupOption {
    /**
     * EventLoopGroup bossGroup (连接处理组)
     *
     * @return EventLoopGroup
     */
    EventLoopGroup bossGroup();

    /**
     * EventLoopGroup workerGroup (业务处理组)
     *
     * @return EventLoopGroup
     */
    EventLoopGroup workerGroup();

    /**
     * channelClass
     *
     * @return channelClass
     */
    Class<? extends ServerChannel> channelClass();

    /**
     * 业务 ThreadFactory
     *
     * @return worker ThreadFactory
     */
    static ThreadFactory workerThreadFactory() {
        return new NamedThreadFactory(
                "iohao.com:external-netty-server-worker",
                true);
    }

    /**
     * 连接 ThreadFactory
     *
     * @return boss ThreadFactory
     */
    static ThreadFactory bossThreadFactory() {
        return new NamedThreadFactory(
                "iohao.com:external-netty-server-boss",
                false);
    }
}
