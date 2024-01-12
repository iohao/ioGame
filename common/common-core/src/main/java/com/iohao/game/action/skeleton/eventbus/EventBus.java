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

import com.iohao.game.action.skeleton.core.IoGameCommonCoreConfig;
import com.iohao.game.action.skeleton.core.commumication.BrokerClientContext;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.common.kit.CollKit;
import com.iohao.game.common.kit.concurrent.executor.ThreadExecutor;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * EventBus 是逻辑服事件总线。
 * EventBus、业务框架、逻辑服三者是 1:1:1 的关系。
 * <p>
 * example1 - 通过 FlowContext 获取对应的 eventBus
 * <pre>{@code
 *         EventBus eventBus = flowContext.getEventBus();
 *         eventBus.fire(userLoginEventMessage);
 * }
 * </pre>
 * <p>
 * example2 - 通过逻辑服 id 获取对应的 eventBus
 * <pre>{@code
 *         BrokerClientContext brokerClientContext = flowContext.getBrokerClientContext();
 *         String id = brokerClientContext.getId();
 *         EventBus eventBus = EventBusRegion.getEventBus(id);
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-12-24
 * @see FlowContext#getEventBus()
 * @see EventBusRegion
 */
@Slf4j
@Setter
@FieldDefaults(level = AccessLevel.PACKAGE)
public final class EventBus {
    /** 订阅者管理 */
    final SubscriberRegistry subscriberRegistry = new SubscriberRegistry();
    final String id;

    SubscribeExecutorStrategy subscribeExecutorStrategy;
    SubscriberInvokeCreator subscriberInvokeCreator;
    EventBusMessageCreator eventBusMessageCreator;
    EventBusListener eventBusListener;

    /** 对应逻辑服的相关信息 */
    EventBrokerClientMessage eventBrokerClientMessage;
    /** 逻辑服 */
    BrokerClientContext brokerClientContext;

    @Setter(AccessLevel.PACKAGE)
    EventBusStatus status = EventBusStatus.register;

    EventBus(String id) {
        this.id = Objects.requireNonNull(id);
    }

    public void register(Object eventBusSubscriber) {

        if (status != EventBusStatus.register) {
            throw new RuntimeException("运行中不允许注册订阅者，请在 AbstractEventRunner.registerEventBus 方法中注册。 ");
        }

        // 注册
        this.subscriberRegistry.register(eventBusSubscriber, this.subscriberInvokeCreator);
    }

    public EventBusMessage createEventBusMessage(Object eventSource) {
        return this.eventBusMessageCreator.create(eventSource);
    }

    public EventTopicMessage getEventTopicMessage() {

        // 当前 eventBus 订阅的所有事件源主题
        var eventSourceClassSet = this.subscriberRegistry.listEventSourceClass();

        Set<String> collect = eventSourceClassSet.stream()
                .map(Class::getName)
                .collect(Collectors.toSet());

        EventTopicMessage message = new EventTopicMessage();
        message.setTopicSet(collect);

        return message;
    }

    /**
     * [异步]发送事件给订阅者
     * <pre>
     *     1 给当前进程所有逻辑服的订阅者发送事件消息
     *     2 给其他进程的订阅者发送事件消息
     * </pre>
     *
     * @param eventBusMessage 事件消息
     */
    public void fire(EventBusMessage eventBusMessage) {
        // 给当前进程所有逻辑服的订阅者发送事件消息
        this.fireLocal(eventBusMessage);
        // 给其他进程的订阅者发送事件
        this.fireRemote(eventBusMessage);

        if (eventBusMessage.emptyFireType()) {
            this.eventBusListener.emptySubscribe(eventBusMessage, this);
        }
    }

    /**
     * [同步]发送事件给订阅者。
     * <pre>
     *     1 [同步] 给当前进程所有逻辑服的订阅者发送事件消息
     *     2 [异步] 给其他进程的订阅者发送事件消息
     *
     *     注意，这里的同步仅指当前进程订阅者的同步，对其他进程中的订阅者无效（处理远程订阅者使用的是异步）。
     * </pre>
     *
     * @param eventBusMessage 事件消息
     */
    public void fireSync(EventBusMessage eventBusMessage) {
        // 给当前进程所有逻辑服的订阅者发送事件消息
        this.fireLocalSync(eventBusMessage);
        // 给其他进程的订阅者发送事件
        this.fireRemote(eventBusMessage);

        if (eventBusMessage.emptyFireType()) {
            this.eventBusListener.emptySubscribe(eventBusMessage, this);
        }
    }

    /**
     * [异步]发送事件给订阅者
     * <pre>
     *     1 给当前进程所有逻辑服的订阅者发送事件消息
     *     2 给其他进程的订阅者发送事件消息
     * </pre>
     *
     * @param eventSource 事件源
     */
    public void fire(Object eventSource) {
        EventBusMessage eventBusMessage = createEventBusMessage(eventSource);
        this.fire(eventBusMessage);
    }

    /**
     * [同步]发送事件给订阅者。
     * <pre>
     *     1 [同步] 给当前进程所有逻辑服的订阅者发送事件消息
     *     2 [异步] 给其他进程的订阅者发送事件消息
     *
     *     注意，这里的同步仅指当前进程订阅者的同步，对其他进程中的订阅者无效（处理远程订阅者使用的是异步）。
     * </pre>
     *
     * @param eventSource 事件源
     */
    public void fireSync(Object eventSource) {
        EventBusMessage eventBusMessage = createEventBusMessage(eventSource);
        this.fireSync(eventBusMessage);
    }

    public void fireLocal(Object eventSource) {
        EventBusMessage eventBusMessage = createEventBusMessage(eventSource);
        this.fireLocal(eventBusMessage);
    }

    /**
     * 给当前进程所有逻辑服的订阅者发送事件消息
     *
     * @param eventBusMessage 事件消息
     */
    public void fireLocal(EventBusMessage eventBusMessage) {
        this.fireLocal(eventBusMessage, true);
    }

    public void fireLocalSync(Object eventSource) {
        EventBusMessage eventBusMessage = createEventBusMessage(eventSource);
        this.fireLocalSync(eventBusMessage);
    }

    /**
     * 给当前进程所有逻辑服的订阅者发送事件消息
     * <pre>
     *     [同步]
     * </pre>
     *
     * @param eventBusMessage 事件消息
     */
    public void fireLocalSync(EventBusMessage eventBusMessage) {
        this.fireLocal(eventBusMessage, false);
    }

    private void fireLocal(EventBusMessage eventBusMessage, boolean async) {
        List<Subscriber> subscribers = EventBusRegion.listLocalSubscriber(eventBusMessage);
        if (CollKit.isEmpty(subscribers)) {
            return;
        }

        eventBusMessage.addFireType(EventBusFireType.fireLocal);

        // 发送事件
        this.invokeSubscriber(eventBusMessage, async, subscribers);
    }

    void fireRemote(EventBusMessage eventBusMessage) {
        var messageSet = EventBusRegion.listRemoteEventBrokerClientMessage(eventBusMessage);

        if (CollKit.isEmpty(messageSet)) {
            return;
        }

        // 如果其他进程中存在当前事件源的订阅者，将事件源发布到其他进程中
        eventBusMessage.setEventBrokerClientMessageSet(messageSet);
        eventBusMessage.addFireType(EventBusFireType.fireRemote);

        this.extractedPrint(eventBusMessage);

        try {
            this.brokerClientContext.oneway(eventBusMessage);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void fireMe(Object eventSource) {
        EventBusMessage eventBusMessage = createEventBusMessage(eventSource);
        this.fireMe(eventBusMessage);
    }

    /**
     * 仅给当前 EventBus 的订阅者发送事件消息
     * <pre>
     *     已注册到 {@link EventBus#register(Object)}  的订阅者
     * </pre>
     *
     * @param eventBusMessage 事件消息
     */
    public void fireMe(EventBusMessage eventBusMessage) {
        this.fireMe(eventBusMessage, true);
    }

    public void fireMeSync(Object eventSource) {
        EventBusMessage eventBusMessage = createEventBusMessage(eventSource);
        this.fireMeSync(eventBusMessage);
    }

    /**
     * 仅给当前 EventBus 的订阅者发送事件消息
     * <pre>
     *     已注册到 {@link EventBus#register(Object)} 的订阅者
     *
     *     [同步]
     * </pre>
     *
     * @param eventBusMessage 事件消息
     */
    public void fireMeSync(EventBusMessage eventBusMessage) {
        this.fireMe(eventBusMessage, false);
    }

    private void fireMe(EventBusMessage eventBusMessage, boolean async) {
        Collection<Subscriber> subscribers = this.listSubscriber(eventBusMessage);

        if (CollKit.isEmpty(subscribers)) {
            return;
        }

        eventBusMessage.addFireType(EventBusFireType.fireMe);

        this.invokeSubscriber(eventBusMessage, async, subscribers);
    }

    public void fireLocalNeighbor(Object eventSource) {
        EventBusMessage eventBusMessage = createEventBusMessage(eventSource);
        this.fireLocalNeighbor(eventBusMessage);
    }

    public void fireLocalNeighbor(EventBusMessage eventBusMessage) {
        this.fireLocalNeighbor(eventBusMessage, true);
    }

    public void fireLocalNeighborSync(Object eventSource) {
        EventBusMessage eventBusMessage = createEventBusMessage(eventSource);
        this.fireLocalNeighborSync(eventBusMessage);
    }

    public void fireLocalNeighborSync(EventBusMessage eventBusMessage) {
        this.fireLocalNeighbor(eventBusMessage, false);
    }

    /**
     * 给当前进程其他逻辑服的订阅者发送事件消息，不包括当前 EventBus。
     *
     * @param eventBusMessage 事件消息
     * @param async           true 表示异步执行
     */
    private void fireLocalNeighbor(EventBusMessage eventBusMessage, boolean async) {
        int size = EventBusRegion.eventBusMap.size();
        if (size == 1) {
            // 当只有一个 eventBus 时，通常是自己，就也是当前 eventBus;
            return;
        }

        var subscribers = EventBusRegion.eventBusMap
                .values()
                .stream()
                // 排除自己（当前 EventBus）
                .filter(eventBus -> !Objects.equals(this, eventBus))
                // 得到所有的订阅者
                .flatMap(eventBus -> eventBus.listSubscriber(eventBusMessage).stream())
                .toList();

        if (CollKit.isEmpty(subscribers)) {
            return;
        }

        eventBusMessage.addFireType(EventBusFireType.fireLocalNeighbor);

        // 发送事件
        this.invokeSubscriber(eventBusMessage, async, subscribers);
    }

    private void extractedPrint(EventBusMessage eventBusMessage) {
        if (IoGameCommonCoreConfig.eventBusLog) {
            log.info("###### 触发远程逻辑服的订阅者 - {} -  : {}", this.eventBrokerClientMessage.getAppName(), eventBusMessage);
            for (EventBrokerClientMessage eventBrokerClientMessage : eventBusMessage.getEventBrokerClientMessageSet()) {
                log.info("远程逻辑服 : {}", eventBrokerClientMessage.getAppName());
            }

            System.out.println();
        }
    }

    private void invokeSubscriber(EventBusMessage eventBusMessage, boolean async, Collection<Subscriber> subscribers) {
        if (async) {
            // 异步执行
            for (Subscriber subscriber : subscribers) {
                // 根据策略得到对应的执行器
                ThreadExecutor threadExecutor = this.subscribeExecutorStrategy.select(subscriber, eventBusMessage);

                SubscriberInvoke subscriberInvoke = subscriber.getSubscriberInvoke();
                threadExecutor.execute(() -> this.invoke(subscriberInvoke, eventBusMessage));
            }
        } else {
            // 同步执行
            for (Subscriber subscriber : subscribers) {
                SubscriberInvoke subscriberInvoke = subscriber.getSubscriberInvoke();
                this.invoke(subscriberInvoke, eventBusMessage);
            }
        }
    }

    private void invoke(SubscriberInvoke subscriberInvoke, EventBusMessage eventBusMessage) {
        try {
            subscriberInvoke.invoke(eventBusMessage);
        } catch (Throwable e) {
            this.eventBusListener.invokeException(e, eventBusMessage.getEventSource(), eventBusMessage);
        }
    }

    private Collection<Subscriber> listSubscriber(EventBusMessage eventBusMessage) {
        var eventSource = eventBusMessage.getEventSource();
        return this.subscriberRegistry.listSubscriber(eventSource);
    }

    enum EventBusStatus {
        register,
        run
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof EventBus eventBus)) {
            return false;
        }

        return Objects.equals(id, eventBus.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
