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

/**
 * 玩法操作业务接口，将验证与操作分离。
 * <pre>
 *     坦克:
 *         射子弹、发射导弹 等操作
 *
 *     麻将：
 *         出牌、碰牌、胡牌等操作
 *
 *     斗地主：
 *         出牌等操作
 *
 *     回合制的：
 *         攻击等
 *
 *     桌游：
 *         发牌等
 *         出牌等
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-03-31
 * @since 21.8
 */
public interface OperationHandler {
    /**
     * 检测验证, 验证用户操作步骤是否合法
     *
     * @param context 操作上下文
     */
    default void verify(PlayerOperationContext context) {
    }

    /**
     * 检测验证，验证用户操作步骤是否合法，通过返回值来决定是否执行 {@link OperationHandler#process(PlayerOperationContext)} 方法。
     * <p>
     * 当返回 false 时，不会执行 process 方法，相当于丢弃该请求的处理。
     * 该方法与 {@link OperationHandler#verify(PlayerOperationContext)} 类似，
     * 只不过多了一个返回值来决定是否执行 process 方法。
     *
     * @param context 操作上下文
     * @return 当返回 true 时，会执行 {@link OperationHandler#process(PlayerOperationContext)} 方法
     * @since 21.23
     */
    default boolean processVerify(PlayerOperationContext context) {
        return true;
    }

    /**
     * 验证通过后, 执行处理
     *
     * @param context 操作上下文
     */
    void process(PlayerOperationContext context);
}
