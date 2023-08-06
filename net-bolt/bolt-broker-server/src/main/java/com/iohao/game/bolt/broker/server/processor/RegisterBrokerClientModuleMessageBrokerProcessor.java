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
package com.iohao.game.bolt.broker.server.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcServer;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.iohao.game.bolt.broker.cluster.BrokerClusterManager;
import com.iohao.game.bolt.broker.cluster.BrokerRunModeEnum;
import com.iohao.game.bolt.broker.core.aware.CmdRegionsAware;
import com.iohao.game.bolt.broker.core.client.BrokerClientType;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.bolt.broker.core.message.BrokerClientModuleMessage;
import com.iohao.game.bolt.broker.core.message.BrokerClusterMessage;
import com.iohao.game.bolt.broker.core.message.BrokerMessage;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.aware.BrokerClientModulesAware;
import com.iohao.game.bolt.broker.server.aware.BrokerServerAware;
import com.iohao.game.bolt.broker.server.balanced.BalancedManager;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientProxy;
import com.iohao.game.bolt.broker.server.kit.BrokerPrintKit;
import com.iohao.game.bolt.broker.server.service.BrokerClientModules;
import com.iohao.game.common.kit.ExecutorKit;
import com.iohao.game.common.consts.IoGameLogName;
import com.iohao.game.core.common.cmd.CmdRegions;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 模块注册
 *
 * @author 渔民小镇
 * @date 2022-05-14
 */
@Slf4j(topic = IoGameLogName.CommonStdout)
public class RegisterBrokerClientModuleMessageBrokerProcessor extends AsyncUserProcessor<BrokerClientModuleMessage>
        implements BrokerServerAware, BrokerClientModulesAware, CmdRegionsAware {
    BrokerServer brokerServer;
    BrokerClientModules brokerClientModules;
    CmdRegions cmdRegions;
    AtomicBoolean fixedRateFlag = new AtomicBoolean(false);

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, BrokerClientModuleMessage brokerClientModuleMessage) {
        brokerClientModules.add(brokerClientModuleMessage);

        String remoteAddress = bizCtx.getRemoteAddress();
        brokerClientModuleMessage.setAddress(remoteAddress);

        // 注册到负载器中
        BalancedManager balancedManager = brokerServer.getBalancedManager();
        balancedManager.register(brokerClientModuleMessage);

        // 发送网关集群消息给客户端 （逻辑服）
        if (brokerServer.getBrokerRunMode() == BrokerRunModeEnum.CLUSTER) {
            this.sendClusterMessage(bizCtx);

            this.printCluster();
        }

        print(brokerClientModuleMessage);

        if (IoGameGlobalConfig.isSendBrokerClientModuleMessage()) {
            this.sendBrokerClientModuleMessage(brokerClientModuleMessage);
        }
    }

    private void sendBrokerClientModuleMessage(BrokerClientModuleMessage moduleMessage) {

        BrokerClientType brokerClientType = moduleMessage.getBrokerClientType();

        if (brokerClientType == BrokerClientType.LOGIC) {
            extractedLogic(moduleMessage);
        }

        if (brokerClientType == BrokerClientType.EXTERNAL) {
            extractedExternal(moduleMessage);
        }
    }

    private void extractedExternal(BrokerClientModuleMessage moduleMessage) {
        // 将所有游戏逻辑服的信息发送给当前游戏对外服
        String address = moduleMessage.getAddress();
        RpcServer rpcServer = this.brokerServer.getRpcServer();

        Consumer<BrokerClientModuleMessage> consumer = message -> {
            try {
                // 将【游戏逻辑服】的模块信息发送给【游戏对外服】
                rpcServer.oneway(address, message);
            } catch (RemotingException | InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        };

        this.brokerClientModules.listBrokerClientModuleMessage()
                .stream()
                .filter(message -> message.getBrokerClientType() == BrokerClientType.LOGIC)
                .forEach(consumer);
    }

    private void extractedLogic(BrokerClientModuleMessage moduleMessage) {
        cmdRegions.loading(moduleMessage);

        // 将当前游戏逻辑服的信息，发送给所有的游戏对外服
        Consumer<BrokerClientProxy> consumer = proxy -> {
            try {
                // 将【游戏逻辑服】的模块信息发送给【游戏对外服】
                proxy.oneway(moduleMessage);
            } catch (RemotingException | InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        };

        this.brokerServer
                .getBalancedManager()
                .getExternalLoadBalanced()
                .listBrokerClientProxy()
                .forEach(consumer);
    }

    private void printCluster(BrokerClusterMessage brokerClusterMessage) {
        if (brokerServer.getBrokerRunMode() != BrokerRunModeEnum.CLUSTER) {
            return;
        }

        if (IoGameGlobalConfig.isBrokerClusterLog()) {
            String message = brokerClusterMessage
                    .getBrokerMessageList()
                    .stream()
                    .map(BrokerMessage::toString)
                    .collect(Collectors.joining("\n"));

            log.info("\n游戏网关端口: [{}] --  集群数量[{}] - 详细：\n[{}]"
                    , this.brokerServer.getPort()
                    , brokerClusterMessage.count()
                    , message);
        }
    }

    private void sendClusterMessage(BizContext bizCtx) {

        BrokerClusterManager brokerClusterManager = brokerServer.getBrokerClusterManager();
        BrokerClusterMessage brokerClusterMessage = brokerClusterManager.getBrokerClusterMessage();

        this.printCluster(brokerClusterMessage);

        try {
            this.brokerServer.getRpcServer().oneway(bizCtx.getConnection(), brokerClusterMessage);
        } catch (RemotingException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void print(BrokerClientModuleMessage brokerClientModuleMessage) {

        int port = this.brokerServer.getPort();
        if (IoGameGlobalConfig.openLog) {
            log.info("模块注册信息 --- 网关port: [{}] --- {}", port, brokerClientModuleMessage);
        }

        // print
        BrokerPrintKit.print(this.brokerServer);
    }

    private void printCluster() {
        if (brokerServer.getBrokerRunMode() != BrokerRunModeEnum.CLUSTER) {
            return;
        }

        if (!IoGameGlobalConfig.isBrokerClusterFixedRateLog()) {
            return;
        }

        if (fixedRateFlag.compareAndSet(false, true)) {
            ExecutorKit.newSingleScheduled("print").scheduleAtFixedRate(() -> {
                BrokerPrintKit.print(this.brokerServer);

                BrokerClusterManager brokerClusterManager = brokerServer.getBrokerClusterManager();
                BrokerClusterMessage brokerClusterMessage = brokerClusterManager.getBrokerClusterMessage();

                this.printCluster(brokerClusterMessage);

            }, 5, 30, TimeUnit.SECONDS);
        }
    }

    @Override
    public String interest() {
        return BrokerClientModuleMessage.class.getName();
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
