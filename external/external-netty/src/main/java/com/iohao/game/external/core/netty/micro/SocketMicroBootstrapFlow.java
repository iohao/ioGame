/*
 * ioGame
 * Copyright (C) 2021 - present  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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

import com.iohao.game.external.core.config.ExternalGlobalConfig;
import com.iohao.game.external.core.hook.internal.IdleProcessSetting;
import com.iohao.game.external.core.micro.PipelineContext;
import com.iohao.game.external.core.netty.SettingOption;
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

        SocketIdleHandler socketIdleHandler = setting.option(SettingOption.socketIdleHandler);

        // 心跳响应、心跳钩子 Handler
        context.addLast("idleHandler", socketIdleHandler);
    }

    @Override
    public void pipelineCustom(PipelineContext context) {
        // 日志打印（异常时）
        if (ExternalGlobalConfig.enableLoggerHandler) {
            context.addLast("SimpleLoggerHandler", SimpleLoggerHandler.me());
        }

        // 路由存在检测
        context.addLast("CmdCheckHandler", CmdCheckHandler.me());

        // 管理 UserSession 的 Handler
        SocketUserSessionHandler socketUserSessionHandler = setting.option(SettingOption.socketUserSessionHandler);
        context.addLast("UserSessionHandler", socketUserSessionHandler);

        // 路由访问验证 的 Handler
        SocketCmdAccessAuthHandler socketCmdAccessAuthHandler = setting.option(SettingOption.socketCmdAccessAuthHandler);
        context.addLast("CmdAccessAuthHandler", socketCmdAccessAuthHandler);

        // 游戏对外服路由数据缓存
        if (Objects.nonNull(ExternalGlobalConfig.externalCmdCache)) {
            context.addLast("CmdCacheHandler", CmdCacheHandler.me());
        }

        // 负责把游戏端的请求转发给 Broker（游戏网关）的 Handler
        SocketRequestBrokerHandler socketRequestBrokerHandler = setting.option(SettingOption.socketRequestBrokerHandler);
        context.addLast("RequestBrokerHandler", socketRequestBrokerHandler);
    }
}
