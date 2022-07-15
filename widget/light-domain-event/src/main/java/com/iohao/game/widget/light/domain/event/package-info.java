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