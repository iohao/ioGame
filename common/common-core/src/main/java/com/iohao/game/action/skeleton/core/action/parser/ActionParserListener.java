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
package com.iohao.game.action.skeleton.core.action.parser;

import com.iohao.game.action.skeleton.core.BarSkeleton;

/**
 * action 构建时的监听器（钩子）
 *
 * @author 渔民小镇
 * @date 2024-04-30
 * @since 21.7
 */
public interface ActionParserListener {
    /**
     * subCmd action callback
     * <pre>
     *     每个 action 都会调用一次
     * </pre>
     *
     * @param context action 构建时的上下文
     */
    void onActionCommand(ActionParserContext context);

    /**
     * 在 {@link ActionParserListener#onActionCommand(ActionParserContext)} 之后执行
     *
     * @param barSkeleton 业务框架
     */
    default void onAfter(BarSkeleton barSkeleton) {
    }
}
