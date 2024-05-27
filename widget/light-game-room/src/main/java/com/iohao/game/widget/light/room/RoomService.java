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

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 房间管理相关的扩展接口
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
 * 内置的默认实现
 * <pre>{@code
 *     RoomService roomService = RoomService.of();
 * }
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

    /**
     * 通过 userId 查找房间
     *
     * @param userId userId
     * @param <T>    Room
     * @return 房间
     */
    default <T extends Room> T getRoomByUserId(long userId) {
        // 通过 userId 得到 roomId
        Long roomId = this.getUserRoomMap().get(userId);

        if (Objects.isNull(roomId)) {
            return null;
        }

        // 通过 roomId 得到 room
        return getRoom(roomId);
    }

    /**
     * 通过 roomId 查找房间
     *
     * @param roomId roomId
     * @param <T>    Room
     * @return 房间
     */
    default <T extends Room> T getRoom(long roomId) {
        return (T) this.getRoomMap().get(roomId);
    }

    /**
     * 通过 roomId 查找房间 Optional
     *
     * @param roomId roomId
     * @param <T>    Room
     * @return Optional Room
     */
    default <T extends Room> Optional<T> optionalRoom(long roomId) {
        return Optional.ofNullable(this.getRoom(roomId));
    }

    /**
     * 通过 userId 查找房间 Optional
     *
     * @param userId userId
     * @param <T>    Room
     * @return Optional Room
     */
    default <T extends Room> Optional<T> optionalRoomByUserId(long userId) {
        return Optional.ofNullable(this.getRoomByUserId(userId));
    }

    /**
     * 添加房间
     *
     * @param room 房间
     */
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

    /**
     * 添加玩家到房间里，并让 userId 与 roomId 关联
     *
     * @param room   间里
     * @param player 玩家
     */
    default void addPlayer(Room room, Player player) {
        room.addPlayer(player);
        this.getUserRoomMap().put(player.getUserId(), room.getRoomId());
    }

    /**
     * 将玩家从房间内内移除 并删除 userId 与 roomId 的关联
     *
     * @param room   房间
     * @param player 玩家
     */
    default void removePlayer(Room room, Player player) {
        room.removePlayer(player);
        this.getUserRoomMap().remove(player.getUserId());
    }

    /**
     * 将玩家从房间内内移除 并删除 userId 与 roomId 的关联
     *
     * @param room   房间
     * @param userId userId
     */
    default void removePlayer(Room room, long userId) {
        room.ifPlayerExist(userId, player -> this.removePlayer(room, player));
    }

    /**
     * 得到房间列表
     *
     * @param <T> Room
     * @return 房间
     */
    default <T extends Room> Collection<T> listRoom() {
        return (Collection<T>) this.getRoomMap().values();
    }

    /**
     * 创建一个 RoomService 对象实例（框架内置的默认实现）
     *
     * @return RoomService
     */
    static RoomService of() {
        return new SimpleRoomService();
    }
}
