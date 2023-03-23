/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
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
package com.iohao.game.action.skeleton.core;

import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.core.flow.FlowContextKit;

/**
 * 该handler用于执行 {@link ActionCommand} 对象
 *
 * @author 渔民小镇
 * @date 2021-12-12
 */
sealed class ActionCommandHandler implements Handler permits ActionCommandTryHandler {

    @Override
    public boolean handler(final FlowContext flowContext) {

        // 设置 flowContext 的一些属性
        this.settingFlowContext(flowContext);

        // actionCommand 命令流程执行器
        DefaultActionCommandFlowExecute.me().execute(flowContext);

        return true;
    }

    protected void settingFlowContext(FlowContext flowContext) {
        /*
         * 做一些兼容，这部分逻辑在 RequestMessageClientProcessor 已经设置过一次，
         * 这里做兼容的目的有两个：
         * 1 防止开发者将 RequestMessageClientProcessor 移除，并做了相关的自定义。
         * 2 业务框架可以单独做测试
         */
        FlowContextKit.employ(flowContext);

        // 业务框架
        BarSkeleton barSkeleton = flowContext.getBarSkeleton();

        // 参数解析器
        var paramParser = barSkeleton.getActionMethodParamParser();
        // 得到业务方法的参数列表，并验证
        var params = paramParser.listParam(flowContext);
        // 业务方法参数 save to flowContext
        flowContext.setMethodParams(params);
    }
}
