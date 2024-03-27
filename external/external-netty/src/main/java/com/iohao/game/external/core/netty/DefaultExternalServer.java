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
package com.iohao.game.external.core.netty;

import com.iohao.game.bolt.broker.client.BrokerClientApplication;
import com.iohao.game.bolt.broker.core.GroupWith;
import com.iohao.game.bolt.broker.core.client.BrokerAddress;
import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.external.core.ExternalCore;
import com.iohao.game.external.core.ExternalServer;
import com.iohao.game.external.core.broker.client.ExternalBrokerClientStartup;
import com.iohao.game.external.core.micro.MicroBootstrap;
import com.iohao.game.external.core.micro.join.ExternalJoinSelector;
import com.iohao.game.external.core.micro.join.ExternalJoinSelectors;

import java.util.Objects;
import java.util.ServiceLoader;

/**
 * 游戏对外服由两部分构成
 * <pre>
 *     1. 与真实玩家连接的 ExternalCore 服务器
 *     2. 与 Broker（游戏网关）通信的 BrokerClient ExternalBrokerClientStartup 服务器
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-02-19
 */
public final class DefaultExternalServer implements ExternalServer, GroupWith {
    /** 与真实玩家连接的 ExternalCore 服务器 */
    ExternalCore externalCore;
    /** ExternalCore 的一些设置 */
    DefaultExternalCoreSetting setting;

    /** 与 Broker（游戏网关）通信的 BrokerClient */
    ExternalBrokerClientStartup externalBrokerClientStartup;
    /** 连接 broker （游戏网关） 的地址 */
    BrokerAddress brokerAddress;
    int withNo;

    static {
        ServiceLoader.load(ExternalJoinSelector.class).forEach(ExternalJoinSelectors::putIfAbsent);
    }

    DefaultExternalServer(DefaultExternalCoreSetting setting
            , ExternalCore externalCore
            , BrokerAddress brokerAddress
            , ExternalBrokerClientStartup externalBrokerClientStartup
    ) {
        this.setting = setting;
        this.brokerAddress = brokerAddress;
        this.externalCore = externalCore;
        this.externalBrokerClientStartup = externalBrokerClientStartup;
    }

    @Override
    public void startup() {
        // 创建与真实玩家通信的 netty 服务器
        MicroBootstrap microBootstrap = this.externalCore.createMicroBootstrap();

        var startExternalBrokerClient = System.getProperty("ExternalBrokerClientStartup", "true");
        if (Boolean.parseBoolean(startExternalBrokerClient)) {
            // 启动与 Broker 游戏网关通信的 BrokerClient
            startExternalBrokerClient();
        }

        this.setting.inject();

        // 启动与真实玩家通信的 netty 服务器
        microBootstrap.startup();
    }

    private void startExternalBrokerClient() {
        this.externalBrokerClientStartup.setWithNo(this.withNo);
        // 与 Broker 游戏网关通信的 BrokerClient
        var brokerClientBuilder = BrokerClientApplication.initConfig(this.externalBrokerClientStartup);
        // aware 注入扩展
        brokerClientBuilder.awareInject(setting);

        // 与 broker 游戏网关通信的服务器地址
        if (Objects.nonNull(this.brokerAddress)) {
            brokerClientBuilder.brokerAddress(this.brokerAddress);
        }

        // 启动与 Broker 游戏网关通信的 BrokerClient
        BrokerClient brokerClient = BrokerClientApplication.start(brokerClientBuilder);

        this.externalBrokerClientStartup.startupSuccess(brokerClient);

        this.setting.setBrokerClient(brokerClient);
    }

    public static DefaultExternalServerBuilder newBuilder(int externalCorePort) {
        return new DefaultExternalServerBuilder(externalCorePort);
    }

    @Override
    public void setWithNo(int withNo) {
        this.withNo = withNo;
    }
}
