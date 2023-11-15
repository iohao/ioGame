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
package com.iohao.game.common.kit.micro.room;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 微房间管理者
 *
 * @author 渔民小镇
 * @date 2023-07-12
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MicroRooms<Room extends MicroRoom> {
    @Getter
    final Map<Long, Room> roomMap = new NonBlockingHashMap<>();

    @Setter
    Supplier<Room> roomSupplier;

    public boolean contains(long id) {
        return roomMap.containsKey(id);
    }

    public Room remove(long id) {
        return this.roomMap.remove(id);
    }

    public Room getRoom(long id) {
        return roomMap.get(id);
    }

    public Room add(Room room) {
        long id = room.getId();
        var anyRegion = roomMap.putIfAbsent(id, room);

        if (Objects.isNull(anyRegion)) {
            anyRegion = roomMap.get(id);
        }

        return anyRegion;
    }

    public Optional<Room> optionalRoom(long id) {
        return Optional.ofNullable(roomMap.get(id));
    }

    /**
     * 得到对应的房间对象
     * <pre>
     *     如果房间不存在就创建
     * </pre>
     *
     * @param id roomId
     * @return 房间对象
     */
    public Room ofRoom(long id) {

        Room region = roomMap.get(id);

        if (Objects.isNull(region)) {
            region = roomSupplier.get();
            region.setId(id);

            region = add(region);
        }

        return region;
    }

    public Stream<Room> stream() {
        return this.roomMap.values().stream();
    }
}