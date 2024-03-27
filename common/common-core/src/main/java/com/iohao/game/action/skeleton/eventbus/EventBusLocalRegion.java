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

import com.iohao.game.common.kit.collect.ListMultiMap;
import lombok.experimental.UtilityClass;
import org.jctools.maps.NonBlockingHashMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author 渔民小镇
 * @date 2024-01-21
 */
@UtilityClass
class EventBusLocalRegion {
    /**
     * EventBus map
     * <pre>
     *     key : id
     * </pre>
     */
    final Map<String, EventBus> eventBusMap = new NonBlockingHashMap<>();
    /**
     * 事件源与订阅者的映射
     * <pre>
     *     key : 事件源
     *     value : 订阅者集合。（当前进程内的所有订阅者）
     * </pre>
     */
    final ListMultiMap<Class<?>, Subscriber> subscriberListMap = ListMultiMap.create();

    public EventBus getEventBus(String brokerClientId) {
        return eventBusMap.get(brokerClientId);
    }

    boolean hasLocalNeighbor() {
        // 当只有一个 eventBus 时，通常是自己，就也是当前 eventBus;
        return eventBusMap.size() > 1;
    }

    /**
     * 根据事件消息，获取当前进程所有的订阅者
     *
     * @param eventBusMessage 事件消息
     * @return 当前进程所有的订阅者
     */
    List<Subscriber> listLocalSubscriber(EventBusMessage eventBusMessage) {
        Class<?> eventSourceClazz = eventBusMessage.getTopicClass();
        return subscriberListMap.get(eventSourceClazz);
    }

    Stream<EventBus> streamEventBus() {
        return eventBusMap.values().stream();
    }

    void addLocal(EventBus eventBus) {
        eventBusMap.put(eventBus.id, eventBus);

        EventBusKit.executeSafe(EventBusLocalRegion::resetLocalSubscriber);
    }

    private void resetLocalSubscriber() {

        ListMultiMap<Class<?>, Subscriber> tempMultiMap = ListMultiMap.create();
        for (EventBus eventBus : eventBusMap.values()) {

            SubscriberRegistry subscriberRegistry = eventBus.subscriberRegistry;
            var multiMap = subscriberRegistry.subscriberMultiMap;

            if (multiMap.isEmpty()) {
                continue;
            }

            for (var entry : multiMap.entrySet()) {
                Class<?> key = entry.getKey();
                tempMultiMap.of(key).addAll(entry.getValue());
            }
        }

        subscriberListMap.clear();

        for (Map.Entry<Class<?>, List<Subscriber>> entry : tempMultiMap.entrySet()) {
            var subscribers = entry.getValue();

            EventBusKit.sort(subscribers);

            subscriberListMap.of(entry.getKey()).addAll(subscribers);
        }
    }
}
