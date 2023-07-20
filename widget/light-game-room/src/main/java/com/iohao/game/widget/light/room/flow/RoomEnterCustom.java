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
package com.iohao.game.widget.light.room.flow;

import com.iohao.game.widget.light.room.AbstractRoom;

/**
 * 进入房间 (重连)
 *
 * @author 渔民小镇
 * @date 2022-03-31
 */
public interface RoomEnterCustom {

    /**
     * 进入房间
     *
     * @param userId        玩家 id
     * @param abstractRoom  玩家所在房间
     * @param roomEnterInfo 进入房间请求信息
     * @return enter Response
     */
    RoomEnterInfo enterRoom(long userId, AbstractRoom abstractRoom, RoomEnterInfo roomEnterInfo);

}
