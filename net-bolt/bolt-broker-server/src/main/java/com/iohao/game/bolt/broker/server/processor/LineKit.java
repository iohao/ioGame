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

import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcServer;
import com.iohao.game.bolt.broker.core.client.BrokerClientType;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.bolt.broker.core.message.BrokerClientModuleMessage;
import com.iohao.game.bolt.broker.core.message.BrokerClientOfflineMessage;
import com.iohao.game.bolt.broker.core.message.BrokerClientOnlineMessage;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.service.BrokerClientModules;
import com.iohao.game.common.kit.concurrent.executor.ExecutorRegionKit;
import com.iohao.game.core.common.cmd.BrokerClientId;
import com.iohao.game.core.common.cmd.CmdRegions;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author 渔民小镇
 * @date 2023-12-14
 */
@Slf4j
@UtilityClass
class LineKit {
    final long executorIndex = IoGameGlobalConfig.InternalConfig.executorIndex;

    record Context(BrokerServer brokerServer
            , BrokerClientModules brokerClientModules
            , CmdRegions cmdRegions
            , BrokerClientModuleMessage moduleMessage) {
    }

    void online(Context context) {
        // 避免并发，使用同一个执行器
        ExecutorRegionKit.getSimpleThreadExecutor(executorIndex).executeTry(() -> {
            // online logic
            internalOnlineNew(context);
        });
    }

    private void internalOnlineNew(Context context) {
        // 当前新上线的逻辑服
        BrokerClientModuleMessage moduleMessage = context.moduleMessage();

        extractedCmdRegions(context, moduleMessage);

        // 当前逻辑服的信息
        BrokerClientOnlineMessage onlineMessage = BrokerClientOnlineMessage.of(moduleMessage);

        streamOtherClient(context).forEach(theModuleMessage -> {
            try {
                RpcServer rpcServer = context.brokerServer().getRpcServer();

                // 将当前逻辑服的信息发送给其他逻辑服（游戏逻辑服和游戏对外服）
                String theModuleAddress = theModuleMessage.getAddress();
                rpcServer.oneway(theModuleAddress, onlineMessage);

                // 拉取所有的逻辑服信息给自己（也就是当前新上线的逻辑服）
                BrokerClientOnlineMessage onlineMsg = BrokerClientOnlineMessage.of(theModuleMessage);
                String address = moduleMessage.getAddress();
                rpcServer.oneway(address, onlineMsg);
            } catch (RemotingException | InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    private void extractedCmdRegions(Context context, BrokerClientModuleMessage moduleMessage) {
        BrokerClientType brokerClientType = moduleMessage.getBrokerClientType();
        if (brokerClientType == BrokerClientType.LOGIC) {
            // 如果新上线的是游戏逻辑服，将路由信息保存一份
            CmdRegions cmdRegions = context.cmdRegions();
            cmdRegions.loading(moduleMessage);
        }
    }

    void offline(Context context) {
        ExecutorRegionKit.getSimpleThreadExecutor(executorIndex).executeTry(() -> {
            // offline logic
            internalOfflineNew(context);
        });
    }

    private static void internalOfflineNew(Context context) {
        // 当前下线的逻辑服
        BrokerClientModuleMessage moduleMessage = getBrokerClientModuleMessage(context);

        // 通知其他逻辑服，当前逻辑服下线了
        BrokerClientOfflineMessage offlineMessage = BrokerClientOfflineMessage.of(moduleMessage);

        streamOtherClient(context).forEach(theModuleMessage -> {
            try {
                String address = theModuleMessage.getAddress();
                context.brokerServer().getRpcServer().oneway(address, offlineMessage);
            } catch (RemotingException | InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        });

    }

    private BrokerClientModuleMessage getBrokerClientModuleMessage(Context context) {
        BrokerClientModuleMessage moduleMessage = context.moduleMessage();

        BrokerClientType brokerClientType = moduleMessage.getBrokerClientType();
        if (brokerClientType == BrokerClientType.LOGIC) {
            CmdRegions cmdRegions = context.cmdRegions();
            String id = moduleMessage.getId();
            int idHash = moduleMessage.getIdHash();
            BrokerClientId brokerClientId = new BrokerClientId(idHash, id);
            // 游戏逻辑服的路由数据
            cmdRegions.unLoading(brokerClientId);
        }

        return moduleMessage;
    }

    Stream<BrokerClientModuleMessage> streamOtherClient(Context context) {
        BrokerClientModuleMessage moduleMessage = context.moduleMessage();

        return context.brokerClientModules()
                .listBrokerClientModuleMessage()
                .stream()
                // 排除自己
                .filter(message -> !Objects.equals(message.getId(), moduleMessage.getId()));
    }
}
