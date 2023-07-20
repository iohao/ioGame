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
