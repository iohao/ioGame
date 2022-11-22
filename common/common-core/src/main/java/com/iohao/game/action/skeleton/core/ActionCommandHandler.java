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

        // 给 FlowContext 设置一些属性
        this.settingActionCommand(flowContext);

        // actionCommand 命令流程执行器
        DefaultActionCommandFlowExecute.me().execute(flowContext);

        return true;
    }

    private void settingActionCommand(final FlowContext flowContext) {

        RequestMessage request = flowContext.getRequest();
        HeadMetadata headMetadata = request.getHeadMetadata();

        // 得到路由信息
        int cmdMerge = headMetadata.getCmdMerge();
        var cmd = CmdKit.getCmd(cmdMerge);
        var subCmd = CmdKit.getSubCmd(cmdMerge);

        // 得到业务框架
        var barSkeleton = flowContext.getBarSkeleton();
        // 命令域 管理器
        var actionCommandRegions = barSkeleton.actionCommandRegions;
        // 根据路由信息获取命令处理器
        var actionCommand = actionCommandRegions.getActionCommand(cmd, subCmd);

        flowContext.setActionCommand(actionCommand);
    }
}
