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

import com.iohao.game.external.core.netty.DefaultExternalCoreSetting;
import com.iohao.game.external.core.micro.PipelineContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;

/**
 * PipelineContext 主要用于包装 SocketChannel channel
 * <pre>
 *     目的是为了增强 Handler aware 能力
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-04-26
 */
public record DefaultPipelineContext(Channel channel, DefaultExternalCoreSetting setting)
        implements PipelineContext {

    @Override
    public void addFirst(String name, Object handler) {

        this.setting.aware(handler);

        if (handler instanceof ChannelHandler channelHandler) {
            ChannelPipeline pipeline = channel.pipeline();
            pipeline.addFirst(name, channelHandler);
        }
    }

    @Override
    public void addLast(String name, Object handler) {

        // aware 能力附加
        this.setting.aware(handler);

        if (handler instanceof ChannelHandler channelHandler) {
            ChannelPipeline pipeline = channel.pipeline();
            pipeline.addLast(name, channelHandler);
        }
    }

    @Override
    public void remove(String name) {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.remove(name);
    }
}
