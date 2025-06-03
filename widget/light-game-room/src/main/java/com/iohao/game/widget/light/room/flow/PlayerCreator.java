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

import com.iohao.game.widget.light.room.Player;

/**
 * @author 渔民小镇
 * @date 2025-06-03
 * @since 21.28
 */
public interface PlayerCreator {
    /**
     * 创建房间内的玩家
     * <pre>
     *     延迟到子游戏中实现, 以便适应不同的子游戏规则
     * </pre>
     *
     * @param gameFlowContext 游戏流程上下文
     * @return 玩家
     */
    Player createPlayer(GameFlowContext gameFlowContext);
}
