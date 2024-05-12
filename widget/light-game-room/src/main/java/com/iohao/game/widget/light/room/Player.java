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

import java.io.Serializable;

/**
 * 玩家
 *
 * @author 渔民小镇
 * @date 2022-03-31
 * @since 21.8
 */
public interface Player extends Serializable {
    /**
     * @return userId 玩家 id
     */
    long getUserId();

    /**
     * @param userId userId 玩家 id
     */
    void setUserId(long userId);

    /**
     * @return 房间 id
     */
    long getRoomId();

    /**
     * @param roomId 房间 id
     */
    void setRoomId(long roomId);

    /**
     * @return 用户所在位置
     */
    int getSeat();

    /**
     * @param seat 用户所在位置
     */
    void setSeat(int seat);

    /**
     * @return true - 已准备
     */
    boolean isReady();

    /**
     * @param ready true - 已准备
     */
    void setReady(boolean ready);
}
