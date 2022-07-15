/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License..
 */
package com.iohao.game.widget.light.room;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 房间的管理
 *
 * @author 渔民小镇
 * @date 2022-03-31
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomService {
    /**
     * 房间 map
     * <pre>
     *     key : roomId
     *     value : room
     * </pre>
     */
    final Map<Long, AbstractRoom> roomMap = new HashMap<>();

    /**
     * 玩家对应的房间 map
     * <pre>
     *     key : userId
     *     value : roomId
     * </pre>
     */
    final Map<Long, Long> userRoomMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T extends AbstractRoom> T getRoomByUserId(long userId) {
        // 通过 userId 得到 roomId
        Long roomId = userRoomMap.get(userId);

        if (Objects.isNull(roomId)) {
            return null;
        }

        // 通过 roomId 得到 room
        return getRoom(roomId);
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractRoom> T getRoom(long roomId) {
        return (T) this.roomMap.get(roomId);
    }

    public void addRoom(AbstractRoom room) {
        long roomId = room.getRoomId();
        this.roomMap.put(roomId, room);
    }

    public void addPlayer(AbstractRoom room, AbstractPlayer player) {
        room.addPlayer(player);
        this.userRoomMap.put(player.getId(), room.getRoomId());
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractRoom> Collection<T> listRoom() {
        return (Collection<T>) this.roomMap.values();
    }

    public static RoomService me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final RoomService ME = new RoomService();
    }

}
