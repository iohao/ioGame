/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
/**
 * 请求对外服，这里对游戏对外服做处理时，统一使用请求多个对外服，不提供只请求其中一个的情况。
 * <pre>
 *     实际上这里是可以复用 RequestCollectMessage 系列的，
 *     因为对外服也可以使用业务框架，但不想搞得太混合。
 *     就分开做额外处理了。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-07-27
 */
package com.iohao.game.action.skeleton.protocol.external;