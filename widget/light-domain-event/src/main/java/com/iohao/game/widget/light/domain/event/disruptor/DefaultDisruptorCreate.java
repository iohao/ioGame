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
        ThreadFactory threadFactory = r -> {
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

        return new Disruptor<>(EventDisruptor::new, ringBufferSize, threadFactory, producerType, waitStrategy);
    }

    private String getName(Runnable r) {
        if (!(r instanceof BatchEventProcessor)) {
            return "";
        }

        String domainEventHandlerName = "";
        BatchEventProcessor eventProcessor = (BatchEventProcessor) r;

        try {
            Field eventHandler = BatchEventProcessor.class.getDeclaredField("eventHandler");
            eventHandler.setAccessible(true);
            Object o = eventHandler.get(eventProcessor);

            if (o instanceof ConsumeEventHandler) {
                ConsumeEventHandler consumeEventHandler = (ConsumeEventHandler) o;
                domainEventHandlerName = consumeEventHandler.eventHandler().getName();
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
        return domainEventHandlerName;
    }
}