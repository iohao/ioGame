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
 * 业务框架 - 内部协议 - <a href="https://www.yuque.com/iohao/game/idl1wm">玩家动态绑定游戏逻辑服</a>
 * <p>
 * 简介
 * <pre>
 *     动态绑定游戏逻辑服，指的是玩家与游戏逻辑服绑定后，之后的请求都由该游戏逻辑服来处理。
 *     玩家动态绑定逻辑服节点后，之后的请求都由这个绑定的游戏逻辑服来处理，可以实现类似 LOL、王者荣耀匹配后动态分配房间的效果。
 *     支持玩家与多个游戏逻辑服的动态绑定。
 * </pre>
 * <p>
 * 使用场景
 * <pre>
 *     跨服活动、跨服战斗等。
 *     动态绑定游戏逻辑服可以解决玩家增量的问题，我们都知道一台机器所能承载的运算是有上限的；当上限达到时，就需要增加新机器来分摊请求量；如果你开发的游戏是有状态的，那么你如何解决请求分配的问题呢？在比如让你做一个类似 LOL、王者荣耀的匹配，将匹配好的玩家分配到一个房间中，之后这些玩家的请求都能在同一个游戏逻辑服上处理，这种业务你该如何实现呢？
 *     使用框架提供的动态绑定逻辑服节点可以轻松解决此类问题，而且还可以根据业务规则，计算出当前空闲最多的游戏逻辑服，并将此游戏逻辑服与玩家做绑定，从而做到均衡的利用机器资源，来防止请求倾斜的问题。
 * </pre>
 * <p>
 * for example
 * <pre>{@code
 * // 绑定消息
 * EndPointLogicServerMessage endPointLogicServerMessage = new EndPointLogicServerMessage()
 *         // 需要绑定的玩家，示例中只取了当前请求匹配的玩家
 *         .setUserList(userIdList)
 *         // 添加需要绑定的逻辑服id，下面绑定了两个；
 *         // 1.给绑定一个房间游戏逻辑服的 id
 *         // 2.绑定 animal 游戏逻辑服就简单点，固定写 id 为 2-1 的；
 *         .addLogicServerId(logicServerId)
 *         .addLogicServerId("2-1")
 *         // 覆盖绑定游戏逻辑服
 *         .setOperation(EndPointOperationEnum.COVER_BINDING);
 *
 * // 发送消息到网关
 * ProcessorContext processorContext = BrokerClientHelper.getProcessorContext();
 * processorContext.invokeOneway(endPointLogicServerMessage);
 * }</pre>
 *
 * @author 渔民小镇
 * @date 2024-08-07
 */
package com.iohao.game.action.skeleton.protocol.processor;