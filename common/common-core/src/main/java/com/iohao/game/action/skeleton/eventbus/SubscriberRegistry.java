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

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import com.iohao.game.common.kit.collect.ListMultiMap;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashSet;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Stream;

/**
 * 订阅者注册
 *
 * @author 渔民小镇
 * @date 2023-12-24
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PACKAGE)
final class SubscriberRegistry {
    static final LongAdder threadIndexAdder = new LongAdder();
    final ListMultiMap<Class<?>, Subscriber> subscriberMultiMap = ListMultiMap.create();
    final Set<Class<?>> eventBusSubscriberSet = new NonBlockingHashSet<>();

    void register(Object eventBusSubscriber) {

        Class<?> clazz = eventBusSubscriber.getClass();

        if (!eventBusSubscriberSet.add(clazz)) {
            throw new RuntimeException("已经存在 " + clazz);
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

            threadIndexAdder.increment();

            Subscriber subscriber = new Subscriber(threadIndexAdder.longValue())
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
                    .setExecutorSelect(executorSelector);

            this.subscriberMultiMap.put(parameterClass, subscriber);
        });

        this.subscriberMultiMap.asMap().values().forEach(EventBusRegion::sort);
    }

    Collection<Class<?>> listEventSourceClass() {
        return this.subscriberMultiMap.keySet();
    }

    Collection<Subscriber> listSubscriber(Object eventSource) {
        Class<?> methodParamClazz = eventSource.getClass();

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
