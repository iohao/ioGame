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

import com.iohao.game.common.kit.collect.SetMultiMap;
import lombok.experimental.UtilityClass;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author 渔民小镇
 * @date 2024-01-21
 */
@UtilityClass
class EventBusRemoteRegion {
    /**
     * 其他进程的订阅者
     * <pre>
     *     key : eventSource class name
     *     value : across progress id
     * </pre>
     */
    final SetMultiMap<String, EventBrokerClientMessage> remoteTopicMultiMap = SetMultiMap.create();

    /**
     * 其他进程逻辑服的信息
     * <pre>
     *     key : EventBrokerClientMessage id
     * </pre>
     */
    final Map<String, EventBrokerClientMessage> eventBrokerClientMessageMap = new NonBlockingHashMap<>();

    /**
     * 加载其他进程的订阅者主题
     *
     * @param eventBrokerClientMessage 其他进程的逻辑服信息
     */
    public void loadRemoteEventTopic(EventBrokerClientMessage eventBrokerClientMessage) {
        Collection<String> topics = eventBrokerClientMessage.getTopics();
        topics.forEach(topic -> remoteTopicMultiMap.put(topic, eventBrokerClientMessage));
        eventBrokerClientMessageMap.put(eventBrokerClientMessage.brokerClientId, eventBrokerClientMessage);


    }

    public void unloadRemoteTopic(EventBrokerClientMessage eventBrokerClientMessage) {
        Collection<String> topics = eventBrokerClientMessage.getTopics();
        for (String topic : topics) {
            Set<EventBrokerClientMessage> eventBrokerClientMessages = remoteTopicMultiMap.get(topic);
            eventBrokerClientMessages.remove(eventBrokerClientMessage);
        }

        eventBrokerClientMessageMap.remove(eventBrokerClientMessage.brokerClientId);
    }

    Set<EventBrokerClientMessage> listRemoteEventBrokerClientMessage(EventBusMessage eventBusMessage) {
        String name = eventBusMessage.getTopic();
        return remoteTopicMultiMap.get(name);
    }
}
