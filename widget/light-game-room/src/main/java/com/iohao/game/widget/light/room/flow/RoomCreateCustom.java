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
import com.iohao.game.widget.light.room.CreateRoomInfo;

/**
 * 房间创建 - 自定义
 * <pre>
 *     延迟到子游戏中实现, 以便适应不同的子游戏规则
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-03-31
 */
public interface RoomCreateCustom {
    /**
     * 创建房间, 子类只需要关心 ruleInfo (房间配置, 规则信息)
     * <p>
     * 根据 创建游戏规则
     *
     * @param createRoomInfo 创建房间信息
     * @param <T>            AbstractRoom
     * @return 房间
     */
    <T extends AbstractRoom> T createRoom(CreateRoomInfo createRoomInfo);
}
