/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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
package com.iohao.game.bolt.broker.client.external.bootstrap;

import lombok.Getter;
import lombok.ToString;

/**
 * 对外服的连接方式
 *
 * @author 渔民小镇
 * @date 2022-01-13
 */
@ToString
public enum ExternalJoinEnum {
    /** tcp socket */
    TCP("tcp socket"),
    /** websocket */
    WEBSOCKET("websocket"),
    /** udp socket 注意这个目前还没现实 */
    UDP("udp socket"),
    /**
     * ext socket
     * <pre>
     *     特殊的预留扩展
     * </pre>
     */
    EXT_SOCKET("ext socket");

    @Getter
    final String name;

    ExternalJoinEnum(String name) {
        this.name = name;
    }
}
