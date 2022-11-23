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
package com.iohao.game.action.skeleton.core;

import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.RequestMessage;

/**
 * 该handler用于执行 {@link ActionCommand} 对象
 *
 * @author 渔民小镇
 * @date 2021-12-12
 */
public final class ActionCommandHandler implements Handler {

    @Override
    public boolean handler(final FlowContext flowContext) {
        // 设置 flowContext 的一些属性
        this.settingFlowContext(flowContext);

        // actionCommand 命令流程执行器
        DefaultActionCommandFlowExecute.me().execute(flowContext);

        return true;
    }

    private void settingFlowContext(FlowContext flowContext) {
        // 业务框架
        BarSkeleton barSkeleton = flowContext.getBarSkeleton();
        // 请求参数
        RequestMessage request = flowContext.getRequest();
        // 元信息
        HeadMetadata headMetadata = request.getHeadMetadata();

        // 得到路由信息
        int cmdMerge = headMetadata.getCmdMerge();
        // 命令域管理器
        var actionCommandRegions = barSkeleton.actionCommandRegions;
        // 根据路由信息得到 ActionCommand
        var actionCommand = actionCommandRegions.getActionCommand(cmdMerge);
        flowContext.setActionCommand(actionCommand);

        // 响应对象创建器
        var responseMessageCreate = barSkeleton.getResponseMessageCreate();
        // 创建响应对象
        var responseMessage = responseMessageCreate.createResponseMessage();
        request.settingCommonAttr(responseMessage);
        // 当前用户 id
        long userId = headMetadata.getUserId();
        flowContext
                .setResponse(responseMessage)
                .setUserId(userId);

        // 参数解析器
        var paramParser = barSkeleton.getActionMethodParamParser();
        // 得到业务方法的参数列表，并验证
        var params = paramParser.listParam(flowContext);
        // 业务方法参数 save to flowContext
        flowContext.setMethodParams(params);
    }
}
