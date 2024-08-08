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
/**
 * 扩展模块 - domain-event 领域事件 - 自定义领域消息实体与、自定义领域事件消费者的相关消息
 * <pre>
 *     开发者只需要关注两个接口
 *     1. {@link com.iohao.game.widget.light.domain.event.message.Eo} 自定义领域消息实体实现该接口后，会得到领域事件发送的能力
 *     2. {@link com.iohao.game.widget.light.domain.event.message.DomainEventHandler} 定义领域事件消费者，接收一个领域事件
 * </pre>
 *
 * @author 渔民小镇
 * @date 2021-12-26
 */
package com.iohao.game.widget.light.domain.event.message;