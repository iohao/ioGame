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
 * 扩展模块 - <a href="https://www.yuque.com/iohao/game/niflk0">timer-task 任务延时器</a>
 * <p>
 * 介绍
 * <pre>
 * 可对任务进行延时执行、暂停、取消等操作，并不是类似 Quartz 的任务调度，功能特点：
 * 1. 单一职责原则
 * 2. 将来某个时间执行开发者定义的任务（一个任务延时器， 达到指定时间则执行业务 - 新增）
 * 3. 暂停任务延时器 (暂停时，将不在计时 - 变更)
 * 4. 该任务延时器也支持被移除（在满足某个条件后 - 移除）
 *
 * 任务延时器内部采用内存级的缓存库的方式实现，巧妙的运用了内存级缓存库的过期机制，来现实任务的延时机制。
 * </pre>
 * 应用场景举例
 * <pre>
 * 在游戏开始前，房间创建后需要在1分钟后自动解散房间（将来某个时间执行该任务）。
 * 在房间解散前，每当有一个玩家加入房间，则解散房间延迟（暂停任务）30秒。
 * 当房间人数大于5人时，则取消房间自动解散规则（显示的取消任务）
 * </pre>
 *
 * @author 渔民小镇
 * @date 2021-12-26
 */
package com.iohao.game.widget.light.timer.task;