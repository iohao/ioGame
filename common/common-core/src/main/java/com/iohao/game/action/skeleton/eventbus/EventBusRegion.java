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

import com.iohao.game.common.kit.collect.SetMultiMap;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Collection;
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
    final SetMultiMap<String, EventBrokerClientMessage> acrossProgressTopicMultiMap = SetMultiMap.create();

    public EventBus getEventBus(String brokerClientId) {
        return eventBusMap.get(brokerClientId);
    }

    /**
     * 加载其他进程的订阅者主题
     *
     * @param topics                   订阅者主题
     * @param eventBrokerClientMessage 其他进程的逻辑服信息
     */
    public void loadAcrossProgressEventBrokerClientMsg(Collection<String> topics, EventBrokerClientMessage eventBrokerClientMessage) {
        topics.forEach(topic -> acrossProgressTopicMultiMap.put(topic, eventBrokerClientMessage));
    }

    public void unloadAcrossProgressTopic(Collection<String> topics, EventBrokerClientMessage eventBrokerClientMessage) {
        for (String topic : topics) {
            Set<EventBrokerClientMessage> eventBrokerClientMessages = acrossProgressTopicMultiMap.get(topic);
            eventBrokerClientMessages.remove(eventBrokerClientMessage);
        }
    }

    void add(EventBus eventBus) {
        eventBusMap.put(eventBus.id, eventBus);
    }

    Set<EventBrokerClientMessage> listAcrossProgressEventBrokerClientMessage(EventBusMessage eventBusMessage) {
        Object eventSource = eventBusMessage.getEventSource();
        Class<?> eventSourceClass = eventSource.getClass();
        String name = eventSourceClass.getName();
        return acrossProgressTopicMultiMap.get(name);
    }

    //    /**
//     * 事件源与订阅者的映射
//     * <pre>
//     *     key : 事件源
//     *     value : 订阅者集合。（当前进程内的所有订阅者）
//     * </pre>
//     */
//    final ListMultiMap<Class<?>, Subscriber> subscriberListMap = ListMultiMap.create();
//
//    final Set<Integer> excludeIdHashSet = new NonBlockingHashSet<>();

//    void add1(EventBus eventBus) {
//        EventBrokerClientMsg eventBrokerClientMsg = eventBus.eventBrokerClientMsg;
//
//        String id = eventBrokerClientMsg.brokerClientId();
//        eventBusMap.put(id, eventBus);
//
//        int idHash = HashKit.hash32(id);
//        excludeIdHashSet.add(idHash);
//
//        SetMultiMap<Class<?>, Subscriber> tempMap = SetMultiMap.create();
//        for (EventBus bus : eventBusMap.values()) {
//            SubscriberRegistry subscriberRegistry = bus.subscriberRegistry;
//            SetMultiMap<Class<?>, Subscriber> setMultiMap = subscriberRegistry.subscriberSetMap;
//
//            if (setMultiMap.isEmpty()) {
//                continue;
//            }
//
//            for (var entry : setMultiMap.entrySet()) {
//                Class<?> key = entry.getKey();
//                tempMap.of(key).addAll(entry.getValue());
//            }
//        }
//
//        subscriberListMap.clear();
//        for (Map.Entry<Class<?>, Set<Subscriber>> entry : tempMap.entrySet()) {
//            subscriberListMap.of(entry.getKey()).addAll(entry.getValue());
//        }
//    }

//    /**
//     * 根据事件源，获取当前进程的所有订阅者
//     *
//     * @param eventSource 事件源
//     * @return 当前进程的所有订阅者
//     */
//    List<Subscriber> listSubscriber(Object eventSource) {
//        Class<?> eventSourceClazz = eventSource.getClass();
//        return listSubscriber(eventSourceClazz);
//    }
//
//    private List<Subscriber> listSubscriber(Class<?> eventSourceClazz) {
//        return subscriberListMap.get(eventSourceClazz);
//    }

//    /**
//     * 根据事件源，获取当前进程的所有订阅者
//     *
//     * @param eventSource 事件源
//     * @return 当前进程的所有订阅者
//     */
//    List<Subscriber> listSubscriber1(Object eventSource) {
//        Class<?> eventSourceClazz = eventSource.getClass();
//        return listSubscriber(eventSourceClazz);
//    }
//
//    private List<Subscriber> listSubscriber1(Class<?> eventSourceClazz) {
//        return subscriberListMap.get(eventSourceClazz);
//    }
}
