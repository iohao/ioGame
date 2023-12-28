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
package com.iohao.game.action.skeleton.eventbus;

import com.iohao.game.common.kit.collect.ListMultiMap;
import com.iohao.game.common.kit.collect.SetMultiMap;
import com.iohao.game.common.kit.concurrent.executor.SimpleThreadExecutorRegion;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
 */
@Slf4j
@UtilityClass
public class EventBusRegion {
    /**
     * EventBus map
     * <pre>
     *     key : brokerClientId
     * </pre>
     */
    final Map<String, EventBus> eventBusMap = new NonBlockingHashMap<>();
    /**
     * 其他进程的订阅者
     * <pre>
     *     key : eventSource class name
     *     value : across progress brokerClientId
     * </pre>
     */
    final SetMultiMap<String, EventBrokerClientMessage> remoteTopicMultiMap = SetMultiMap.create();
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

    /**
     * 加载其他进程的订阅者主题
     *
     * @param topics                   订阅者主题
     * @param eventBrokerClientMessage 其他进程的逻辑服信息
     */
    public void loadRemoteEventTopic(Collection<String> topics, EventBrokerClientMessage eventBrokerClientMessage) {
        topics.forEach(topic -> remoteTopicMultiMap.put(topic, eventBrokerClientMessage));
    }

    public void unloadRemoteTopic(Collection<String> topics, EventBrokerClientMessage eventBrokerClientMessage) {
        for (String topic : topics) {
            Set<EventBrokerClientMessage> eventBrokerClientMessages = remoteTopicMultiMap.get(topic);
            eventBrokerClientMessages.remove(eventBrokerClientMessage);
        }
    }

    Set<EventBrokerClientMessage> listRemoteEventBrokerClientMessage(EventBusMessage eventBusMessage) {
        Object eventSource = eventBusMessage.getEventSource();
        Class<?> eventSourceClass = eventSource.getClass();
        String name = eventSourceClass.getName();
        return remoteTopicMultiMap.get(name);
    }

    void addLocal(EventBus eventBus) {
        eventBusMap.put(eventBus.id, eventBus);

        SimpleThreadExecutorRegion.me().execute(EventBusRegion::resetLocalSubscriber, 1);
    }

    /**
     * 根据事件消息，获取当前进程所有的订阅者
     *
     * @param eventBusMessage 事件消息
     * @return 当前进程所有的订阅者
     */
    List<Subscriber> listLocalSubscriber(EventBusMessage eventBusMessage) {
        Object eventSource = eventBusMessage.getEventSource();
        Class<?> eventSourceClazz = eventSource.getClass();
        return subscriberListMap.get(eventSourceClazz);
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

            sort(subscribers);

            subscriberListMap.of(entry.getKey()).addAll(subscribers);
        }
    }

    void sort(List<Subscriber> subscribers) {
        // order 排序
        subscribers.sort((o1, o2) -> o2.order - o1.order);
    }
}
