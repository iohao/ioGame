/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.iohao.game.external.core.netty.micro;

import com.iohao.game.external.core.hook.internal.IdleProcessSetting;
import com.iohao.game.external.core.micro.PipelineContext;
import com.iohao.game.external.core.netty.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

/**
 * @author 渔民小镇
 * @date 2023-05-31
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
abstract class SocketMicroBootstrapFlow extends AbstractMicroBootstrapFlow<ServerBootstrap> {
    @Override
    public void channelInitializer(ServerBootstrap bootstrap) {
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                DefaultPipelineContext pipelineContext = new DefaultPipelineContext(ch, setting);
                /*
                 * 新建连接时的执行流程
                 * 通常情况下，我们可以将 ChannelInitializer 内的实现划分为三部分
                 *  1. pipelineCodec：编解码
                 *  2. pipelineIdle：心跳相关
                 *  3. pipelineCustom：自定义的业务编排 （大部分情况下只需要重写 pipelineCustom 就可以达到很强的扩展了）
                 */
                pipelineFlow(pipelineContext);
            }
        });
    }

    @Override
    public void pipelineIdle(PipelineContext context) {
        IdleProcessSetting idleProcessSetting = this.setting.getIdleProcessSetting();
        if (Objects.isNull(idleProcessSetting)) {
            return;
        }

        // netty 心跳检测
        context.addLast("idleStateHandler", new IdleStateHandler(
                idleProcessSetting.getReaderIdleTime(),
                idleProcessSetting.getWriterIdleTime(),
                idleProcessSetting.getAllIdleTime(),
                idleProcessSetting.getTimeUnit())
        );

        // 心跳响应、心跳钩子 Handler
        SocketIdleHandler idleHandler = new SocketIdleHandler(setting);
        context.addLast("idleHandler", idleHandler);
    }

    @Override
    public void pipelineCustom(PipelineContext context) {
        // 路由存在检测
        context.addLast("CmdCheckHandler", new CmdCheckHandler());

        // 管理 UserSession 的 Handler
        context.addLast("UserSessionHandler", new SocketUserSessionHandler());

        // 路由访问验证 的 Handler
        context.addLast("CmdAccessAuthHandler", new SocketCmdAccessAuthHandler());

        // 负责把游戏端的请求转发给 Broker（游戏网关）的 Handler
        context.addLast("RequestBrokerHandler", new SocketRequestBrokerHandler());
    }
}
