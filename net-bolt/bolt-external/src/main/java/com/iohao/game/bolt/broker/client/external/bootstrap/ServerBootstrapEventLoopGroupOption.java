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
