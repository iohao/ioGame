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
 * 桌游类、房间类游戏的扩展模块 - 规避并发的领域事件。
 * see <a href="https://www.yuque.com/iohao/game/gmfy1k">文档 - domain-event 领域事件</a>
 * 在使用 room 模块的领域事件时，还需要做以下配置。将领域事件集成到 room 模块中。
 * <pre>{@code
 * public class MyRoomDomainRunner implements Runner {
 *     @Override
 *     public void onStart(BarSkeleton skeleton) {
 *         // 领域事件上下文参数
 *         DomainEventContextParam contextParam = new DomainEventContextParam();
 *         // 配置领域事件 - 玩法操作相关
 *         contextParam.addEventHandler(new OperationContextEventHandler());
 *         // 游戏流程相关
 *         contextParam.addEventHandler(new GameFlowEventHandler());
 *
 *         // 启动事件驱动
 *         DomainEventContext domainEventContext = new DomainEventContext(contextParam);
 *         domainEventContext.startup();
 *     }
 * }
 *
 * // 业务框架构建器
 * BarSkeletonBuilder builder = ...;
 * // 启动项. see <a href="https://www.yuque.com/iohao/game/dpwe6r6sqwwtrh1q">文档 - Runner 扩展机制</a>
 * builder.addRunner(new MyRoomDomainRunner());
 * }</pre>
 *
 * @author 渔民小镇
 * @date 2024-05-15
 * @since 21.8
 */
package com.iohao.game.widget.light.room.domain;