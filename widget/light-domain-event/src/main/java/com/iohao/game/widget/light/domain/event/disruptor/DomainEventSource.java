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

import com.iohao.game.widget.light.domain.event.message.Topic;

/**
 * 领域事件接口 - 源事件源
 *
 * @author 渔民小镇
 * @date 2021-12-26
 */
public interface DomainEventSource extends Topic {
    /**
     * 获取领域事件主题
     *
     * @return 领域事件主题
     */
    @Override
    default Class<?> getTopic() {
        return this.getClass();
    }

    /**
     * 获取事件源
     *
     * @param <T> source
     * @return 事件源
     */
    @SuppressWarnings("unchecked")
    default <T> T getSource() {
        return (T) this;
    }
}
