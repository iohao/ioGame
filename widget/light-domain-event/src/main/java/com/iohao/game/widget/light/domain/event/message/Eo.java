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
package com.iohao.game.widget.light.domain.event.message;

import com.iohao.game.widget.light.domain.event.DomainEventPublish;
import com.iohao.game.widget.light.domain.event.disruptor.DomainEventSource;

/**
 * 领域事件的业务接口 (Event Object)
 * <pre>
 *     通常是业务数据载体实现的接口
 *     实现该接口后，会得到领域事件发送的能力
 * </pre>
 *
 * @author 渔民小镇
 * @date 2021-12-26
 */
public interface Eo extends DomainEventSource {
    /**
     * 领域事件发送
     */
    default void send() {
        DomainEventPublish.send(this);
    }
}
