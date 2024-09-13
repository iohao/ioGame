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
 * 游戏对外服 - core - <a href="https://www.yuque.com/iohao/game/wg6lk7">UserSessions 和 UserSession</a>，UserSessions 是管理所有玩家连接的管理器，UserSession 玩家连接对象，与连接是 1:1 的关系，可取到对应的 userId、channel 等信息。
 *
 * @author 渔民小镇
 * @date 2024-09-13
 */
package com.iohao.game.external.core.session;