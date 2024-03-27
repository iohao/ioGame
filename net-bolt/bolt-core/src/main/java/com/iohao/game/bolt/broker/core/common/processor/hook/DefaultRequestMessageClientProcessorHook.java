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
package com.iohao.game.bolt.broker.core.common.processor.hook;

import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.kit.ExecutorSelectKit;

/**
 * 框架提供的 RequestMessageClientProcessorHook 默认实现
 * <pre>
 *     userId 与 Executor 关联，确保同一玩家使用的业务线程是相同的。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-03-31
 */
final class DefaultRequestMessageClientProcessorHook implements RequestMessageClientProcessorHook {

    @Override
    public void processLogic(BarSkeleton barSkeleton, FlowContext flowContext) {
        boolean execute = ExecutorSelectKit.processLogic(barSkeleton, flowContext);

        if (!execute) {
            // 在当前线程中执行业务框架
            barSkeleton.handle(flowContext);
        }
    }
}