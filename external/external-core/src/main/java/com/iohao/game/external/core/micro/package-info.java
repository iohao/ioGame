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
 * 游戏对外服 - core - micro - <a href="https://www.yuque.com/iohao/game/wotnhl">游戏对外服设计</a>，负责与外部通信，与真实用户（玩家）建立连接
 *
 *
 * <pre>
 *     MicroBootstrap 接口
 *     描述：与真实玩家连接的服务器，服务器的创建由 MicroBootstrap 完成，实际上 ExternalCore 是一个类似代理类的角色。MicroBootstrap 帮助开发者屏蔽连接方式的细节，如 TCP、WebSocket、UDP 等。
 *     职责：与真实玩家连接的【真实】服务器
 *
 *     MicroBootstrapFlow 接口
 *     描述：与真实玩家连接【真实】服务器的启动流程，专为 MicroBootstrap 服务。开发者可通过此接口对服务器做编排，编排分为：构建时、新建连接时两种。框架提供了 TCP、WebSocket、UDP 的实现；开发者可以选择性的重写流程方法，来定制符合自身项目的业务。
 *     职责：业务编排，也是开发者在扩展时接触最多的一个接口。
 *
 * </pre>
 *
 * @author 渔民小镇
 * @date 2024-09-13
 */
package com.iohao.game.external.core.micro;