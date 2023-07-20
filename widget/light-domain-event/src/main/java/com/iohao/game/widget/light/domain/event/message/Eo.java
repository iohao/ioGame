/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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
