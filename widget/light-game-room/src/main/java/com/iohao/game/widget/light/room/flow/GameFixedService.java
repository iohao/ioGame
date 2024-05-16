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
import com.iohao.game.widget.light.room.Room;

/**
 * 游戏流程 - 相对固定的流程。如，创建房间、创建玩家、进入房间；解散房间、退出房间、玩家准备。
 * <pre>
 *     创建房间
 *     创建玩家
 *     进入房间
 *
 *     解散房间
 *     退出房间
 *     玩家准备
 * </pre>
 *
 * @author 渔民小镇
 * @date 2024-05-15
 * @since 21.8
 */
public interface GameFixedService {
    /**
     * 创建房间, 子类只需要关心房间配置和规则信息
     * <pre>
     *     延迟到子游戏中实现, 以便适应不同的子游戏规则
     * </pre>
     *
     * @param createContext 创建房间信息（及玩法规则）
     * @return 房间
     */
    Room createRoom(RoomCreateContext createContext);

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

    /**
     * 进入房间 (重连)
     *
     * @param gameFlowContext 进入房间上下文
     */
    void enterRoom(GameFlowContext gameFlowContext);

    /**
     * 解散房间
     *
     * @param gameFlowContext gameFlowContext
     */
    default void dissolveRoom(GameFlowContext gameFlowContext) {
    }

    /**
     * 玩家退出房间
     *
     * @param gameFlowContext gameFlowContext
     */
    default void quitRoom(GameFlowContext gameFlowContext) {
    }

    /**
     * 玩家准备
     *
     * @param ready           true 表示准备，false 则是取消准备
     * @param gameFlowContext gameFlowContext
     */
    default void ready(boolean ready, GameFlowContext gameFlowContext) {
    }
}
