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
package com.iohao.game.external.client.join;

import com.iohao.game.common.consts.IoGameLogName;
import com.iohao.game.common.kit.ExecutorKit;
import com.iohao.game.common.kit.InternalKit;
import com.iohao.game.common.kit.PresentKit;
import com.iohao.game.external.client.ClientConnectOption;
import com.iohao.game.external.client.InputCommandRegion;
import com.iohao.game.external.client.user.ClientUser;
import com.iohao.game.external.client.user.ClientUserChannel;
import com.iohao.game.external.client.user.ClientUsers;
import com.iohao.game.external.client.user.DefaultClientUser;
import com.iohao.game.external.core.config.ExternalGlobalConfig;
import com.iohao.game.external.core.config.ExternalJoinEnum;
import com.iohao.game.external.core.message.ExternalMessage;
import com.iohao.game.external.core.message.ExternalMessageCmdCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author 渔民小镇
 * @date 2023-07-04
 */
@Getter
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j(topic = IoGameLogName.CommonStdout)
public final class ClientRunOne {
    List<InputCommandRegion> inputCommandRegions;
    ClientUser clientUser;

    /** 服务器连接端口 */
    int connectPort = ExternalGlobalConfig.externalPort;
    String connectAddress = "127.0.0.1";
    String websocketPath = ExternalGlobalConfig.CoreOption.websocketPath;
    String websocketVerify = "";

    ExternalJoinEnum joinEnum = ExternalJoinEnum.WEBSOCKET;
    ClientConnectOption option;

    public void startup() {
        if (Objects.isNull(this.clientUser)) {
            this.clientUser = new DefaultClientUser();
        }

        Objects.requireNonNull(this.inputCommandRegions, "请设置需要发送的请求消息");

        ClientUsers.addClientUser(clientUser);
        clientUser.setInputCommandRegions(this.inputCommandRegions);

        this.inputCommandRegions.forEach(inputCommandRegion -> {
            inputCommandRegion.setClientUser(clientUser);
            inputCommandRegion.initInputCommand();
        });

        ClientConnectOption option = getOption();

        ClientConnect clientConnect = ClientConnects.getClientConnect(joinEnum);
        if (Objects.isNull(clientConnect)) {
            log.error("连接方式 {} 没有对应的实现类", joinEnum);
            return;
        }

        InternalKit.execute(() -> clientConnect.connect(option));

        try {
            log.info("启动成功");
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 启动定时器发送心跳
     * <pre>
     *     该方法建议只调用一次
     * </pre>
     *
     * @param idlePeriod 心跳周期（秒）
     * @return this
     */
    public ClientRunOne idle(int idlePeriod) {

        ExecutorKit.newSingleScheduled("idle").scheduleAtFixedRate(() -> {
            ExternalMessage externalMessage = new ExternalMessage();
            externalMessage.setCmdCode(ExternalMessageCmdCode.idle);

            ClientUserChannel clientUserChannel = clientUser.getClientUserChannel();
            clientUserChannel.writeAndFlush(externalMessage);

        }, 1, idlePeriod, TimeUnit.SECONDS);

        return this;
    }

    private ClientConnectOption getOption() {

        if (Objects.isNull(option)) {
            option = new ClientConnectOption();
        }

        PresentKit.ifNull(option.getWsUrl(), () -> {
            String wsUrl = String.format("ws://%s:%d%s%s", connectAddress, connectPort, websocketPath, websocketVerify);
            option.setWsUrl(wsUrl);
        });

        PresentKit.ifNull(option.getSocketAddress(), () -> {
            InetSocketAddress socketAddress = new InetSocketAddress(connectAddress, connectPort);
            option.setSocketAddress(socketAddress);
        });

        PresentKit.ifNull(option.getClientUser(), () -> option.setClientUser(clientUser));

        return option;
    }
}
