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

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import com.iohao.game.action.skeleton.core.IoGameCommonCoreConfig;
import com.iohao.game.action.skeleton.core.commumication.BrokerClientContext;
import com.iohao.game.common.kit.CollKit;
import com.iohao.game.common.kit.MoreKit;
import com.iohao.game.common.kit.collect.ListMultiMap;
import com.iohao.game.common.kit.collect.SetMultiMap;
import com.iohao.game.common.kit.concurrent.executor.ExecutorRegion;
import com.iohao.game.common.kit.exception.ThrowKit;
import com.iohao.game.common.kit.trace.TraceKit;
import com.iohao.game.common.kit.concurrent.executor.ExecutorRegionKit;
import com.iohao.game.common.kit.concurrent.executor.ThreadExecutor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;
import org.jctools.maps.NonBlockingHashSet;
import org.slf4j.MDC;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Object target = this.subscriber.getTarget();
        int methodIndex = this.subscriber.getMethodIndex();

        MethodAccess methodAccess = this.subscriber.getMethodAccess();
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

@UtilityClass
final class EventBusLocalRegion {
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
    final ListMultiMap<Class<?>, Subscriber> subscriberListMap = ListMultiMap.of();

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
        eventBusMap.put(eventBus.getId(), eventBus);

        EventBusKit.executeSafe(EventBusLocalRegion::resetLocalSubscriber);
    }

    private void resetLocalSubscriber() {

        ListMultiMap<Class<?>, Subscriber> tempMultiMap = ListMultiMap.of();
        for (EventBus eventBus : eventBusMap.values()) {
            if (eventBus instanceof DefaultEventBus defaultEventBus) {
                SubscriberRegistry subscriberRegistry = defaultEventBus.subscriberRegistry;
                var multiMap = subscriberRegistry.subscriberMultiMap;

                if (multiMap.isEmpty()) {
                    continue;
                }

                for (var entry : multiMap.entrySet()) {
                    Class<?> key = entry.getKey();
                    tempMultiMap.of(key).addAll(entry.getValue());
                }
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

@UtilityClass
final class EventBusAnyTagRegion {
    /**
     * key : EventBrokerClientMessage tag
     */
    Map<BrokerClientTag, AnyTagBrokerClient> map = new NonBlockingHashMap<>();

    AnyTagView anyTagView = new AnyTagView();

    AnyTagViewData getAnyTagData(EventBusMessage message) {
        return anyTagView.getAnyTagData(message);
    }

    void add(EventBrokerClientMessage eventBrokerClientMessage) {
        // 将当前进程和远程的 EventBrokerClientMessage 添加到对应的 tag 中
        BrokerClientTag tag = BrokerClientTag.of(eventBrokerClientMessage.tag);

        AnyTagBrokerClient anyTagBrokerClient = getAnyTagBrokerClient(tag);
        anyTagBrokerClient.add(eventBrokerClientMessage);

        reload();
    }

    void remove(EventBrokerClientMessage eventBrokerClientMessage) {
        BrokerClientTag tag = BrokerClientTag.of(eventBrokerClientMessage.tag);

        AnyTagBrokerClient anyTagBrokerClient = getAnyTagBrokerClient(tag);
        anyTagBrokerClient.remove(eventBrokerClientMessage);

        // 如果 tag 没有任何数据时，从 map 中移除
        if (anyTagBrokerClient.isEmpty()) {
            map.remove(tag);
        }

        reload();
    }

    private AnyTagBrokerClient getAnyTagBrokerClient(BrokerClientTag tag) {
        AnyTagBrokerClient anyTagBrokerClient = map.get(tag);

        if (Objects.isNull(anyTagBrokerClient)) {
            AnyTagBrokerClient region = new AnyTagBrokerClient();
            return MoreKit.firstNonNull(map.putIfAbsent(tag, region), region);
        }

        return anyTagBrokerClient;
    }

    private void reload() {
        // 重新加载视图
        EventBusKit.executeSafe(() -> anyTagView.reload(map.values()));
    }
}

@UtilityClass
final class EventBusRemoteRegion {
    /**
     * 其他进程的订阅者
     * <pre>
     *     key : eventSource class name
     *     value : across progress id
     * </pre>
     */
    final SetMultiMap<String, EventBrokerClientMessage> remoteTopicMultiMap = SetMultiMap.of();

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

/**
 * 订阅者注册
 *
 * @author 渔民小镇
 * @date 2023-12-24
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PACKAGE)
final class SubscriberRegistry {
    static final AtomicLong subscriberId = new AtomicLong();
    final ListMultiMap<Class<?>, Subscriber> subscriberMultiMap = ListMultiMap.of();
    final Set<Class<?>> eventBusSubscriberSet = new NonBlockingHashSet<>();

    EventBus eventBus;

    void register(Object eventBusSubscriber, SubscriberInvokeCreator subscriberInvokeCreator) {

        Class<?> clazz = eventBusSubscriber.getClass();

        if (!eventBusSubscriberSet.add(clazz)) {
            ThrowKit.ofRuntimeException("已经存在 " + clazz);
        }

        // 方法访问器: 获取类中自己定义的方法
        var methodAccess = MethodAccess.get(clazz);
        var constructorAccess = ConstructorAccess.get(clazz);

        streamMethod(clazz).forEach(method -> {
            var parameter = method.getParameters()[0];

            // 方法名
            String methodName = method.getName();
            // 方法下标
            var parameterClass = parameter.getType();
            int methodIndex = methodAccess.getIndex(methodName, parameterClass);

            // 订阅者注解相关
            var annotation = method.getAnnotation(EventSubscribe.class);
            var executorSelector = annotation.value();
            int order = Math.abs(annotation.order());

            Subscriber subscriber = new Subscriber(subscriberId.incrementAndGet())
                    .setMethodAccess(methodAccess)
                    .setConstructorAccess(constructorAccess)
                    .setMethodName(methodName)
                    .setMethod(method)
                    .setMethodIndex(methodIndex)
                    .setTargetClazz(clazz)
                    .setTarget(eventBusSubscriber)
                    .setParameterName(parameter.getName())
                    .setParameterClass(parameterClass)
                    .setOrder(order)
                    .setEventBus(eventBus)
                    .setExecutorSelect(executorSelector);

            SubscriberInvoke subscriberInvoke = subscriberInvokeCreator.create(subscriber);
            subscriber.setSubscriberInvoke(subscriberInvoke);

            this.subscriberMultiMap.put(parameterClass, subscriber);
        });

        this.subscriberMultiMap.asMap().values().forEach(EventBusKit::sort);
    }

    Collection<Class<?>> listEventSourceClass() {
        return this.subscriberMultiMap.keySet();
    }

    Collection<Subscriber> listSubscriber(EventBusMessage eventBusMessage) {

        Class<?> methodParamClazz = eventBusMessage.getTopicClass();

        return this.subscriberMultiMap.containsKey(methodParamClazz)
                ? this.subscriberMultiMap.get(methodParamClazz)
                : Collections.emptyList();
    }

    private Stream<Method> streamMethod(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                // 添加了订阅者注解的方法
                .filter(method -> Objects.nonNull(method.getAnnotation(EventSubscribe.class)))
                .filter(method -> {
                    // 访问权限必须是 public 的
                    boolean isPublic = Modifier.isPublic(method.getModifiers());
                    var notStatic = !Modifier.isStatic(method.getModifiers());
                    var onceParam = method.getParameters().length == 1;
                    var isVoid = method.getReturnType() == Void.TYPE;
                    /*
                     * 访问权限必须是 public 的
                     * 不能是静态方法的
                     * 方法必须有一个参数
                     * 方法返回值必须为 void
                     */
                    var result = isPublic && notStatic && onceParam && isVoid;

                    if (!result) {
                        log.warn("不是标准的订阅方法 {}", method);
                    }

                    return result;
                })
                // 订阅者必须且只能有一个参数，用于接收事件源
                .filter(method -> method.getParameters().length == 1)
                ;
    }
}

enum EventBusStatus {
    register,
    run
}

@Slf4j
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PACKAGE)
final class DefaultEventBus implements EventBus {
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
    /** 与业务框架所关联的线程执行器管理域 */
    ExecutorRegion executorRegion;

    EventBusStatus status = EventBusStatus.register;

    DefaultEventBus(String id) {
        this.id = Objects.requireNonNull(id);
    }

    @Override
    public void register(Object eventBusSubscriber) {

        if (status != EventBusStatus.register) {
            ThrowKit.ofRuntimeException("运行中不允许注册订阅者，请在 EventRunner.registerEventBus 方法中注册。 ");
        }

        // 注册
        this.subscriberRegistry.eventBus = this;
        this.subscriberRegistry.register(eventBusSubscriber, this.subscriberInvokeCreator);
    }

    @Override
    public EventBusMessage createEventBusMessage(Object eventSource) {
        return this.eventBusMessageCreator.create(eventSource);
    }

    @Override
    public Set<String> listTopic() {
        // 当前 eventBus 订阅的所有事件源主题
        return this.subscriberRegistry
                .listEventSourceClass()
                .stream()
                .map(Class::getName)
                .collect(Collectors.toSet());
    }

    @Override
    public void fire(EventBusMessage eventBusMessage) {
        // 给当前进程所有逻辑服的订阅者发送事件消息
        this.fireLocal(eventBusMessage);
        // 给其他进程的订阅者发送事件
        this.fireRemote(eventBusMessage);

        if (eventBusMessage.emptyFireType()) {
            this.eventBusListener.emptySubscribe(eventBusMessage, this);
        }
    }

    @Override
    public void fireSync(EventBusMessage eventBusMessage) {
        // 给当前进程所有逻辑服的订阅者发送事件消息
        this.fireLocalSync(eventBusMessage);
        // 给其他进程的订阅者发送事件
        this.fireRemote(eventBusMessage);

        if (eventBusMessage.emptyFireType()) {
            this.eventBusListener.emptySubscribe(eventBusMessage, this);
        }
    }

    @Override
    public void fire(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);
        this.fire(eventBusMessage);
    }

    @Override
    public void fireSync(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);
        this.fireSync(eventBusMessage);
    }

    @Override
    public void fireLocal(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);
        this.fireLocal(eventBusMessage);
    }

    @Override
    public void fireLocal(EventBusMessage eventBusMessage) {
        this.fireLocal(eventBusMessage, true);
    }

    @Override
    public void fireLocalSync(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);
        this.fireLocalSync(eventBusMessage);
    }

    @Override
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
        var messages = EventBusRegion.listRemoteEventBrokerClientMessage(eventBusMessage);

        this.fireRemote(eventBusMessage, messages);
    }

    void fireRemote(EventBusMessage eventBusMessage, Collection<EventBrokerClientMessage> messages) {
        if (CollKit.isEmpty(messages)) {
            return;
        }

        // 如果其他进程中存在当前事件源的订阅者，将事件源发布到其他进程中
        eventBusMessage.setEventBrokerClientMessages(messages);
        eventBusMessage.addFireType(EventBusFireType.fireRemote);

        this.extractedPrint(eventBusMessage);

        try {
            this.brokerClientContext.oneway(eventBusMessage);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void fireMe(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);
        this.fireMe(eventBusMessage);
    }

    @Override
    public void fireMe(EventBusMessage eventBusMessage) {
        this.fireMe(eventBusMessage, true);
    }

    @Override
    public void fireMeSync(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);
        this.fireMeSync(eventBusMessage);
    }

    @Override
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

    @Override
    public void fireAny(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);
        this.fireAny(eventBusMessage);
    }

    @Override
    public void fireAny(EventBusMessage eventBusMessage) {
        AnyTagViewData anyTagViewData = EventBusAnyTagRegion.getAnyTagData(eventBusMessage);

        List<EventBrokerClientMessage> messages = anyTagViewData.getLocalMessages();
        this.fireAny(eventBusMessage, messages, true);

        List<EventBrokerClientMessage> remoteMessages = anyTagViewData.getRemoteMessages();
        this.fireRemote(eventBusMessage, remoteMessages);

        if (eventBusMessage.emptyFireType()) {
            this.eventBusListener.emptySubscribe(eventBusMessage, this);
        }
    }

    @Override
    public void fireAnySync(Object eventSource) {
        EventBusMessage eventBusMessage = this.createEventBusMessage(eventSource);
        this.fireAnySync(eventBusMessage);
    }

    @Override
    public void fireAnySync(EventBusMessage eventBusMessage) {
        AnyTagViewData anyTagViewData = EventBusAnyTagRegion.getAnyTagData(eventBusMessage);

        List<EventBrokerClientMessage> messages = anyTagViewData.getLocalMessages();
        this.fireAny(eventBusMessage, messages, false);

        List<EventBrokerClientMessage> remoteMessages = anyTagViewData.getRemoteMessages();
        this.fireRemote(eventBusMessage, remoteMessages);

        if (eventBusMessage.emptyFireType()) {
            this.eventBusListener.emptySubscribe(eventBusMessage, this);
        }
    }

    void fireAny(EventBusMessage eventBusMessage, List<EventBrokerClientMessage> list, boolean async) {

        if (CollKit.isEmpty(list)) {
            return;
        }

        for (EventBrokerClientMessage brokerClientMessage : list) {
            EventBus eventBus = EventBusRegion.getEventBus(brokerClientMessage.brokerClientId);
            if (eventBus instanceof DefaultEventBus defaultEventBus) {
                defaultEventBus.fireMe(eventBusMessage, async);
            }
        }
    }

    @Override
    public void fireLocalNeighbor(EventBusMessage eventBusMessage) {
        this.fireLocalNeighbor(eventBusMessage, true);
    }

    @Override
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
        if (!EventBusRegion.hasLocalNeighbor()) {
            // 当前进程仅有一个逻辑服
            return;
        }

        var subscribers = EventBusRegion.streamLocalEventBus()
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
            for (EventBrokerClientMessage eventBrokerClientMessage : eventBusMessage.getEventBrokerClientMessages()) {
                log.info("远程逻辑服 : {}", eventBrokerClientMessage.getAppName());
            }

            System.out.println();
        }
    }

    private void invokeSubscriber(EventBusMessage eventBusMessage, boolean async, Collection<Subscriber> subscribers) {
        if (async) {
            // 异步执行
            for (Subscriber subscriber : subscribers) {
                EventBus eventBus = subscriber.getEventBus();
                ExecutorRegion executorRegion = eventBus.getExecutorRegion();

                // 根据策略得到对应的执行器
                SubscribeExecutorStrategy executorStrategy = eventBus.getSubscribeExecutorStrategy();
                ThreadExecutor threadExecutor = executorStrategy.select(subscriber, eventBusMessage, executorRegion);
                threadExecutor.execute(() -> this.invoke(subscriber, eventBusMessage));
            }
        } else {
            // 同步执行
            for (Subscriber subscriber : subscribers) {
                this.invoke(subscriber, eventBusMessage);
            }
        }
    }

    private void invoke(Subscriber subscriber, EventBusMessage eventBusMessage) {
        try {
            SubscriberInvoke subscriberInvoke = subscriber.getSubscriberInvoke();
            subscriberInvoke.invoke(eventBusMessage);
        } catch (Throwable e) {
            EventBus eventBus = subscriber.getEventBus();
            EventBusListener eventBusListener = eventBus.getEventBusListener();
            eventBusListener.invokeException(e, eventBusMessage.getEventSource(), eventBusMessage);
        }
    }

    @Override
    public Collection<Subscriber> listSubscriber(EventBusMessage eventBusMessage) {
        return this.subscriberRegistry.listSubscriber(eventBusMessage);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof DefaultEventBus that)) {
            return false;
        }

        return this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}