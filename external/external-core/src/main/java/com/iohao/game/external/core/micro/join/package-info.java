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
 * 游戏对外服 - core micro <a href="https://www.yuque.com/iohao/game/wotnhl">设计</a>，连接方式 TCP、UDP、WS 的支持
 *
 * <pre>
 *     目前，ioGame 框架已经提供了 TCP、WebSocket、UDP 的实现，其支持了连接方式的灵活切换。
 *     例如，可以将 TCP、WebSocket、UDP 连接方式与业务代码进行无缝衔接，让开发者用一套业务代码，无需任何改动，同时支持多种通信协议。
 *
 *     虽然 KCP 的支持尚未实现，但是它已经被列入计划中。同样是可以通过简单的更改实现将 TCP、WebSocket、UDP 的连接方式切换成 KCP 连接方式，而这对于已有的项目而言也非常方便。
 *
 *     扩展连接方式：MicroBootstrap 在扩展上也是简单的，游戏对外服在扩展无连接的 UDP 时，只用了 400+ 行 java 代码就完成了与 TCP、WebSocket 相兼容的扩展（包括路由权限、心跳、UserSession 管理...等）。这意味着，将来我们实现 KCP 的扩展时，也是简单的。
 *
 *     连接方式的切换对业务代码没有任何影响，无需做出任何改动即可实现连接方式的更改。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2024-09-13
 */
package com.iohao.game.external.core.micro.join;