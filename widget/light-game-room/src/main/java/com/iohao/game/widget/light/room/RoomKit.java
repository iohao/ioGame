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

import lombok.experimental.UtilityClass;

/**
 * 房间相关工具类
 *
 * @author 渔民小镇
 * @date 2024-04-30
 * @since 21.7
 */
@UtilityClass
public class RoomKit {
    /**
     * 从房间内获取一个空位置
     *
     * @param room 房间
     * @return 空的位置。当值为 -1 时，表示没有空的位置（房间满人了）。
     */
    public int getEmptySeatNo(Room room) {
        // 玩家位置 map
        var playerSeatMap = room.getPlayerSeatMap();

        for (int i = 0; i < room.getSpaceSize(); i++) {
            if (!playerSeatMap.containsKey(i)) {
                return i;
            }
        }

        return -1;
    }
}
