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
 * 游戏开始
 *
 * @author 渔民小镇
 * @date 2022-03-31
 * @since 21.8
 */
public interface RoomGameStartCustom {
    /**
     * 游戏开始前的逻辑校验
     *
     * <pre>
     *     比如做一个游戏，房间空间大小为 10 人。
     *     表示房间最大可容纳 10 人，而开始游戏并不一定需要满足 10 人。
     *     现在，假设规则定义为满足 4 人准备，就可以开始游戏，那么这个开始前就可以派上用场了。
     *
     *     方法主要作用是交给子类游戏来定义开始游戏的规则，及一些其他规则验证。
     * </pre>
     *
     * @param gameFlowContext 开始游戏上下文
     */
    void startGameVerify(GameFlowContext gameFlowContext);

    /**
     * 游戏开始
     * <pre>
     *     比如，斗地主、桌游、麻将 等可以发牌；
     *     回合制游戏进入战斗；
     * </pre>
     *
     * @param gameFlowContext 开始游戏上下文
     */
    void startGame(GameFlowContext gameFlowContext);

    default void executeStartGame(GameFlowContext gameFlowContext) {
        this.startGameVerify(gameFlowContext);
        this.startGame(gameFlowContext);
    }
}
