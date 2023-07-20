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
 * 游戏开始
 * 玩家全都准备后会触发
 *
 * @author 渔民小镇
 * @date 2022-03-31
 */
public interface RoomGameStartCustom {
    /**
     * 游戏开始前的逻辑校验
     *
     * <pre>
     *     方法解说:
     *     比如做一个游戏, 8人场, 由于人数需要很多.
     *     假设规则定义为满足4人准备, 就可以开始游戏.
     *     那么这个开始前就可以派上用场了, 毕竟你永远不知道子游戏的规则是什么.
     *     所以最好预留一个这样的验证接口, 交给子类游戏来定义开始游戏的规则
     * </pre>
     *
     * @param abstractRoom 房间
     * @return 返回 true, 会执行 {@link RoomGameStartCustom#startAfter}. 并更新用户的状态为战斗状态
     */
    boolean startBefore(AbstractRoom abstractRoom);

    /**
     * 游戏开始前的 after 逻辑. 这里可以游戏正式开始的逻辑
     * <pre>
     *     比如
     *      斗地主、桌游、麻将 等可以发牌
     *      回合制 进入战斗
     * </pre>
     *
     * @param abstractRoom 房间
     */
    void startAfter(AbstractRoom abstractRoom);
}
