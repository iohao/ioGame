/*
 * ioGame 
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
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
