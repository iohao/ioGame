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

import com.iohao.game.widget.light.domain.event.disruptor.EventDisruptor;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * 负责 Disruptor 的管理
 *
 * @author 渔民小镇
 * @date 2021-12-26
 */
public class DisruptorManager {
    /**
     * 事件主题
     * <pre>
     *     key : topic
     *     value : 事件订阅
     * </pre>
     */
    private final Map<Class<?>, Disruptor<EventDisruptor>> topicMap = new ConcurrentHashMap<>();

    /** 领域事件线程名前缀 */
    @Setter
    @Getter
    private String threadNamePrefix = "iohao.domain";

    /**
     * 获取所有Disruptor
     *
     * @return Disruptor集合
     */
    Collection<Disruptor<EventDisruptor>> listDisruptor() {
        return topicMap.values();
    }

    void forEach(Consumer<Disruptor<EventDisruptor>> action) {
        listDisruptor().parallelStream().forEach(action);
    }

    /**
     * 获取领域消息主题对应的Disruptor
     *
     * @param topic 领域消息主题
     * @return Disruptor
     */
    Disruptor<EventDisruptor> getDisruptor(final Class<?> topic) {
        return topicMap.get(topic);
    }

    void put(Class<?> topic, Disruptor<EventDisruptor> disruptor) {
        topicMap.putIfAbsent(topic, disruptor);
    }

    private DisruptorManager() {
    }

    public static DisruptorManager me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final DisruptorManager ME = new DisruptorManager();
    }

}
