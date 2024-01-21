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

import com.alipay.remoting.exception.RemotingException;
import com.iohao.game.bolt.broker.core.client.BrokerClientType;
import com.iohao.game.bolt.broker.core.message.BrokerClientModuleMessage;
import com.iohao.game.bolt.broker.core.message.BrokerClientOfflineMessage;
import com.iohao.game.bolt.broker.core.message.BrokerClientOnlineMessage;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.service.BrokerClientModules;
import com.iohao.game.common.kit.MoreKit;
import com.iohao.game.common.kit.concurrent.executor.ExecutorRegionKit;
import com.iohao.game.core.common.cmd.BrokerClientId;
import com.iohao.game.core.common.cmd.CmdRegions;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;
import org.jctools.maps.NonBlockingHashSet;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author 渔民小镇
 * @date 2023-12-14
 */
@Slf4j
@UtilityClass
class LineKit {
    long executorIndex = 0;

    record Context(BrokerServer brokerServer
            , BrokerClientModules brokerClientModules
            , CmdRegions cmdRegions
            , BrokerClientModuleMessage moduleMessage) {
    }

    void online(Context context) {
        // 避免并发，使用同一个执行器
        ExecutorRegionKit.getSimpleThreadExecutor(executorIndex).execute(() -> {
            // online logic
            internalOnline(context);
        });
    }

    private void internalOnline(Context context) {
        // 当前新上线的逻辑服
        BrokerClientModuleMessage moduleMessage = context.moduleMessage();

        extractedCmdRegions(context, moduleMessage);

        // 过滤条件，已经处理过的做个记录
        Predicate<BrokerClientModuleMessage> predicate = theModuleMessage -> {
            ModuleMessageDelegate moduleMessageDelegate = ModuleMessageDelegates.get(theModuleMessage);
            if (moduleMessageDelegate.contains(moduleMessage)) {
                return false;
            }

            moduleMessageDelegate.addBrokerClientModuleMessage(moduleMessage);
            return true;
        };

        // 将当前逻辑服的信息发送给其他逻辑服（游戏逻辑服和游戏对外服）
        BrokerClientOnlineMessage onlineMessage = BrokerClientOnlineMessage.of(moduleMessage);
        streamOtherClient(context).filter(predicate).forEach(theModuleMessage -> {
            try {
                String address = theModuleMessage.getAddress();
                context.brokerServer().getRpcServer().oneway(address, onlineMessage);
            } catch (RemotingException | InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        });

        // 拉取所有的逻辑服信息给自己（也就是当前新上线的逻辑服）
        streamOtherClient(context).filter(predicate).forEach(theModuleMessage -> {
            try {
                BrokerClientOnlineMessage onlineMsg = BrokerClientOnlineMessage.of(theModuleMessage);
                String address = moduleMessage.getAddress();

                BrokerServer brokerServer = context.brokerServer();
                brokerServer.getRpcServer().oneway(address, onlineMsg);
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
        ExecutorRegionKit.getSimpleThreadExecutor(executorIndex).execute(() -> {
            // offline logic
            internalOffline(context);
        });
    }

    private static void internalOffline(Context context) {
        // 当前下线的逻辑服
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

        ModuleMessageDelegates.offline(moduleMessage);
    }

    Stream<BrokerClientModuleMessage> streamOtherClient(Context context) {
        BrokerClientModuleMessage moduleMessage = context.moduleMessage();

        return context.brokerClientModules()
                .listBrokerClientModuleMessage()
                .stream()
                // 排除自己
                .filter(message -> !Objects.equals(message.getId(), moduleMessage.getId()));
    }

    @UtilityClass
    private class ModuleMessageDelegates {
        Map<String, ModuleMessageDelegate> map = new NonBlockingHashMap<>();

        ModuleMessageDelegate get(BrokerClientModuleMessage moduleMessage) {
            String id = moduleMessage.getId();
            ModuleMessageDelegate moduleMessageDelegate = map.get(id);

            if (Objects.isNull(moduleMessageDelegate)) {
                var newModuleMessageDelegate = new ModuleMessageDelegate(moduleMessage, new NonBlockingHashSet<>());
                return MoreKit.firstNonNull(map.putIfAbsent(id, newModuleMessageDelegate), newModuleMessageDelegate);
            }

            return moduleMessageDelegate;
        }

        void offline(BrokerClientModuleMessage moduleMessage) {
            String id = moduleMessage.getId();
            map.remove(id);
        }
    }

    private record ModuleMessageDelegate(BrokerClientModuleMessage moduleMessage, Set<String> recordSet) {
        void addBrokerClientModuleMessage(BrokerClientModuleMessage moduleMessage) {
            String id = moduleMessage.getId();
            recordSet.add(id);
        }

        boolean contains(BrokerClientModuleMessage moduleMessage) {
            String id = moduleMessage.getId();
            return recordSet.contains(id);
        }
    }
}
