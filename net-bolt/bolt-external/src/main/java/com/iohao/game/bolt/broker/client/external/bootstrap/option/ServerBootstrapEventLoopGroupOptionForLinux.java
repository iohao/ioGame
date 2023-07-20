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
package com.iohao.game.bolt.broker.client.external.bootstrap.option;

import com.iohao.game.bolt.broker.client.external.bootstrap.ServerBootstrapEventLoopGroupOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;

/**
 * 服务端 for linux nio 处理类
 *
 * @author 渔民小镇
 * @date 2022-01-09
 */
public class ServerBootstrapEventLoopGroupOptionForLinux implements ServerBootstrapEventLoopGroupOption {
    @Override
    public EventLoopGroup bossGroup() {
        return new EpollEventLoopGroup(
                1,
                ServerBootstrapEventLoopGroupOption.bossThreadFactory()
        );
    }

    @Override
    public EventLoopGroup workerGroup() {

        int availableProcessors = Runtime.getRuntime().availableProcessors() << 1;

        return new EpollEventLoopGroup(
                availableProcessors,
                ServerBootstrapEventLoopGroupOption.workerThreadFactory()
        );
    }

    @Override
    public Class<? extends ServerChannel> channelClass() {
        return EpollServerSocketChannel.class;
    }
}
