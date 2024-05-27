/*
 * ioGame
 * Copyright (C) 2021 - present  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General  License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General  License for more details.
 *
 * You should have received a copy of the GNU Affero General  License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.iohao.game.widget.light.room.flow;

import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.common.kit.attr.AttrOption;
import com.iohao.game.common.kit.attr.AttrOptions;

/**
 * 上下文 - 创建房间信息（及玩法规则）
 *
 * @author 渔民小镇
 * @date 2022-03-31
 * @since 21.8
 */
public interface RoomCreateContext {
    /**
     * get 游戏 id
     *
     * @return 游戏 id
     */
    long getGameId();

    /**
     * setGameId
     *
     * @param gameId 游戏 id
     */
    RoomCreateContext setGameId(long gameId);

    /**
     * get 房间创建者
     *
     * @return 房间创建者
     */
    long getCreatorUserId();

    /**
     * get 房间空间大小
     *
     * @return 房间空间大小
     */
    int getSpaceSize();

    /**
     * get 开始游戏需要的最小人数
     *
     * @return 开始游戏需要的最小人数
     */
    int getStartGameMinSpaceSize();

    /**
     * 设置房间空间大小
     *
     * @param spaceSize 房间空间
     * @return this
     */
    default RoomCreateContext setSpaceSize(int spaceSize) {
        return setSpaceSize(spaceSize, spaceSize);
    }

    /**
     * 设置房间空间大小
     * <pre>
     *     如 spaceSize = 10，表示房间最大可容纳 10 人，而开始游戏并不一定需要满足 10 人。
     *     而 startGameMinSpaceSize 则表示开始游戏的前提条件所需要的最小人数，
     *     当 startGameMinSpaceSize = 2 时，则表示当房间内有 2 个玩家时就能开始游戏。
     * </pre>
     *
     * @param spaceSize             房间空间
     * @param startGameMinSpaceSize 开始游戏需要的最小人数
     * @return this
     */
    RoomCreateContext setSpaceSize(int spaceSize, int startGameMinSpaceSize);

    /**
     * 获取动态成员属性
     *
     * @return 动态成员属性
     */
    AttrOptions getOptions();

    /**
     * get 动态属性，获取选项值，如果选项不存在，返回默认值。
     *
     * @param option 选项值
     * @return 如果 option 不存在，则使用默认的 option 值。
     */
    default <T> T option(AttrOption<T> option) {
        return this.getOptions().option(option);
    }

    /**
     * 设置动态属性。设置一个具有特定值的新选项，使用 null 值删除前一个设置的 {@link AttrOption}。
     *
     * @param option 选项值
     * @param value  选项值, null 用于删除前一个 {@link AttrOption}.
     * @return this
     */
    default <T> RoomCreateContext option(AttrOption<T> option, T value) {
        this.getOptions().option(option, value);
        return this;
    }

    /**
     * 创建一个 RoomCreateContext 对象，使用默认实现
     *
     * @param creatorUserId creatorUserId 房间创建者
     * @return default RoomCreateContext
     */
    static RoomCreateContext of(long creatorUserId) {
        return new SimpleRoomCreateContext(creatorUserId);
    }

    /**
     * 创建一个 RoomCreateContext 对象，使用默认实现
     *
     * @param flowContext flowContext 房间创建者
     * @return default RoomCreateContext
     */
    static RoomCreateContext of(FlowContext flowContext) {
        long userId = flowContext.getUserId();
        return of(userId);
    }

    /**
     * 创建一个 RoomCreateContext 对象，使用默认实现
     *
     * @return default RoomCreateContext
     */
    static RoomCreateContext of() {
        return of(0);
    }
}