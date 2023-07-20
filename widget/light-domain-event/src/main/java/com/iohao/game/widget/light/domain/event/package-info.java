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
/**
 * 高性能的异步无阻塞领域驱动，可使代码更好的维护、可扩展。 <BR>
 * 使用示例 :
 * <pre>
 * public void testEventSend() {
 *     // 配置一个学生的领域事件消费 - 学生回家业务逻辑
 *     DomainEventHandlerConfig.addEventHandler(new StudentGoHomeEventHandler4());
 *     // 启动事件驱动
 *     DomainEventHandlerConfig.start();
 *
 *     // 这里开始就是你的业务代码
 *     Student student = new Student(1);
 *     // 发送事件、上面只配置了一个事件。
 *     // 如果将来还需要给学生发送一封email,那么直接配置。（可扩展）
 *     // 如果将来还需要记录学生今天上了什么课程，那么也是直接配置 （可扩展）
 *     // 这里的业务代码无需任何改动（松耦合）
 *     // 如果将来又不需要给学生发送email的事件了，直接删除配置即可，这里还是无需改动代码。（高伸缩）
 *     DomainEventPublish.send(student);
 *
 *     // 停止
 *     DomainEventHandlerConfig.stop();
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2021-12-26
 */
package com.iohao.game.widget.light.domain.event;