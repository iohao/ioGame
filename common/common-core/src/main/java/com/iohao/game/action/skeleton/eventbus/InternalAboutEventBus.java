/*
 * ioGame
 * Copyright (C) 2021 - 2024  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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

import com.esotericsoftware.reflectasm.MethodAccess;
import com.iohao.game.common.kit.concurrent.executor.ExecutorRegion;
import com.iohao.game.common.kit.trace.TraceKit;
import com.iohao.game.common.kit.concurrent.executor.ExecutorRegionKit;
import com.iohao.game.common.kit.concurrent.executor.ThreadExecutor;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
final class DefaultEventBusListener implements EventBusListener {
    @Override
    public void invokeException(Throwable e, Object eventSource, EventBusMessage eventBusMessage) {
        log.error(e.getMessage(), e);
    }

    @Override
    public void emptySubscribe(EventBusMessage eventBusMessage, EventBus eventBus) {
        Class<?> clazz = eventBusMessage.getTopicClass();
        String simpleName = eventBusMessage.getTopic();
        log.warn("事件源[{}]没有配置订阅者 {}", clazz.getSimpleName(), simpleName);
    }

    static DefaultEventBusListener me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final DefaultEventBusListener ME = new DefaultEventBusListener();
    }
}

final class DefaultEventBusMessageCreator implements EventBusMessageCreator {
    @Override
    public EventBusMessage create(Object eventSource) {

        EventBusMessage eventBusMessage = new EventBusMessage();
        eventBusMessage.setEventSource(eventSource);

        // traceId
        String traceId = MDC.get(TraceKit.traceName);
        eventBusMessage.setTraceId(traceId);

        return eventBusMessage;
    }

    static DefaultEventBusMessageCreator me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final DefaultEventBusMessageCreator ME = new DefaultEventBusMessageCreator();
    }
}

final class DefaultSubscribeExecutorStrategy implements SubscribeExecutorStrategy {
    static final AtomicLong threadIndexNo = new AtomicLong();

    @Override
    public ThreadExecutor select(Subscriber subscriber, EventBusMessage eventBusMessage, ExecutorRegion executorRegion) {

        ExecutorSelector executorSelect = subscriber.getExecutorSelect();

        // 虚拟线程中执行
        if (executorSelect == ExecutorSelector.userVirtualExecutor) {
            long threadIndex = getThreadIndex(eventBusMessage);
            return executorRegion.getUserVirtualThreadExecutor(threadIndex);
        }

        // [线程安全] 用户线程中执行
        if (executorSelect == ExecutorSelector.userExecutor) {
            long threadIndex = getThreadIndex(eventBusMessage);
            return executorRegion.getUserThreadExecutor(threadIndex);
        }

        // [线程安全] 相同的订阅者使用同一个线程执行器
        if (executorSelect == ExecutorSelector.methodExecutor) {
            long threadIndex = subscriber.id;
            return executorRegion.getSimpleThreadExecutor(threadIndex);
        }

        long threadIndex = getThreadIndex(eventBusMessage);
        return executorRegion.getSimpleThreadExecutor(threadIndex);
    }

    long getThreadIndex(EventBusMessage eventBusMessage) {
        long userId = eventBusMessage.getThreadIndex();

        if (userId != 0) {
            return userId;
        }

        return threadIndexNo.incrementAndGet();
    }

    static DefaultSubscribeExecutorStrategy me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final DefaultSubscribeExecutorStrategy ME = new DefaultSubscribeExecutorStrategy();
    }
}

record DefaultSubscriberInvoke(Subscriber subscriber) implements SubscriberInvoke {
    @Override
    public void invoke(EventBusMessage eventBusMessage) {
        var eventSource = eventBusMessage.getEventSource();
        var traceId = eventBusMessage.getTraceId();

        final boolean test = Objects.isNull(traceId) || Objects.nonNull(MDC.get(TraceKit.traceName));
        if (test) {
            this.invoke(eventSource);
            return;
        }

        try {
            // traceId
            MDC.put(TraceKit.traceName, traceId);
            this.invoke(eventSource);
        } finally {
            MDC.clear();
        }
    }

    void invoke(Object param) {
        MethodAccess methodAccess = this.subscriber.getMethodAccess();
        Object target = this.subscriber.getTarget();
        int methodIndex = this.subscriber.getMethodIndex();

        // 调用开发者在 action 类中编写的业务方法，即 action
        methodAccess.invoke(target, methodIndex, param);
    }
}

final class DefaultSubscriberInvokeCreator implements SubscriberInvokeCreator {

    @Override
    public SubscriberInvoke create(Subscriber subscriber) {
        return new DefaultSubscriberInvoke(subscriber);
    }

    static DefaultSubscriberInvokeCreator me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final DefaultSubscriberInvokeCreator ME = new DefaultSubscriberInvokeCreator();
    }
}

@UtilityClass
class EventBusKit {
    void executeSafe(Runnable runnable) {
        ExecutorRegionKit.getSimpleThreadExecutor(1).executeTry(runnable);
    }

    void sort(List<Subscriber> subscribers) {
        // order 排序
        subscribers.sort((o1, o2) -> o2.order - o1.order);
    }
}