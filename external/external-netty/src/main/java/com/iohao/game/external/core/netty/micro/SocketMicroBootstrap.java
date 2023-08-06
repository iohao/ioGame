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

import com.iohao.game.action.skeleton.toy.IoGameBanner;
import com.iohao.game.common.consts.IoGameLogName;
import com.iohao.game.common.kit.system.OsInfo;
import com.iohao.game.external.core.micro.MicroBootstrapFlow;
import com.iohao.game.external.core.netty.micro.auto.GroupChannelOption;
import com.iohao.game.external.core.netty.micro.auto.GroupChannelOptionForLinux;
import com.iohao.game.external.core.netty.micro.auto.GroupChannelOptionForMac;
import com.iohao.game.external.core.netty.micro.auto.GroupChannelOptionForOther;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

/**
 * 与真实玩家连接的服务器，处理 tcp、websocket 的 netty 服务器。
 * <p>
 * 开发者可以继承后重写部分方法，来满足业务需求
 *
 * @author 渔民小镇
 * @date 2023-05-27
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j(topic = IoGameLogName.ExternalTopic)
public final class SocketMicroBootstrap extends AbstractMicroBootstrap {
    @Override
    public void startup() {
        // 线程组相关
        GroupChannelOption groupChannelOption = createGroupChannelOption();
        EventLoopGroup bossGroup = groupChannelOption.bossGroup();
        EventLoopGroup workerGroup = groupChannelOption.workerGroup();
        Class<? extends ServerChannel> channelClass = groupChannelOption.channelClass();

        // netty 服务器
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(channelClass);

        // 开发者可以选择性的重写流程方法，来定制符合自身项目的业务
        MicroBootstrapFlow<ServerBootstrap> microBootstrapFlow = this.setting.getMicroBootstrapFlow();
        microBootstrapFlow.createFlow(bootstrap);

        // 真实玩家连接的端口
        final int externalCorePort = this.setting.getExternalCorePort();
        ChannelFuture channelFuture = bootstrap.bind(externalCorePort);

        try {
            IoGameBanner.render();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    protected GroupChannelOption createGroupChannelOption() {
        // 根据环境自动选择，开发者也可以重写此方法，做些自定义
        GroupChannelOption groupChannelOption;
        OsInfo osInfo = OsInfo.me();

        // 根据系统内核来优化
        if (osInfo.isLinux()) {
            // linux
            groupChannelOption = new GroupChannelOptionForLinux();
        } else if (osInfo.isMac()) {
            // mac
            groupChannelOption = new GroupChannelOptionForMac();
        } else {
            // other system
            groupChannelOption = new GroupChannelOptionForOther();
        }

        return groupChannelOption;
    }
}
