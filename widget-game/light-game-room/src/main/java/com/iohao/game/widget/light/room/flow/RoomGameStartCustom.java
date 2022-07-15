/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License..
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
