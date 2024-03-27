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
package com.iohao.game.bolt.broker.core.client;

import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.SkeletonAttr;
import com.iohao.game.action.skeleton.eventbus.EventBrokerClientMessage;
import com.iohao.game.action.skeleton.eventbus.EventBus;
import com.iohao.game.action.skeleton.eventbus.EventBusRegion;
import com.iohao.game.action.skeleton.eventbus.EventTopicMessage;
import com.iohao.game.bolt.broker.core.common.processor.listener.BrokerClientListener;
import com.iohao.game.bolt.broker.core.message.BrokerClientModuleMessage;
import com.iohao.game.common.kit.CollKit;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 分布式事件总线的 listener
 *
 * @author 渔民小镇
 * @date 2023-12-24
 */
@Slf4j
final class EventBusBrokerClientListener implements BrokerClientListener {
    String eventBusTopicName = "eventBusTopicSet";
    boolean theLog;

    @Override
    public void registerBefore(BrokerClientModuleMessage moduleMessage, BrokerClient client) {
        BarSkeleton barSkeleton = client.getBarSkeleton();
        EventBus eventBus = barSkeleton.option(SkeletonAttr.eventBus);

        if (eventBus == null) {
            return;
        }

        // 记录当前 EventBus 的订阅者信息到 moduleMessage 中
        Set<String> topicSet = eventBus.listTopic();

        if (CollKit.isEmpty(topicSet)) {
            return;
        }

        moduleMessage.addHeader(this.eventBusTopicName, topicSet);
    }

    @Override
    public void offlineLogic(BrokerClientModuleMessage otherModuleMessage, BrokerClient client) {
        eventBusProcess(otherModuleMessage, client, result -> {
            if (theLog) {
                log.info("##卸载## {} 卸载 远程订阅者 {}", client.getAppName(), otherModuleMessage.getName());
            }

            EventBrokerClientMessage eventBrokerClientMessage = result.eventBrokerClientMessage();
            EventBusRegion.unloadRemoteTopic(eventBrokerClientMessage);
        });
    }

    @Override
    public void onlineLogic(BrokerClientModuleMessage otherModuleMessage, BrokerClient client) {
        eventBusProcess(otherModuleMessage, client, result -> {
            if (theLog) {
                log.info("##加载## {} 加载 远程订阅者 {}", client.getAppName(), otherModuleMessage.getName());
            }

            EventBrokerClientMessage eventBrokerClientMessage = result.eventBrokerClientMessage();
            EventBusRegion.loadRemoteEventTopic(eventBrokerClientMessage);
        });
    }

    private void eventBusProcess(BrokerClientModuleMessage otherModuleMessage, BrokerClient client, Consumer<Result> consumer) {
        Result result = getResult(otherModuleMessage, client);
        if (result.process) {
            consumer.accept(result);
        }
    }

    private Result getResult(BrokerClientModuleMessage otherModuleMessage, BrokerClient client) {
        Set<String> topicSet = otherModuleMessage.getHeader(this.eventBusTopicName);
        if (CollKit.isEmpty(topicSet)) {
            // 说明该逻辑服没有订阅者，不做任何处理
            return new Result(false, null);
        }

        // 检查该逻辑服是否在同一个进程中
        BrokerClientModuleMessage moduleMessage = client.getBrokerClientModuleMessage();
        if (Objects.equals(otherModuleMessage.getIoGamePid(), moduleMessage.getIoGamePid())) {
            // 在同一个进程中，不做处理
            return new Result(false, null);
        }

        // 将其他进程的订阅者主题添加到管理域中
        EventBrokerClientMessage eventBrokerClientMessage = createEventBrokerClientMessage(otherModuleMessage, topicSet);

        return new Result(true, eventBrokerClientMessage);
    }

    private EventBrokerClientMessage createEventBrokerClientMessage(BrokerClientModuleMessage otherModuleMessage, Set<String> topicSet) {
        String id = otherModuleMessage.getId();
        String appName = otherModuleMessage.getName();
        String tag = otherModuleMessage.getTag();
        String typeName = otherModuleMessage.getBrokerClientType().name();

        EventBrokerClientMessage eventBrokerClientMessage = new EventBrokerClientMessage(appName, tag, typeName, id);
        eventBrokerClientMessage.setRemote(true);
        eventBrokerClientMessage.setEventTopicMessage(new EventTopicMessage(topicSet));

        return eventBrokerClientMessage;
    }

    private record Result(boolean process, EventBrokerClientMessage eventBrokerClientMessage) {
    }
}
