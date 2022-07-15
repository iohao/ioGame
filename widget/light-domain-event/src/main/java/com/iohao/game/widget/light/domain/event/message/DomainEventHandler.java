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

/**
 * 领域事件消费接口, 接收一个领域事件
 *
 * @param <T> T 领域实体
 * @author 渔民小镇
 * @date 2021-12-26
 */
@FunctionalInterface
public interface DomainEventHandler<T> {

    /**
     * 事件处理
     *
     * @param event      领域实体
     * @param endOfBatch endOfBatch
     */
    void onEvent(final T event, final boolean endOfBatch);

    /**
     * 事件处理
     *
     * @param event      领域实体
     * @param sequence   sequence
     * @param endOfBatch endOfBatch
     */
    default void onEvent(final T event, final long sequence, final boolean endOfBatch) {
        this.onEvent(event, endOfBatch);
    }

    /**
     * 获取领域事件名
     *
     * @return 领域事件名
     */
    default String getName() {
        return this.getClass().getSimpleName();
    }
}
