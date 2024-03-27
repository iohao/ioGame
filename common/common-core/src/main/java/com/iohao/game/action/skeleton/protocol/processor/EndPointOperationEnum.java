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
package com.iohao.game.action.skeleton.protocol.processor;

/**
 * 游戏逻辑服动态绑定枚举
 *
 * @author 渔民小镇
 * @date 2023-06-07
 */
public enum EndPointOperationEnum {
    /**
     * 追加绑定游戏逻辑服
     * <p>
     * 举例：
     * <pre>
     *     在追加之前，如果玩家已经绑定了 [1-1] 的游戏逻辑服 id；
     *
     *     现在玩家添加 [2-2、3-1] 的游戏逻辑服 id；
     *
     *     此时玩家所绑定的游戏逻辑服数据为 [1-1、2-2、3-1]，一共 3 条数据
     * </pre>
     */
    APPEND_BINDING,
    /**
     * 覆盖绑定游戏逻辑服
     * <p>
     * 举例：
     * <pre>
     *     在覆盖之前，如果玩家已经绑定了 [1-1] 的游戏逻辑服 id；
     *
     *     现在玩家添加 [2-2、3-1] 的游戏逻辑服 id；
     *
     *     此时玩家所绑定的游戏逻辑服数据为 [2-2、3-1]，一共 2 条数据，新的覆盖旧的；
     *     无论之前有没有绑定的数据，都将使用当前设置的值；
     * </pre>
     */
    COVER_BINDING,
    /**
     * 移除绑定的游戏逻辑服
     * <p>
     * 举例：
     * <pre>
     *     在移除之前，如果玩家已经绑定了 [1-1、2-2、3-1] 的游戏逻辑服 id；
     *
     *     现在玩家添加 [2-2、3-1] 为需要移除的游戏逻辑服 id；
     *
     *     此时玩家所绑定的游戏逻辑服数据为 [1-1]，一共 1 条数据，被移除了 2 条数据；
     * </pre>
     */
    REMOVE_BINDING,
    /**
     * 清除所有绑定的游戏逻辑服
     */
    CLEAR
}
