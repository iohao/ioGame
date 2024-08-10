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
 * 游戏对外服 - <a href="https://www.yuque.com/iohao/game/ea6geg">游戏对外服使用</a>
 * <p>
 * 游戏对外服的职责
 * <pre>
 *     1. 保持与用户（玩家）长的连接
 *     2. 帮助开发者屏蔽通信细节、与连接方式的细节
 *     3. 连接方式支持：WebSocket、TCP、UDP
 *     4. 将用户（玩家）请求转发到游戏网关
 *     5. 可动态增减扩展机器
 *     6. 功能扩展，如：路由存在检测、路由权限、UserSession 管理、心跳，及后续要提供但还未提供的熔断、限流、降载、用户流量统计等功能。
 * </pre>
 * <p>
 * 扩展场景
 * <pre>
 *     游戏对外服主要负责与用户（玩家）的连接。假设一台硬件支持最多建立 5000 个用户连接，当用户量达到 7000 人时，我们可以增加一个游戏对外服来进行流量控制和减压。
 *
 *     由于游戏对外服的扩展性和灵活性，可以支持同时在线玩家从几千人到数千万人不等。
 *     这是因为，通过增加游戏对外服的数量，可以有效地进行连接的负载均衡和流量控制，使得系统能够更好地承受高并发的压力。
 * </pre>
 * <p>
 * 连接方式的切换、支持、扩展
 * <pre>
 *     ioGame 已提供了 TCP、WebSocket、UDP 连接方式的支持，并提供了灵活的方式来实现连接方式的切换。
 *     可以将 TCP、WebSocket、UDP 连接方式与业务代码进行无缝衔接。开发者可以用一套业务代码，无需任何改动，同时支持多种通信协议。
 *
 *     如果想要切换到不同的连接方式，只需要更改相应的枚举即可，非常简单。
 *     在不使用 ioGame 时，将连接方式从 TCP 改为 WebSocket 或 UDP 等，需要进行大量的调整和改动，但在 ioGame 中，实现这些转换是简单的。
 *     此外，除了能轻松切换各种连接方式外，还能同时支持多种连接方式，并使它们在同一应用程序中共存。
 *
 *     连接方式是可扩展的，而且扩展也简单，这意味着之后如果支持了 KCP，那么将已有项目的连接方式，如 TCP、WebSocket、UDP 切换成 KCP 也是简单的。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-04-28
 */
package com.iohao.game.external.core;