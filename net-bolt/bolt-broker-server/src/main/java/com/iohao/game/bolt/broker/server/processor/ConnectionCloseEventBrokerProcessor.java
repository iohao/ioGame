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
package com.iohao.game.bolt.broker.server.processor;

import com.alipay.remoting.Connection;
import com.alipay.remoting.ConnectionEventProcessor;
import com.alipay.remoting.ConnectionEventType;
import com.iohao.game.bolt.broker.core.aware.CmdRegionsAware;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.bolt.broker.core.message.BrokerClientModuleMessage;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.aware.BrokerClientModulesAware;
import com.iohao.game.bolt.broker.server.aware.BrokerServerAware;
import com.iohao.game.bolt.broker.server.balanced.BalancedManager;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientProxy;
import com.iohao.game.bolt.broker.server.kit.BrokerPrintKit;
import com.iohao.game.bolt.broker.server.service.BrokerClientModules;
import com.iohao.game.common.consts.IoGameLogName;
import com.iohao.game.core.common.cmd.CmdRegions;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;

/**
 * @author 渔民小镇
 * @date 2023-12-14
 */
@Setter
@Slf4j(topic = IoGameLogName.ConnectionTopic)
public final class ConnectionCloseEventBrokerProcessor implements ConnectionEventProcessor,
        BrokerServerAware, BrokerClientModulesAware, CmdRegionsAware {

    BrokerServer brokerServer;
    BrokerClientModules brokerClientModules;
    CmdRegions cmdRegions;

    @Override
    public void onEvent(String remoteAddress, Connection connection) {
        Objects.requireNonNull(connection);

        BalancedManager balancedManager = this.brokerServer.getBalancedManager();
        // 当前下线的逻辑服
        BrokerClientProxy brokerClientProxy = balancedManager.remove(remoteAddress);

        Optional.ofNullable(brokerClientProxy).ifPresent(proxy -> {
            if (IoGameGlobalConfig.openLog) {
                log.info("Broker ConnectionEventType:【{}】，remoteAddress:【{}】，brokerClientProxy:【{}】，Connection:【{}】",
                        ConnectionEventType.CLOSE, remoteAddress, brokerClientProxy, connection
                );

                BrokerPrintKit.print(this.brokerServer);
            }

            String id = proxy.getId();
            BrokerClientModuleMessage moduleMessage = this.brokerClientModules.removeById(id);

            // 在集群下，可能为 null，因为存在 127、192 的问题
            if (Objects.isNull(moduleMessage)) {
                return;
            }

            var context = new LineKit.Context(brokerServer, brokerClientModules, cmdRegions, moduleMessage);

            LineKit.offline(context);
        });
    }
}
