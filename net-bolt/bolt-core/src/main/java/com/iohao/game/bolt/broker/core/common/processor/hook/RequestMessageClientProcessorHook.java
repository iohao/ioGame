/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
