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
package com.iohao.game.bolt.broker.client.processor;

import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.client.BrokerClientAttr;
import com.iohao.game.bolt.broker.core.common.processor.listener.BrokerClientListenerRegion;
import com.iohao.game.bolt.broker.core.message.BrokerClientModuleMessage;
import com.iohao.game.common.kit.concurrent.executor.ExecutorRegionKit;
import lombok.experimental.UtilityClass;

import java.util.Set;

/**
 * @author 渔民小镇
 * @date 2024-02-06
 */
@UtilityClass
public class BrokerClientLineKit {
    public void executeSafe(BrokerClientModuleMessage moduleMessage, Runnable runnable) {
        String tag = moduleMessage.getTag();
        int index = Math.abs(tag.hashCode());

        ExecutorRegionKit
                .getSimpleThreadExecutor(index)
                .executeTry(runnable);
    }

    public void onlineProcess(BrokerClientModuleMessage moduleMessage, BrokerClient brokerClient) {
        String moduleMessageId = moduleMessage.getId();

        // 在集群模式下，可能会触发多次；对于逻辑服的上线处理，默认只处理一次。
        Set<String> onlineListenerRecordSet = brokerClient.option(BrokerClientAttr.onlineListenerRecordSet);
        if (onlineListenerRecordSet.contains(moduleMessageId)) {
            return;
        }

        onlineListenerRecordSet.add(moduleMessageId);

        Set<String> offlineListenerRecordSet = brokerClient.option(BrokerClientAttr.offlineListenerRecordSet);
        offlineListenerRecordSet.remove(moduleMessageId);

        // BrokerClientOnlineMessage 是 Broker（游戏网关）发送过来的信息，表示有新的逻辑服上线
        BrokerClientListenerRegion listenerRegion = brokerClient.getBrokerClientListenerRegion();
        listenerRegion.forEach(listener -> {
            switch (moduleMessage.getBrokerClientType()) {
                case EXTERNAL -> listener.onlineExternal(moduleMessage, brokerClient);
                case LOGIC -> listener.onlineLogic(moduleMessage, brokerClient);
            }
        });
    }

    public void offlineProcess(BrokerClientModuleMessage moduleMessage, BrokerClient brokerClient) {
        String moduleMessageId = moduleMessage.getId();

        // 在集群模式下，可能会触发多次；对于逻辑服的下线处理，默认只处理一次。
        Set<String> offlineListenerRecordSet = brokerClient.option(BrokerClientAttr.offlineListenerRecordSet);
        if (offlineListenerRecordSet.contains(moduleMessageId)) {
            return;
        }

        offlineListenerRecordSet.add(moduleMessageId);

        Set<String> onlineListenerRecordSet = brokerClient.option(BrokerClientAttr.onlineListenerRecordSet);
        onlineListenerRecordSet.remove(moduleMessageId);

        // BrokerClientOfflineMessage 是 Broker（游戏网关）发送过来的信息，表示有逻辑服下线
        BrokerClientListenerRegion listenerRegion = brokerClient.getBrokerClientListenerRegion();
        listenerRegion.forEach(listener -> {
            switch (moduleMessage.getBrokerClientType()) {
                case EXTERNAL -> listener.offlineExternal(moduleMessage, brokerClient);
                case LOGIC -> listener.offlineLogic(moduleMessage, brokerClient);
            }
        });
    }
}
