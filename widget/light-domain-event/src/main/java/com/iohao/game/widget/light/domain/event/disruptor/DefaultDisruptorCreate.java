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
package com.iohao.game.widget.light.domain.event.disruptor;

import com.iohao.game.widget.light.domain.event.DisruptorManager;
import com.iohao.game.widget.light.domain.event.DomainEventContextParam;
import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 默认的 领域事件构建接口 实现类
 *
 * @author 渔民小镇
 * @date 2021-12-26
 */
@Slf4j
public class DefaultDisruptorCreate implements DisruptorCreate {
    static final AtomicInteger THREAD_INIT_NUMBER = new AtomicInteger(1);

    @Override
    public Disruptor<EventDisruptor> createDisruptor(Class<?> topic, DomainEventContextParam param) {
        int ringBufferSize = param.getRingBufferSize();
        ProducerType producerType = param.getProducerType();
        WaitStrategy waitStrategy = param.getWaitStrategy();
        // 自定义线程工厂
        ThreadFactory threadFactory = createThreadFactory(topic);

        return new Disruptor<>(EventDisruptor::new, ringBufferSize, threadFactory, producerType, waitStrategy);
    }

    private ThreadFactory createThreadFactory(Class<?> topic) {
        return r -> {
            String domainEventHandlerName = getName(r);

            List<String> nameParamList = new ArrayList<>();
            // 线程名前缀
            nameParamList.add(DisruptorManager.me().getThreadNamePrefix());
            // 主题名
            nameParamList.add(topic.getSimpleName());
            // 领域事件名
            nameParamList.add(domainEventHandlerName);
            // 编号
            nameParamList.add(String.valueOf(THREAD_INIT_NUMBER.getAndIncrement()));

            // 组合成线程名
            String threadName = String.join("-", nameParamList);

            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName(threadName);

            return thread;
        };
    }

    private String getName(Runnable r) {

        String domainEventHandlerName = "";

        if (r instanceof BatchEventProcessor eventProcessor) {
            try {
                Field eventHandler = BatchEventProcessor.class.getDeclaredField("eventHandler");
                eventHandler.setAccessible(true);
                Object o = eventHandler.get(eventProcessor);

                if (o instanceof ConsumeEventHandler consumeEventHandler) {
                    domainEventHandlerName = consumeEventHandler.eventHandler().getName();
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.error(e.getMessage(), e);
            }

            return domainEventHandlerName;
        }

        return domainEventHandlerName;
    }
}