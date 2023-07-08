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
package com.iohao.game.external.client.core;

import com.alipay.remoting.rpc.RpcCommandType;
import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.CmdKit;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.core.flow.FlowContextKit;
import com.iohao.game.action.skeleton.core.flow.attr.FlowAttr;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.external.core.message.ExternalMessage;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 交给 client action 处理
 *
 * @author 渔民小镇
 * @date 2023-05-31
 */
@Slf4j
@UtilityClass
class ClientActionProcess {
    void action(ExternalMessage externalMessage, BarSkeleton barSkeleton) {

        RequestMessage request = ClientActionProcess.mapper(externalMessage);

        // 业务框架 flow 上下文
        FlowContext flowContext = barSkeleton
                // 业务框架 flow 上下文 工厂
                .getFlowContextFactory()
                // 创建 flow 上下文
                .createFlowContext();

        // 设置请求参数
        flowContext.setRequest(request);
        flowContext.setBarSkeleton(barSkeleton);
        flowContext.option(FlowAttr.logicServerId, "client-100");
        flowContext.option(FlowAttr.logicServerTag, "模拟客户端");

        // 设置 flowContext 的一些属性值
        FlowContextKit.employ(flowContext);

        barSkeleton.handle(flowContext);
    }

    private RequestMessage mapper(ExternalMessage message) {

        int cmdMerge = message.getCmdMerge();
        byte[] data = message.getData();

        RequestMessage requestMessage = createRequestMessage(cmdMerge, data);
        HeadMetadata headMetadata = requestMessage.getHeadMetadata();
        headMetadata.setMsgId(message.getMsgId());

        return requestMessage;
    }


    /**
     * 创建请求消息
     *
     * @param cmdMerge 路由 {@link CmdKit#merge(int, int)}
     * @param data     业务数据 byte[]
     * @return 请求消息
     */
    private RequestMessage createRequestMessage(int cmdMerge, byte[] data) {

        // 元信息
        HeadMetadata headMetadata = new HeadMetadata()
                .setCmdMerge(cmdMerge)
                .setRpcCommandType(RpcCommandType.REQUEST_ONEWAY);

        // 请求
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setHeadMetadata(headMetadata);

        requestMessage.setData(data);

        return requestMessage;
    }
}
