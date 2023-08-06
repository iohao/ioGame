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
package com.iohao.game.bolt.broker.server.processor.connection;

import com.alipay.remoting.Connection;
import com.alipay.remoting.ConnectionEventProcessor;
import com.alipay.remoting.ConnectionEventType;
import com.alipay.remoting.exception.RemotingException;
import com.iohao.game.bolt.broker.core.aware.CmdRegionsAware;
import com.iohao.game.bolt.broker.core.client.BrokerClientType;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.bolt.broker.core.message.BrokerClientModuleMessage;
import com.iohao.game.bolt.broker.core.message.BrokerClientModuleMessageOffline;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.aware.BrokerClientModulesAware;
import com.iohao.game.bolt.broker.server.aware.BrokerServerAware;
import com.iohao.game.bolt.broker.server.balanced.BalancedManager;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientProxy;
import com.iohao.game.bolt.broker.server.kit.BrokerPrintKit;
import com.iohao.game.bolt.broker.server.service.BrokerClientModules;
import com.iohao.game.common.consts.IoGameLogName;
import com.iohao.game.core.common.cmd.BrokerClientId;
import com.iohao.game.core.common.cmd.CmdRegions;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @author 渔民小镇
 * @date 2022-05-14
 */
@Slf4j(topic = IoGameLogName.ConnectionTopic)
public class CloseConnectionEventBrokerProcessor implements ConnectionEventProcessor,
        BrokerServerAware, BrokerClientModulesAware, CmdRegionsAware {
    private final AtomicInteger disConnectTimes = new AtomicInteger();
    private final AtomicBoolean dicConnected = new AtomicBoolean();
    BrokerServer brokerServer;
    BrokerClientModules brokerClientModules;
    CmdRegions cmdRegions;


    @Override
    public void onEvent(String remoteAddress, Connection conn) {

        if (IoGameGlobalConfig.openLog) {
            log.info("Broker ConnectionEventType:【{}】 remoteAddress:【{}】，Connection:【{}】",
                    ConnectionEventType.CLOSE, remoteAddress, conn
            );
        }

        Objects.requireNonNull(conn);
        dicConnected.set(true);
        disConnectTimes.incrementAndGet();

        BalancedManager balancedManager = this.brokerServer.getBalancedManager();
        BrokerClientProxy brokerClientProxy = balancedManager.remove(remoteAddress);

        extractedPrint(remoteAddress, brokerClientProxy);
        BrokerPrintKit.print(this.brokerServer);

        Optional.ofNullable(brokerClientProxy).ifPresent(proxy -> {
            String id = proxy.getId();
            BrokerClientModuleMessage moduleMessage = this.brokerClientModules.removeById(id);

            // 在集群下，可能为 null，因为存在 127、192 的问题
            if (Objects.isNull(moduleMessage)) {
                return;
            }

            BrokerClientType brokerClientType = moduleMessage.getBrokerClientType();
            if (brokerClientType != BrokerClientType.LOGIC) {
                return;
            }

            unLoading(moduleMessage);

            Consumer<BrokerClientProxy> consumer = externalProxy -> {
                BrokerClientModuleMessageOffline offline = new BrokerClientModuleMessageOffline();
                offline.setBrokerClientModuleMessage(moduleMessage);

                try {
                    externalProxy.oneway(offline);
                } catch (RemotingException | InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
            };

            // 通知所有的游戏对外服，有游戏逻辑服下线了
            this.brokerServer.getBalancedManager()
                    .getExternalLoadBalanced()
                    .listBrokerClientProxy()
                    .forEach(consumer);
        });

    }

    private static void extractedPrint(String remoteAddress, BrokerClientProxy brokerClientProxy) {
        if (IoGameGlobalConfig.openLog) {
            log.info("Broker ConnectionEventType:【{}】 remoteAddress:【{}】，brokerClientProxy:【{}】",
                    ConnectionEventType.CLOSE, remoteAddress, brokerClientProxy
            );
        }
    }

    private void unLoading(BrokerClientModuleMessage moduleMessage) {
        String id = moduleMessage.getId();
        int idHash = moduleMessage.getIdHash();
        BrokerClientId brokerClientId = new BrokerClientId(idHash, id);
        // 游戏逻辑服的路由数据
        this.cmdRegions.unLoading(brokerClientId);
    }

    public boolean isDisConnected() {
        return this.dicConnected.get();
    }

    public int getDisConnectTimes() {
        return this.disConnectTimes.get();
    }

    public void reset() {
        this.disConnectTimes.set(0);
        this.dicConnected.set(false);
    }

    @Override
    public void setBrokerServer(BrokerServer brokerServer) {
        this.brokerServer = brokerServer;
    }

    @Override
    public void setBrokerClientModules(BrokerClientModules brokerClientModules) {
        this.brokerClientModules = brokerClientModules;
    }

    @Override
    public void setCmdRegions(CmdRegions cmdRegions) {
        this.cmdRegions = cmdRegions;
    }
}
