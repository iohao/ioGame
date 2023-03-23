/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
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
package com.iohao.game.action.skeleton.core.flow;

import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import lombok.experimental.UtilityClass;

import java.util.Objects;

/**
 * @author 渔民小镇
 * @date 2023-03-23
 */
@UtilityClass
public class FlowContextKit {
    /**
     * FlowContext 自身属性赋值
     *
     * @param flowContext flowContext
     */
    public void employ(FlowContext flowContext) {
        BarSkeleton barSkeleton = flowContext.getBarSkeleton();
        // 请求参数
        RequestMessage request = flowContext.getRequest();
        // 元信息
        HeadMetadata headMetadata = request.getHeadMetadata();

        // 路由
        if (Objects.isNull(flowContext.getActionCommand())) {
            // 得到路由信息
            int cmdMerge = headMetadata.getCmdMerge();
            // 命令域管理器
            var actionCommandRegions = barSkeleton.getActionCommandRegions();
            // 根据路由信息得到 ActionCommand
            var actionCommand = actionCommandRegions.getActionCommand(cmdMerge);

            flowContext.setActionCommand(actionCommand);
        }

        // 用户 id
        if (flowContext.getUserId() == 0) {
            long userId = headMetadata.getUserId();
            flowContext.setUserId(userId);
        }

        // 响应对象
        if (Objects.isNull(flowContext.getResponse())) {
            // 响应对象创建器
            var responseMessageCreate = barSkeleton.getResponseMessageCreate();
            // 创建响应对象
            var responseMessage = responseMessageCreate.createResponseMessage();
            request.settingCommonAttr(responseMessage);

            flowContext.setResponse(responseMessage);
        }
    }
}
