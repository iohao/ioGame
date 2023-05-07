/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General  License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General  License for more details.
 *
 * You should have received a copy of the GNU General  License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
/**
 * 创建房间时, 子游戏可以自定义流程创建逻辑 包括:
 * <pre>
 *     创建房间
 *     创建玩家
 *     进入房间
 *     规则解析
 *     游戏开始
 *
 *     只提供抽象骨架, 具体的逻辑实现由子游戏自定义
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-03-31
 */
package com.iohao.game.widget.light.room.flow;