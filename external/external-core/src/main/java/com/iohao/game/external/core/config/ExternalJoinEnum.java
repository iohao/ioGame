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
package com.iohao.game.external.core.config;

import lombok.Getter;

/**
 * 连接方式
 *
 * @author 渔民小镇
 * @date 2023-02-18
 */
@Getter
public enum ExternalJoinEnum {
    /**
     * ext socket
     * <pre>
     *     特殊的预留扩展
     * </pre>
     */
    EXT_SOCKET("ext socket", 0),
    /** TCP socket */
    TCP("TCP", 1),
    /** WebSocket */
    WEBSOCKET("WebSocket", 2),
    /** UDP socket */
    UDP("UDP", 3);

    final String name;
    final int index;

    ExternalJoinEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }
}
