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

import com.lmax.disruptor.RingBuffer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * 事件订阅发送 {@link EventDisruptor} 给 {@link RingBuffer}
 *
 * @author 渔民小镇
 * @date 2021-12-26
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class EventDisruptor {

    /**
     * 没有实现 {@link DomainEventSource} 接口的对象
     */
    Object value;

    /**
     * 是否包装的领域事件
     * <pre>
     *     true 实现了 {@link DomainEventSource} 接口的对象
     *     false 没有实现 {@link DomainEventSource} 接口的对象
     * </pre>
     */
    @Setter
    @Getter
    boolean eventSource = true;

    /**
     * 领域事件
     */
    DomainEventSource domainEventSource;

    /**
     * 获取领域事件源对象
     *
     * @param <T> source
     * @return 事件源对象
     */
    @SuppressWarnings("unchecked")
    public <T> T getDomainEventSource() {
        return (T) domainEventSource;
    }

    /**
     * 设置领域事件源
     *
     * @param domainEventSource 领域事件源
     */
    public void setDomainEventSource(DomainEventSource domainEventSource) {
        this.eventSource = true;
        this.domainEventSource = domainEventSource;
    }

    /**
     * 没有实现 {@link DomainEventSource} 接口的对象
     *
     * @param <T> T
     * @return value
     */
    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        return (T) value;
    }

    /**
     * 没有实现 {@link DomainEventSource} 接口的对象
     *
     * @param value value
     */
    public void setValue(Object value) {
        this.eventSource = false;
        this.value = value;
    }
}
