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
package com.iohao.game.widget.light.room.operation;

import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.widget.light.room.Player;
import com.iohao.game.widget.light.room.Room;

/**
 * 上下文 - 玩家玩法操作上下文接口
 *
 * @author 渔民小镇
 * @date 2022-03-31
 * @see OperationHandler
 * @since 21.8
 */
public interface PlayerOperationContext {
    /**
     * get 房间
     *
     * @param <T> Room
     * @return 房间
     */
    <T extends Room> T getRoom();

    /**
     * get 操作数据。具体玩法需要操作的数据，通常由开发者根据游戏业务来定制
     *
     * @param <T> t
     * @return 操作数据
     */
    <T> T getCommand();

    /**
     * 当前玩家的 FlowContext
     *
     * @return FlowContext
     */
    FlowContext getFlowContext();

    /**
     * get 当前操作玩家的 userId
     * <pre>
     *     注意：{@link OperationContext#getFlowContext()} 必须存在
     * </pre>
     *
     * @return userId
     */
    default long getUserId() {
        return this.getFlowContext().getUserId();
    }

    /**
     * get 当前操作的玩家
     * <pre>
     *     注意：{@link OperationContext#getFlowContext()} 必须存在
     * </pre>
     *
     * @param <T> Player
     * @return 当前操作的玩家
     */
    default <T extends Player> T getPlayer() {
        Room room = this.getRoom();
        long userId = this.getUserId();
        return room.getPlayerById(userId);
    }

    /**
     * get room id
     *
     * @return room id
     */
    default long getRoomId() {
        return this.getRoom().getRoomId();
    }
}
