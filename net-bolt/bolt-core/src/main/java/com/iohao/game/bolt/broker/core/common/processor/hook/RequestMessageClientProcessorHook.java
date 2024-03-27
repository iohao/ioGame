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

/**
 * 逻辑服业务处理钩子接口
 *
 * @author 渔民小镇
 * @date 2022-06-26
 */
public interface RequestMessageClientProcessorHook {

    /**
     * 钩子流程逻辑
     * <pre>
     *     通过业务框架把请求派发给指定的业务类（action）来处理
     *
     *     用于在 bolt 接收请求时，对该请求做一些类似线程编排的事
     *     当然，这个编排是由开发者自定义的
     * </pre>
     *
     * @param barSkeleton 业务框架
     * @param flowContext 业务框架 flow 上下文
     */
    void processLogic(BarSkeleton barSkeleton, FlowContext flowContext);

}
