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
 * {@link com.iohao.game.action.skeleton.core.runner.Runner} 机制类似于 Spring CommandLineRunner 的启动项，它能够在逻辑服务器启动之后调用一次 Runner 接口实现类，让开发者能够通过实现 Runner 接口来扩展自身的系统。
 * <p>
 * Runner 机制，会在逻辑服与 Broker（游戏网关）建立连接之前、之后分别触发一次对应的方法。<a href="https://www.yuque.com/iohao/game/dpwe6r6sqwwtrh1q">相关文档</a>
 *
 * @author 渔民小镇
 * @date 2024-06-05
 */
package com.iohao.game.action.skeleton.core.runner;