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
 * 游戏对外服 - core - <a href="https://iohao.github.io/game/docs/manual_high/external_message">对外服的协议说明</a>、游戏对外服协议编解码、自定义统一的交互协议
 * <p>
 * ExternalMessage
 * <pre>
 *     ExternalMessage 是统一交互协议，也称为游戏对外服协议；其主要作用是与游戏客户端交互的统一协议。
 *
 *     ExternalMessage 包含了
 *     1. 请求命令类型: 0 心跳，1 业务
 *     2. 业务路由（高16为主, 低16为子）
 *     3. 响应码: 0:成功, 其他为有错误
 *     4. 验证信息（错误消息、异常消息），通常情况下 responseStatus == -1001 时， 会有值
 *     5. 业务请求数据
 *     6. 消息标记号；由前端请求时设置，服务器响应时会携带上；（类似透传参数）
 * </pre>
 * <p>
 * 自定义统一的交互协议
 * <pre>
 *     ExternalMessage 是玩家与游戏服务器交互的对外统一协议。玩家（游戏客户端）在发起请求时，默认情况下是通过 ExternalMessage 来进行交互的。
 *
 *     如果没有特殊情况，建议使用框架默认提供的 ExternalMessage；因为没有使用到的字段 Protocol Buffer 会压缩数据，用多少占多少。
 *
 *     ExternalMessage 是框架提供的一种与外部交互的统一协议，也是默认推荐的方式。
 *     注意，这里说的是默认推荐方式，并不是唯一方式，开发者可以自定义这部分内容的；
 *     也就是说，可以不使用 ExternalMessage 与外部联系，而是使用一种自定义的统一协议。
 *
 *     比如你正打算开发一个物联网相关的、或者说其他的项目；想简化对外的统一协议，协议内容只需要路由与业务对象就足够了。
 *     此时，我们可以通过重写 {@link com.iohao.game.external.core.message.ExternalCodec} 接口来实现自定义协议。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2024-09-13
 */
package com.iohao.game.external.core.message;