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

import com.iohao.game.action.skeleton.core.commumication.BrokerClientContext;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.common.kit.concurrent.executor.ExecutorRegion;

import java.util.*;

/**
 * 事件总线 EventBus，EventBus、业务框架、逻辑服三者是 1:1:1 的关系。
 * <p>
 * 发布事件
 * <pre>
 *     在发布事件时
 *     1. 如果相关订阅者在同进程内，可控制同步和异步发送。
 *     2. 如果相关订阅者不在同一个进程内，而是分布在不同的进程中，则只能异步发送（即使使用了同步的方法来发布事件）。
 *
 *     这里的【同步】指的是：发布事件时，相关订阅者执行完成后，主逻辑才会继续往下走。
 *     这里的【异步】指的是：发布事件时，主逻辑不会阻塞，相关订阅者会在其他线程中执行。
 *
 *     无论是同步或者是异步，相关订阅者在执行逻辑服时，默认是线程安全的；这是因为订阅者 {@link EventSubscribe} 默认使用的是用户线程执行器。
 * </pre>
 * <p>
 * 关于获取 EventBus 的相关示例
 * <p>
 * for example 1 - 通过 FlowContext 获取对应的 eventBus
 * <pre>{@code
 * EventBus eventBus = flowContext.getEventBus();
 * eventBus.fire(userLoginEventMessage);
 * }
 * </pre>
 * for example 2 - 通过逻辑服 id 获取对应的 eventBus
 * <pre>{@code
 * BrokerClientContext brokerClientContext = flowContext.getBrokerClientContext();
 * String id = brokerClientContext.getId();
 * EventBus eventBus = EventBusRegion.getEventBus(id);
 * }
 * </pre>
 * for example 3 - 通过业务框架获取对应的 eventBus
 * <pre>{@code
 * BarSkeleton barSkeleton = ...
 * EventBus eventBus = barSkeleton.option(SkeletonAttr.eventBus);
 * }
 * </pre>
 * for example 4 - 在初始化时，自己保存一下引用
 * <pre>{@code
 * public BarSkeleton createBarSkeleton() {
 *     // 业务框架构建器
 *     var builder = ...
 *     // 游戏逻辑服添加 EventBusRunner，用于处理 EventBus 相关业务
 *     builder.addRunner(new EventBusRunner() {
 *         @Override
 *         public void registerEventBus(EventBus eventBus, BarSkeleton skeleton) {
 *            // 这里保存一下 EventBus 的引用
 *         }
 *     });
 * }
 * }
 * </pre>
 * fire 系列提供了多个种类的事件发布机制
 * <pre>
 *     1. fire 发送事件给订阅者，这些订阅者包括
 *         a. 给当前进程所有逻辑服的订阅者发送事件消息。
 *         b. 给其他进程的订阅者发送事件消息。
 *     2. fireLocal 给当前进程所有逻辑服的订阅者发送事件消息
 *     3. fireMe 仅给当前 EventBus 的订阅者发送事件消息
 *     4. fireAny 发送事件给订阅者，这些订阅者包括
 *         a. 给当前进程所有逻辑服的订阅者发送事件消息。
 *         b. 给其他进程的订阅者发送事件消息。
 *         c. 当有同类型的多个逻辑服实例时，只会给同类型其中的一个逻辑服发送事件。
 *
 *     fire 系列提供了多个种类的事件发布机制，以上方法默认是异步的，而相关同步方法则以 fireXXXSync 命名。
 * </pre>
 * 便捷使用 - {@link FlowContext}
 * <pre>
 *     除了可以通过 EventBus 发布事件外，框架还在 {@link FlowContext} 中提供了 EventBus 的相关方法。
 *     FlowContext 内部使用 EventBus 来发布事件。
 *     更多使用示例请阅读 <a href="https://www.yuque.com/iohao/game/zz8xiz#3c306ed1">FlowContext - 分布式事件总线</a>文档
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-12-24
 * @see FlowContext#getEventBus()
 * @see EventBusRegion
 * @see FlowContext
 * @since 21
 */
public interface EventBus {
    /**
     * EventBus id。EventBus、业务框架、逻辑服三者是 1:1:1 的关系，默认该 id 是逻辑服的 id;
     *
     * @return id
     */
    String getId();

    /**
     * 注册订阅者
     *
     * @param eventBusSubscriber 订阅者
     */
    void register(Object eventBusSubscriber);

    /**
     * 事件消息所对应的订阅者
     *
     * @param eventBusMessage 事件消息
     * @return 所对应的订阅者
     */
    Collection<Subscriber> listSubscriber(EventBusMessage eventBusMessage);

    /**
     * 当前 eventBus 订阅的所有事件源主题
     *
     * @return 当前 eventBus 订阅的所有事件源主题
     */
    Set<String> listTopic();

    /**
     * [异步] 发送事件给所有订阅者
     * <pre>
     *     1 给当前进程所有逻辑服的订阅者发送事件消息
     *     2 给其他进程的订阅者发送事件消息
     * </pre>
     *
     * @param eventBusMessage 事件消息
     */
    void fire(EventBusMessage eventBusMessage);

    /**
     * [同步] 发送事件给所有订阅者
     * <pre>
     *     1 [同步] 给当前进程所有逻辑服的订阅者发送事件消息
     *     2 [异步] 给其他进程的订阅者发送事件消息
     *
     *     注意，这里的同步仅指当前进程订阅者的同步，对其他进程中的订阅者无效（处理远程订阅者使用的是异步）。
     * </pre>
     *
     * @param eventBusMessage 事件消息
     */
    void fireSync(EventBusMessage eventBusMessage);

    /**
     * [异步] 发送事件给所有订阅者
     * <pre>
     *     1 给当前进程所有逻辑服的订阅者发送事件消息
     *     2 给其他进程的订阅者发送事件消息
     * </pre>
     *
     * @param eventSource 事件源
     */
    default void fire(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);
        this.fire(eventBusMessage);
    }

    /**
     * [同步] 发送事件给所有订阅者
     * <pre>
     *     1 [同步] 给当前进程所有逻辑服的订阅者发送事件消息
     *     2 [异步] 给其他进程的订阅者发送事件消息
     *
     *     注意，这里的同步仅指当前进程订阅者的同步，对其他进程中的订阅者无效（处理远程订阅者使用的是异步）。
     * </pre>
     *
     * @param eventSource 事件源
     */
    default void fireSync(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);
        this.fireSync(eventBusMessage);
    }

    /**
     * [异步] 给当前进程所有逻辑服的订阅者发送事件消息
     *
     * @param eventSource 事件源
     */
    default void fireLocal(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);
        this.fireLocal(eventBusMessage);
    }

    /**
     * [异步] 给当前进程所有逻辑服的订阅者发送事件消息
     *
     * @param eventBusMessage 事件消息
     */
    void fireLocal(EventBusMessage eventBusMessage);

    /**
     * [同步] 给当前进程所有逻辑服的订阅者发送事件消息
     *
     * @param eventSource 事件源
     */
    default void fireLocalSync(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);
        this.fireLocalSync(eventBusMessage);
    }

    /**
     * [同步] 给当前进程所有逻辑服的订阅者发送事件消息
     *
     * @param eventBusMessage 事件消息
     */
    void fireLocalSync(EventBusMessage eventBusMessage);

    /**
     * [异步] 仅给当前 EventBus 的订阅者发送事件消息
     * <pre>
     *     已注册到 {@link EventBus#register(Object)}  的订阅者
     * </pre>
     *
     * @param eventSource 事件源
     */
    default void fireMe(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);
        this.fireMe(eventBusMessage);
    }

    /**
     * [异步] 仅给当前 EventBus 的订阅者发送事件消息
     * <pre>
     *     已注册到 {@link EventBus#register(Object)}  的订阅者
     * </pre>
     *
     * @param eventBusMessage 事件消息
     */
    void fireMe(EventBusMessage eventBusMessage);

    /**
     * [同步] 仅给当前 EventBus 的订阅者发送事件消息
     * <pre>
     *     已注册到 {@link EventBus#register(Object)} 的订阅者
     * </pre>
     *
     * @param eventSource 事件源
     */
    default void fireMeSync(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);
        this.fireMeSync(eventBusMessage);
    }

    /**
     * [同步] 仅给当前 EventBus 的订阅者发送事件消息
     * <pre>
     *     已注册到 {@link EventBus#register(Object)} 的订阅者
     * </pre>
     *
     * @param eventBusMessage 事件消息
     */
    void fireMeSync(EventBusMessage eventBusMessage);

    /**
     * [异步] 给当前进程的订阅者和远程进程的订阅者送事件消息，如果同类型逻辑服存在多个，只会给其中一个实例发送。
     * <pre>
     *     假设现在有一个发放奖励的邮件逻辑服，我们启动了两个（或者说多个）邮件逻辑服实例来处理业务。
     *     当我们使用 fireAny 方法发送事件时，只会给其中一个实例发送事件。
     * </pre>
     *
     * @param eventSource 事件源
     */
    default void fireAny(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);
        this.fireAny(eventBusMessage);
    }

    /**
     * [异步] 给当前进程的订阅者和远程进程的订阅者送事件消息，如果同类型逻辑服存在多个，只会给其中一个实例发送。
     * <pre>
     *     假设现在有一个发放奖励的邮件逻辑服，我们启动了两个（或者说多个）邮件逻辑服实例来处理业务。
     *     当我们使用 fireAny 方法发送事件时，只会给其中一个实例发送事件。
     * </pre>
     *
     * @param eventBusMessage 事件消息
     */
    void fireAny(EventBusMessage eventBusMessage);

    /**
     * [同步] 给当前进程的订阅者和远程进程的订阅者送事件消息，如果同类型逻辑服存在多个，只会给其中一个实例发送。
     * <pre>
     *     假设现在有一个发放奖励的邮件逻辑服，我们启动了两个（或者说多个）邮件逻辑服实例来处理业务。
     *     当我们使用 fireAny 方法发送事件时，只会给其中一个实例发送事件。
     * </pre>
     *
     * @param eventSource 事件源
     */
    default void fireAnySync(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);
        this.fireAnySync(eventBusMessage);
    }

    /**
     * [同步] 给当前进程的订阅者和远程进程的订阅者送事件消息，如果同类型逻辑服存在多个，只会给其中一个实例发送。
     * <pre>
     *     假设现在有一个发放奖励的邮件逻辑服，我们启动了两个（或者说多个）邮件逻辑服实例来处理业务。
     *     当我们使用 fireAny 方法发送事件时，只会给其中一个实例发送事件。
     * </pre>
     *
     * @param eventBusMessage 事件消息
     */
    void fireAnySync(EventBusMessage eventBusMessage);

    /**
     * [异步] 给当前进程其他逻辑服的订阅者发送事件消息，不包括当前 EventBus。
     *
     * @param eventSource 事件源
     */
    default void fireLocalNeighbor(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);
        this.fireLocalNeighbor(eventBusMessage);
    }

    /**
     * [异步] 给当前进程其他逻辑服的订阅者发送事件消息，不包括当前 EventBus。
     *
     * @param eventBusMessage 事件消息
     */
    void fireLocalNeighbor(EventBusMessage eventBusMessage);

    /**
     * [同步] 给当前进程其他逻辑服的订阅者发送事件消息，不包括当前 EventBus。
     *
     * @param eventSource 事件源
     */
    default void fireLocalNeighborSync(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);
        this.fireLocalNeighborSync(eventBusMessage);
    }

    /**
     * [同步] 给当前进程其他逻辑服的订阅者发送事件消息，不包括当前 EventBus。
     *
     * @param eventBusMessage 事件消息
     */
    void fireLocalNeighborSync(EventBusMessage eventBusMessage);

    /**
     * set 订阅者线程执行器选择策略
     *
     * @param subscribeExecutorStrategy 订阅者线程执行器选择策略
     */
    void setSubscribeExecutorStrategy(SubscribeExecutorStrategy subscribeExecutorStrategy);

    /**
     * get 订阅者线程执行器选择策略
     *
     * @return 订阅者线程执行器选择策略
     */
    SubscribeExecutorStrategy getSubscribeExecutorStrategy();

    /**
     * set SubscriberInvokeCreator
     *
     * @param subscriberInvokeCreator SubscriberInvokeCreator
     */
    void setSubscriberInvokeCreator(SubscriberInvokeCreator subscriberInvokeCreator);

    /**
     * set 事件消息创建者，EventBusMessage creator
     *
     * @param eventBusMessageCreator EventBusMessageCreator
     */
    void setEventBusMessageCreator(EventBusMessageCreator eventBusMessageCreator);

    /**
     * set 事件监听器
     *
     * @param eventBusListener 事件监听器
     */
    void setEventBusListener(EventBusListener eventBusListener);

    /**
     * get 事件监听器
     *
     * @return 事件监听器
     */
    EventBusListener getEventBusListener();

    /**
     * set 事件总线逻辑服相关信息
     *
     * @param eventBrokerClientMessage 事件总线逻辑服相关信息
     */
    void setEventBrokerClientMessage(EventBrokerClientMessage eventBrokerClientMessage);

    /**
     * 当前服务器上下文（逻辑服）
     *
     * @param brokerClientContext 当前服务器上下文（逻辑服）
     */
    void setBrokerClientContext(BrokerClientContext brokerClientContext);

    /**
     * set 线程执行器管理域
     *
     * @param executorRegion 线程执行器管理域
     */
    void setExecutorRegion(ExecutorRegion executorRegion);

    /**
     * get 线程执行器管理域
     *
     * @return 线程执行器管理域
     */
    ExecutorRegion getExecutorRegion();

    /**
     * get 事件消息创建者
     *
     * @return 事件消息创建者
     */
    EventBusMessageCreator getEventBusMessageCreator();

    /**
     * get 事件总线逻辑服相关信息
     *
     * @return 事件总线逻辑服相关信息
     */
    EventBrokerClientMessage getEventBrokerClientMessage();

    /**
     * 创建事件消息
     *
     * @param eventSource 事件源
     * @return 事件消息
     */
    default EventBusMessage createEventBusMessage(Object eventSource) {
        return this.getEventBusMessageCreator().create(eventSource);
    }
}