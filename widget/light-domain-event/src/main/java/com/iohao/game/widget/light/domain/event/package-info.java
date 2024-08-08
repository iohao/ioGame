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
 * 扩展模块 - <a href="https://www.yuque.com/iohao/game/gmfy1k">domain-event 领域事件</a> - 可为你的系统实现类似 Guava-EventBus、Spring 事件驱动模型 ApplicationEvent、业务解耦、规避并发、不阻塞主线程...等，各种浪操作。
 * <p>
 * 介绍
 * <pre>
 *     Disruptor 是一个开源的并发框架。由英国外汇交易公司LMAX开发的一个高性能队列，并且大大的简化了并发程序开发的难度，获得2011Duke’s程序框架创新奖。可以将 disruptor 理解为单机版本的 MQ（轻量级单机最快MQ -- disruptor）。
 * </pre>
 * Event Source 领域事件优点
 * <pre>
 * 1. 领域驱动设计，基于LMAX架构。
 * 2. 单一职责原则，可以给系统的可扩展、高伸缩、低耦合达到极致。
 * 3. 异步高并发、线程安全的，使用 disruptor 环形数组来消费业务。
 * 4. 使用事件消费的方式编写代码，即使业务在复杂也不会使得代码混乱，维护代码成本更低。
 * 5. 可灵活的定制业务线程模型
 * 6. 插件形式提供事件领域，做到了可插拔，就像玩乐高积木般有趣。
 * </pre>
 * for example
 * <pre>{@code
 * // 自定义领域消息实体
 * public record StudentEo(int id) implements Eo {
 * }
 *
 * // 定义领域事件 - 用于处理 StudentEo 领域消息实体，一个事件消费类只处理一件事件（单一职责原则）
 * public final class StudentEmailEventHandler1 implements DomainEventHandler<StudentEo> {
 *     @Override
 *     public void onEvent(StudentEo studentEo, boolean endOfBatch) {
 *         log.debug("给这个学生发送一个email消息: {}", studentEo);
 *     }
 * }
 *
 * // 测试用例
 * public class StudentDomainEventTest {
 *     ... ...省略部分代码
 *
 *     @Before
 *     public void setUp() {
 *         // ======项目启动时配置一次（初始化）======
 *
 *         // 领域事件上下文参数
 *         DomainEventContextParam contextParam = new DomainEventContextParam();
 *         // 配置一个学生的领域事件消费 - 给学生发生一封邮件
 *         contextParam.addEventHandler(new StudentEmailEventHandler1());
 *         // 配置一个学生的领域事件消费 - 回家
 *         contextParam.addEventHandler(new StudentGoHomeEventHandler2());
 *         // 配置一个学生的领域事件消费 - 让学生睡觉
 *         contextParam.addEventHandler(new StudentSleepEventHandler3());
 *
 *         // 启动事件驱动
 *         domainEventContext = new DomainEventContext(contextParam);
 *         domainEventContext.startup();
 *     }
 *
 *     @Test
 *     public void testEventSend() {
 *         // 这里开始就是你的业务代码
 *         StudentEo studentEo = new StudentEo(1);
 *         // 发送事件、上面只配置了一个事件。
 *         // 如果将来还需要给学生发送一封email,那么直接配置。（可扩展）
 *         // 如果将来还需要记录学生今天上了什么课程，那么也是直接配置 （可扩展） 这里的业务代码无需任何改动（松耦合）
 *         // 如果将来又不需要给学生发送email的事件了，直接删除配置即可，这里还是无需改动代码。（高伸缩）
 *         studentEo.send();
 *     }
 * }
 * }</pre>
 *
 * @author 渔民小镇
 * @date 2021-12-26
 */
package com.iohao.game.widget.light.domain.event;