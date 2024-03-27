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

    /**
     * 根据所使用的连接方式，协定变化的端口
     * <a href="https://github.com/iohao/ioGame/issues/159">同时支持多种通信方式</a>
     * <pre>
     *     ws  port = port;
     *     tcp port = port + 1;
     *     udp port = port + 2;
     *
     *     这个用法一般出现在，同一进程内启动了多个不同连接方式的游戏对外服（websocket、tcp、udp），
     *     如果没有这方面需求的，不需要使用该方法。
     * </pre>
     *
     * @param port 端口，玩家连接的端口
     * @return 协定变化的端口
     */
    public int cocPort(int port) {
        return switch (this) {
            case TCP -> port + 1;
            case UDP -> port + 2;
            default -> port;
        };
    }
}
