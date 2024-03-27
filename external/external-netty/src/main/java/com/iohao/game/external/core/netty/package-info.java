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
 * 基于 Netty 的实现 <a href="https://www.yuque.com/iohao/game/ea6geg">新游戏对外服-文档</a>
 * <p>
 * 连接方式的支持、切换
 * <pre>
 *     ioGame 已提供了 TCP、WebSocket、UDP 连接方式的支持，并提供了灵活的方式来实现连接方式的切换。
 *     可以将 TCP、WebSocket、UDP 连接方式与业务代码进行无缝衔接。开发者可以用一套业务代码，无需任何改动，同时支持多种通信协议。
 *
 *     如果想要切换到不同的连接方式，只需要更改相应的枚举即可，非常简单。
 *     在不使用 ioGame 时，将连接方式从 TCP 改为 WebSocket 或 UDP 等，需要进行大量的调整和改动。
 *     然而，在 ioGame 中，实现这些转换是非常简单的。此外，不仅可以轻松切换各种连接方式，而且可以同时支持多种连接方式，并使它们在同一应用程序中共存。
 *
 *     连接方式是可扩展的，而且扩展也简单，这意味着之后如果支持了 KCP，那么将已有项目的连接方式，如 TCP、WebSocket、UDP 切换成 KCP 也是简单的。
 *
 *     需要再次强调的是，连接方式的切换对业务代码没有任何影响，无需做出任何改动即可实现连接方式的更改。
 * </pre>
 * <p>
 * 游戏对外服的核心接口
 * <pre>
 *     ExternalServer：游戏对外服，由 ExternalCore 和 ExternalBrokerClientStartup 组成的一个整体。
 *     ExternalCore： 帮助开发者屏蔽各通信框架的细节，如 Netty、mina、smart-socket 等通信框，ioGame 默认提供了基于 Netty 的实现。
 *     MicroBootstrap：真实玩家连接的服务器，服务器的创建由 MicroBootstrap 完成，MicroBootstrap 帮助开发者屏蔽连接方式的细节，如 TCP、WebSocket、UDP、KCP 等。目前已经支持 TCP、WebSocket、UDP 的连接方式，而 KCP 的连接方式也在计划内。
 *     MicroBootstrapFlow：MicroBootstrapFlow	与真实玩家连接【真实】服务器的启动流程，专为 MicroBootstrap 服务。开发者可通过此接口对服务器做编排，编排分为：构建时、新建连接时两种。
 *
 *     MicroBootstrapFlow 接口的目的是尽可能地细化服务器创建和连接时的每个环节，以方便开发者对游戏对外服进行定制化扩展。通常情况下，开发者只需要关注重写 MicroBootstrapFlow.pipelineCustom 方法，就可以实现很强的扩展了。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-06-04
 */
package com.iohao.game.external.core.netty;