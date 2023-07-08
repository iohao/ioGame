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
package com.iohao.game.external.client.join;

import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.common.kit.ExecutorKit;
import com.iohao.game.common.kit.PresentKit;
import com.iohao.game.common.kit.log.IoGameLoggerFactory;
import com.iohao.game.external.client.ClientConnectOption;
import com.iohao.game.external.client.ClientMessageCreate;
import com.iohao.game.external.client.CreateBarSkeleton;
import com.iohao.game.external.core.config.ExternalGlobalConfig;
import com.iohao.game.external.core.config.ExternalJoinEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author 渔民小镇
 * @date 2023-07-04
 */
@Getter
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class ClientRunOne {
    static final Logger log = IoGameLoggerFactory.getLoggerCommonStdout();
    final ExecutorService executorService = ExecutorKit.newSingleThreadExecutor("client");
    CreateBarSkeleton createBarSkeleton;
    ClientMessageCreate clientMessageCreate;

    /** 服务器连接端口 */
    int connectPort = ExternalGlobalConfig.externalPort;
    String connectAddress = "127.0.0.1";
    String websocketPath = ExternalGlobalConfig.CoreOption.websocketPath;

    ExternalJoinEnum joinEnum = ExternalJoinEnum.WEBSOCKET;
    ClientConnectOption option;

    public void startup() {

        Objects.requireNonNull(this.clientMessageCreate, "请设置需要发送的消息");

        ClientConnectOption option = getOption();

        ClientConnect clientConnect = ClientConnects.getClientConnect(joinEnum);
        if (Objects.isNull(clientConnect)) {
            log.error("连接方式 {} 没有对应的实现类", joinEnum);
            return;
        }

        executorService.execute(() -> clientConnect.connect(option));

        try {
            log.info("启动成功");
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private ClientConnectOption getOption() {

        if (Objects.isNull(option)) {
            option = new ClientConnectOption();
        }

        PresentKit.ifNull(option.getWsUrl(), () -> {
            String wsUrl = String.format("ws://%s:%d%s", connectAddress, connectPort, websocketPath);
            option.setWsUrl(wsUrl);
        });

        PresentKit.ifNull(option.getSocketAddress(), () -> {
            InetSocketAddress socketAddress = new InetSocketAddress(connectAddress, connectPort);
            option.setSocketAddress(socketAddress);
        });

        PresentKit.ifNull(option.getBarSkeleton(), () -> {
            BarSkeleton barSkeleton;
            if (Objects.nonNull(createBarSkeleton)) {
                barSkeleton = this.createBarSkeleton.createBarSkeleton();
            } else {
                barSkeleton = BarSkeleton.newBuilder().build();
            }

            option.setBarSkeleton(barSkeleton);
        });

        PresentKit.ifNull(option.getClientMessageCreate(), () -> option.setClientMessageCreate(this.clientMessageCreate));

        return option;
    }
}
