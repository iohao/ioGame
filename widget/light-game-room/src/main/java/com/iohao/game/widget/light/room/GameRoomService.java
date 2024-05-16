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

import com.iohao.game.widget.light.room.flow.GameFlowService;
import com.iohao.game.widget.light.room.operation.OperationService;

/**
 * 游戏房间相关的聚合扩展接口。房间相关的、游戏流程相关的、玩法操作相关的。
 * <pre>
 *     包括内容如下：
 *     1. 房间相关的
 *     2. 开始游戏流程相关的
 *     3. 玩法操作相关的
 * </pre>
 *
 * @author 渔民小镇
 * @date 2024-05-12
 * @see RoomService 房间相关
 * @see GameFlowService 开始游戏流程相关
 * @see OperationService 玩法操作
 * @since 21.8
 */
public interface GameRoomService
        extends RoomService, GameFlowService, OperationService {
}
