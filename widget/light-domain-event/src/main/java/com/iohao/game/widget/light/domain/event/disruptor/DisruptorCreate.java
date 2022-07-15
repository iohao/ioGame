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

import com.iohao.game.widget.light.domain.event.DomainEventContextParam;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * 领域事件构建接口
 * <pre>
 *     创建disruptor
 * </pre>
 *
 * @author 渔民小镇
 * @date 2021-12-26
 */
public interface DisruptorCreate {
    /**
     * 根据topic（领域消息主题）创建disruptor
     *
     * @param topic 主题
     * @param param param
     * @return Disruptor
     */
    Disruptor<EventDisruptor> createDisruptor(Class<?> topic, DomainEventContextParam param);
}
