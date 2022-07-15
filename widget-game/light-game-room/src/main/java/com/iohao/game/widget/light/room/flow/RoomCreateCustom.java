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
