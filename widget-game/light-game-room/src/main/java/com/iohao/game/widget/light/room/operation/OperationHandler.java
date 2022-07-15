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
