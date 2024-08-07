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
 * 业务框架 - 内部协议 - <a href="https://www.yuque.com/iohao/game/rf9rb9">请求同类型多个逻辑服通信结果</a>
 * <p>
 * 场景介绍
 * <pre>
 *     模块之间的访问，访问【同类型】的多个逻辑服，如： 模块A 访问 模块B 的某个方法，因为只有模块B持有这些数据，这里的模块指的是逻辑服。
 *     假设启动了多个模块B，分别是：模块B-1、模块B-2、模块B-3、模块B-4 等。
 *     框架支持访问【同类型】的多个逻辑服，并把多个相同逻辑服结果收集到一起。
 * </pre>
 * <p>
 * 场景举例
 * <pre>
 *     【象棋逻辑服】有 3 台，分别是：《象棋逻辑服-1》、《象棋逻辑服-2》、《象棋逻辑服-3》，这些逻辑服可以在不同的进程中。
 *     我们可以在大厅逻辑服中向【同类型】的多个游戏逻辑服请求，意思是大厅发起一个向这 3 台象棋逻辑服的请求，框架会收集这 3 个结果集（假设结果是：当前服务器房间数）。
 *     当大厅得到这个结果集，可以统计房间的总数，又或者说根据这些信息做一些其他的业务逻辑；这里只是举个例子，实际当中可以发挥大伙的想象力。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2024-08-07
 */
package com.iohao.game.action.skeleton.protocol.collect;