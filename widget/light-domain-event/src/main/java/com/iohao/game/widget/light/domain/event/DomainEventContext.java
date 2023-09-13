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
package com.iohao.game.widget.light.domain.event;

import com.iohao.game.widget.light.domain.event.disruptor.ConsumeEventHandler;
import com.iohao.game.widget.light.domain.event.disruptor.DisruptorCreate;
import com.iohao.game.widget.light.domain.event.disruptor.EventDisruptor;
import com.iohao.game.widget.light.domain.event.message.DomainEventHandler;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 领域事件上下文
 *
 * @author 渔民小镇
 * @date 2021-12-26
 */
@Slf4j
public class DomainEventContext {

    final DomainEventContextParam param;

    public DomainEventContext(DomainEventContextParam param) {
        this.param = param;
    }

    /**
     * <pre>
     * 初始化配置，Disruptor
     * 一个topic只能对应一个disruptor，且只有第一个有效，后面添加的将无效。
     * 如果并发与队列事件同时存在（就是topic是同一个名字），则优先保存并发的topic的。队列的将无效
     * </pre>
     *
     * @return true 成功启动
     */
    @SuppressWarnings("unchecked")
    public boolean startup() {
        AtomicBoolean init = param.getInit();

        if (!init.compareAndSet(false, true)) {
            return true;
        }

        Set<DomainEventHandler<?>> domainEventHandlerSet = param.domainEventHandlerSet;
        domainEventHandlerSet.stream().collect(Collectors.groupingBy(o -> {
            // 一个 key 对应多个 value. key 是从领域事件中的接口类型中查找领域实体类型
            ParameterizedType parameterizedType = (ParameterizedType) o.getClass().getGenericInterfaces()[0];
            Type type = parameterizedType.getActualTypeArguments()[0];
            String typeName = type.getTypeName();

            try {
                return Class.forName(typeName);
            } catch (ClassNotFoundException e) {

                try {
                    // 尝试从当前线程的类加载器中加载；#194
                    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                    return Class.forName(typeName, true, contextClassLoader);
                } catch (ClassNotFoundException e1) {
                    log.error(e1.getMessage(), e1);
                }

                return null;
            }
        })).forEach((Class<?> topic, List<DomainEventHandler<?>> eventHandlers) -> {

            // 创建 disruptor；并发事件消费 - 无顺序的执行事件消费
            DisruptorCreate disruptorCreate = param.disruptorCreate;
            Disruptor<EventDisruptor> disruptor = disruptorCreate.createDisruptor(topic, param);
            DisruptorManager.me().put(topic, disruptor);

            if (Objects.nonNull(param.exceptionHandler)) {
                disruptor.setDefaultExceptionHandler(param.exceptionHandler);
            }

            // disruptor 绑定领域事件消费接口，事件消费绑定
            eventHandlers.forEach(eventHandler -> {
                // 事件消费绑定
                var consumeEventHandler = new ConsumeEventHandler(eventHandler);
                disruptor.handleEventsWith(consumeEventHandler);
            });
        });

        // 启动disruptor
        DisruptorManager.me().forEach(Disruptor::start);

        domainEventHandlerSet.clear();

        return init.get();
    }

    /**
     * 停止Disruptor
     * <pre>
     *     把环形数组中的事件执行完后停止, 不在接受新的事件
     * </pre>
     *
     * @return true 停止成功
     */
    public boolean stop() {

        DisruptorManager.me().listDisruptor().removeIf(disruptor -> {
            disruptor.shutdown();
            return true;
        });

        return true;
    }


}
