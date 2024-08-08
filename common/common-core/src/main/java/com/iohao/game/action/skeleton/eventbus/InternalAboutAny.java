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

import com.iohao.game.common.kit.CollKit;
import com.iohao.game.common.kit.MoreKit;
import com.iohao.game.common.kit.collect.SetMultiMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingHashMap;

import java.util.*;
import java.util.stream.Stream;

/**
 * any tag topic
 * <p>
 * 处理 topic 时的逻辑
 * <pre>
 *     1 通过 topic 找到所有 anyTag，因为 anyTag 持有逻辑服相关的信息。
 *     2 在 anyTag 中找出一个 BrokerClient 处理该 topic，优先找当前进程中的 BrokerClient。
 *     3 得到 BrokerClient 信息后，从对应的 EventBusRegion 中得到对应的 EventBus。
 *       3.1 当 EventBus 不为 null 时，表示本地进程中有订阅者
 *       3.2 当 EventBus 为 null 时，表示只有远程的订阅者
 * </pre>
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
final class AnyTagBrokerClient {
    final Map<BrokerClientId, EventBrokerClientMessage> map = new NonBlockingHashMap<>();
    long nextRemote;
    long nextLocal;
    EventBrokerClientMessage[] eventRemoteBrokerClientMessages;
    EventBrokerClientMessage[] eventLocalBrokerClientMessages;

    boolean isEmpty() {
        return this.map.isEmpty();
    }

    Stream<EventBrokerClientMessage> streamEventBrokerClientMessage() {
        return this.map.isEmpty() ? Stream.empty() : this.map.values().stream();
    }

    void add(EventBrokerClientMessage eventBrokerClientMessage) {

        String brokerClientId = eventBrokerClientMessage.getBrokerClientId();
        BrokerClientId id = BrokerClientId.of(brokerClientId);
        this.map.put(id, eventBrokerClientMessage);

        EventBusKit.executeSafe(this::reload);
    }

    void remove(EventBrokerClientMessage eventBrokerClientMessage) {
        String brokerClientId = eventBrokerClientMessage.getBrokerClientId();
        BrokerClientId id = BrokerClientId.of(brokerClientId);
        this.map.remove(id);

        EventBusKit.executeSafe(this::reload);
    }

    EventBrokerClientMessage anyEventBrokerClientMessage() {
        // 优先使用同进程的
        int localLength = this.eventLocalBrokerClientMessages.length;
        if (localLength > 0) {
            if (localLength == 1) {
                return this.eventLocalBrokerClientMessages[0];
            }

            int index = getIndex(this.nextLocal++, this.eventLocalBrokerClientMessages);
            return this.eventLocalBrokerClientMessages[index];
        }

        int remoteLength = this.eventRemoteBrokerClientMessages.length;
        if (remoteLength == 1) {
            return this.eventRemoteBrokerClientMessages[0];
        }

        int index = getIndex(this.nextRemote++, this.eventRemoteBrokerClientMessages);
        return this.eventRemoteBrokerClientMessages[index];
    }

    private int getIndex(long index, EventBrokerClientMessage[] clientMessages) {
        return (int) (index % clientMessages.length);
    }

    static final EventBrokerClientMessage[] emptyClientMessage = new EventBrokerClientMessage[0];

    private void reload() {

        var localClientMessageList = new ArrayList<EventBrokerClientMessage>();
        var remoteClientMessageList = new ArrayList<EventBrokerClientMessage>();

        for (EventBrokerClientMessage clientMessage : this.map.values()) {
            if (clientMessage.isRemote()) {
                remoteClientMessageList.add(clientMessage);
            } else {
                localClientMessageList.add(clientMessage);
            }
        }

        this.nextRemote = 0;
        this.nextLocal = 0;

        this.eventLocalBrokerClientMessages = localClientMessageList.isEmpty()
                ? emptyClientMessage
                : localClientMessageList.toArray(emptyClientMessage);

        this.eventRemoteBrokerClientMessages = remoteClientMessageList.isEmpty()
                ? emptyClientMessage
                : remoteClientMessageList.toArray(emptyClientMessage);
    }
}

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
final class AnyTagViewData {
    static final AnyTagViewData empty = new AnyTagViewData();
    List<EventBrokerClientMessage> localMessages;
    List<EventBrokerClientMessage> remoteMessages;

    void add(EventBrokerClientMessage eventBrokerClientMessage) {
        if (eventBrokerClientMessage.isRemote()) {
            this.initRemoteMessages();
            this.remoteMessages.add(eventBrokerClientMessage);
        } else {
            this.initLocalMessages();
            this.localMessages.add(eventBrokerClientMessage);
        }
    }

    private void initLocalMessages() {
        if (Objects.isNull(this.localMessages)) {
            this.localMessages = new ArrayList<>();
        }
    }

    private void initRemoteMessages() {
        if (Objects.isNull(this.remoteMessages)) {
            this.remoteMessages = new ArrayList<>();
        }
    }
}

@FieldDefaults(level = AccessLevel.PRIVATE)
final class AnyTagView {
    SetMultiMap<String, AnyTagBrokerClient> topicMultiMap = SetMultiMap.of();

    AnyTagViewData getAnyTagData(EventBusMessage message) {
        String topic = message.getTopic();
        Set<AnyTagBrokerClient> anyTagBrokerClients = this.topicMultiMap.get(topic);

        if (CollKit.isEmpty(anyTagBrokerClients)) {
            return AnyTagViewData.empty;
        }

        AnyTagViewData anyTagViewData = new AnyTagViewData();

        anyTagBrokerClients.stream()
                .map(AnyTagBrokerClient::anyEventBrokerClientMessage)
                .forEach(anyTagViewData::add);

        return anyTagViewData;
    }

    void reload(Collection<AnyTagBrokerClient> values) {
        // 重新加载
        SetMultiMap<String, AnyTagBrokerClient> tempMultiMap = SetMultiMap.of();

        for (AnyTagBrokerClient anyTagBrokerClient : values) {
            anyTagBrokerClient.streamEventBrokerClientMessage()
                    .map(EventBrokerClientMessage::getTopics)
                    .flatMap(Collection::stream)
                    .forEach(topic -> tempMultiMap.of(topic).add(anyTagBrokerClient));
        }

        this.topicMultiMap = tempMultiMap;
    }
}

record BrokerClientTag(String tag) {
    static final Map<String, BrokerClientTag> map = new NonBlockingHashMap<>();

    static BrokerClientTag of(String tag) {
        BrokerClientTag brokerClientTag = map.get(tag);

        if (Objects.isNull(brokerClientTag)) {
            var theTag = new BrokerClientTag(tag);
            return MoreKit.firstNonNull(map.putIfAbsent(tag, theTag), theTag);
        }

        return brokerClientTag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof BrokerClientTag that)) {
            return false;
        }

        return tag.equals(that.tag);
    }

    @Override
    public int hashCode() {
        return tag.hashCode();
    }
}

record BrokerClientId(String id) {
    static final Map<String, BrokerClientId> map = new NonBlockingHashMap<>();

    static BrokerClientId of(String id) {
        BrokerClientId brokerClientId = map.get(id);

        if (Objects.isNull(brokerClientId)) {
            var theId = new BrokerClientId(id);
            return MoreKit.firstNonNull(map.putIfAbsent(id, theId), theId);
        }

        return brokerClientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof BrokerClientId that)) {
            return false;
        }

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}