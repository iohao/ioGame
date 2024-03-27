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
