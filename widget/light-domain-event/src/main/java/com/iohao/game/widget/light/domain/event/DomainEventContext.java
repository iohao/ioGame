/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License..
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
        if (init.get()) {
            return init.get();
        }

        init.set(true);

        // 并发事件消费 - 无顺序的执行事件消费
        DisruptorCreate disruptorCreate = param.disruptorCreate;

        Set<DomainEventHandler<?>> domainEventHandlerSet = param.domainEventHandlerSet;
        domainEventHandlerSet.parallelStream().collect(Collectors.groupingBy(o -> {
            // 一个key对应多个value. key 是从领域事件中的接口类型中查找领域实体类型
            Type type = ((ParameterizedType) o.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
            try {
                String name = type.getTypeName();
                return Class.forName(name);
            } catch (ClassNotFoundException e) {
                log.error(e.getMessage(), e);
                return null;
            }
        })).forEach((Class<?> topic, List<DomainEventHandler<?>> eventHandlers) -> {

            // 创建disruptor
            Disruptor<EventDisruptor> disruptor = disruptorCreate.createDisruptor(topic, param);
            DisruptorManager.me().put(topic, disruptor);

            if (Objects.nonNull(param.exceptionHandler)) {
                disruptor.setDefaultExceptionHandler(param.exceptionHandler);
            }

            // disruptor 绑定领域事件消费接口
            eventHandlers.forEach(eventHandler -> {
                // 事件消费绑定
                disruptor.handleEventsWith(new ConsumeEventHandler(eventHandler));
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
