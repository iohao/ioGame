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
package com.iohao.game.action.skeleton.eventbus;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * 事件总线管理域
 * <pre>
 *     1. 管理其他进程的订阅者信息。
 *     2. 如果一个进程中启动了多少逻辑服，那么多个逻辑服的订阅者会添加到这里。
 *
 *     如果只想获取某个逻辑服中的订阅者集合，可通过 {@code EventBus.listSubscriber} 方法得到
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-12-24
 * @since 21
 */
@UtilityClass
public final class EventBusRegion {
    public EventBus getEventBus(String brokerClientId) {
        return EventBusLocalRegion.getEventBus(brokerClientId);
    }

    void addLocal(EventBus eventBus) {
        EventBusLocalRegion.addLocal(eventBus);

        EventBusAnyTagRegion.add(eventBus.getEventBrokerClientMessage());
    }

    Stream<EventBus> streamLocalEventBus() {
        return EventBusLocalRegion.streamEventBus();
    }

    boolean hasLocalNeighbor() {
        return EventBusLocalRegion.hasLocalNeighbor();
    }

    /**
     * 根据事件消息，获取当前进程所有的订阅者
     *
     * @param eventBusMessage 事件消息
     * @return 当前进程所有的订阅者
     */
    List<Subscriber> listLocalSubscriber(EventBusMessage eventBusMessage) {
        return EventBusLocalRegion.listLocalSubscriber(eventBusMessage);
    }

    public void loadRemoteEventTopic(EventBrokerClientMessage eventBrokerClientMessage) {
        EventBusRemoteRegion.loadRemoteEventTopic(eventBrokerClientMessage);

        EventBusAnyTagRegion.add(eventBrokerClientMessage);
    }

    public void unloadRemoteTopic(EventBrokerClientMessage eventBrokerClientMessage) {
        EventBusRemoteRegion.unloadRemoteTopic(eventBrokerClientMessage);

        EventBusAnyTagRegion.remove(eventBrokerClientMessage);
    }

    Set<EventBrokerClientMessage> listRemoteEventBrokerClientMessage(EventBusMessage eventBusMessage) {
        return EventBusRemoteRegion.listRemoteEventBrokerClientMessage(eventBusMessage);
    }
}
