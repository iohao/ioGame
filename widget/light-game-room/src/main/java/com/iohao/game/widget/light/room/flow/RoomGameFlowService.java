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
package com.iohao.game.widget.light.room.flow;

/**
 * 游戏流程
 * <pre>
 *     房间创建
 *     创建玩家
 *     进入房间
 *     游戏开始
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-03-31
 * @since 21.8
 */
public interface RoomGameFlowService extends
        // 房间创建
        RoomCreateCustom,
        // 创建玩家
        RoomPlayerCreateCustom,
        // 进入房间
        RoomEnterCustom,
        // 游戏开始
        RoomGameStartCustom {
}
