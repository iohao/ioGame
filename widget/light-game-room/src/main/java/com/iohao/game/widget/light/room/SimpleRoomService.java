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
package com.iohao.game.widget.light.room;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Map;

/**
 * 房间管理相关的扩展（内置实现）
 *
 * @author 渔民小镇
 * @date 2024-05-12
 * @since 21.8
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
final class SimpleRoomService implements RoomService {
    /**
     * 房间 map
     * <pre>
     *     key : roomId
     *     value : room
     * </pre>
     */
    final Map<Long, Room> roomMap = new NonBlockingHashMap<>();

    /**
     * 玩家对应的房间 map
     * <pre>
     *     key : userId
     *     value : roomId
     * </pre>
     */
    final Map<Long, Long> userRoomMap = new NonBlockingHashMap<>();

    SimpleRoomService() {
    }
}
