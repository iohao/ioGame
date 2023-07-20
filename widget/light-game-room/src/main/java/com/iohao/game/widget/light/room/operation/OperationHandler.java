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
package com.iohao.game.widget.light.room.operation;

import com.iohao.game.action.skeleton.core.exception.MsgException;

/**
 * 玩法操作业务类, 将验证与操作分离
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
 */
public interface OperationHandler {
    /**
     * 检测验证, 验证用户操作步骤是否合法
     *
     * @param context 操作上下文
     * @throws MsgException e
     */
    void verify(OperationContext context) throws MsgException;

    /**
     * 验证通过后, 执行处理
     *
     * @param context 操作上下文
     */
    void process(OperationContext context);
}
