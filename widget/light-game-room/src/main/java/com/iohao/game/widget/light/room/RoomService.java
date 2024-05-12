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

import java.util.*;

/**
 * 房间的管理
 * <pre>
 *     房间的添加
 *     房间的删除
 *     房间与玩家之间的关联
 *     房间查找
 *         通过 roomId 查找
 *         通过 userId 查找
 * </pre>
 * 子类扩展实现
 * <pre>
 *     如果你使用了lombok, 推荐这种方式. 只需要在对象中新增此行代码
 *     {@code
 *     // 房间 map
 *     final Map<Long, Room> roomMap = new ConcurrentHashMap<>();
 *     // 玩家对应的房间 map
 *     final Map<Long, Long> userRoomMap = new ConcurrentHashMap<>();
 *     }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-03-31
 * @since 21.8
 */
@SuppressWarnings("unchecked")
public interface RoomService {
    /**
     * 房间 map
     * <pre>
     *     key : roomId
     *     value : room
     * </pre>
     *
     * @return 房间 map
     */
    Map<Long, Room> getRoomMap();

    /**
     * 玩家对应的房间 map
     * <pre>
     *     key : userId
     *     value : roomId
     * </pre>
     *
     * @return 玩家对应的房间 map
     */
    Map<Long, Long> getUserRoomMap();

    default <T extends Room> T getRoomByUserId(long userId) {
        // 通过 userId 得到 roomId
        Long roomId = this.getUserRoomMap().get(userId);

        if (Objects.isNull(roomId)) {
            return null;
        }

        // 通过 roomId 得到 room
        return getRoom(roomId);
    }

    default <T extends Room> T getRoom(long roomId) {
        return (T) this.getRoomMap().get(roomId);
    }

    default Optional<Room> optionalRoomByUserId(long userId) {
        return Optional.ofNullable(this.getRoomByUserId(userId));
    }

    default void addRoom(Room room) {
        long roomId = room.getRoomId();
        this.getRoomMap().put(roomId, room);
    }

    /**
     * 删除房间
     *
     * @param room 房间
     */
    default void removeRoom(Room room) {
        long roomId = room.getRoomId();
        this.getRoomMap().remove(roomId);
        room.listPlayerId().forEach(userId -> this.getUserRoomMap().remove(userId));
    }

    default void addPlayer(Room room, Player player) {
        room.addPlayer(player);
        this.getUserRoomMap().put(player.getUserId(), room.getRoomId());
    }

    /**
     * 移出房间内的玩家 删除用户与房间的对应关系
     *
     * @param room   房间
     * @param player 玩家
     */
    default void removePlayer(Room room, Player player) {
        room.removePlayer(player);
        this.getUserRoomMap().remove(player.getUserId());
    }

    default <T extends Room> Collection<T> listRoom() {
        return (Collection<T>) this.getRoomMap().values();
    }

    static RoomService of() {
        return new SimpleRoomService();
    }
}
