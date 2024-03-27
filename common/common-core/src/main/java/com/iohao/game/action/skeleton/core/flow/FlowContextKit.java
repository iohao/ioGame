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
package com.iohao.game.action.skeleton.core.flow;

import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.commumication.ChannelContext;
import com.iohao.game.action.skeleton.core.flow.attr.FlowAttr;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import lombok.experimental.UtilityClass;

import java.util.Objects;

/**
 * @author 渔民小镇
 * @date 2023-03-23
 */
@UtilityClass
public class FlowContextKit {

    /** rpc oneway request */
    static final byte REQUEST_ONEWAY = (byte) 0x02;

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

    public ChannelContext getChannelContext(FlowContext flowContext) {
        ResponseMessage response = flowContext.getResponse();
        HeadMetadata headMetadata = response.getHeadMetadata();

        byte rpcCommandType = headMetadata.getRpcCommandType();

        if (rpcCommandType == REQUEST_ONEWAY) {
            return flowContext.option(FlowAttr.brokerClientContext);
        } else {
            return flowContext.option(FlowAttr.channelContext);
        }
    }
}
