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
package com.iohao.game.action.skeleton.core.flow.interal;

import com.iohao.game.action.skeleton.core.ActionCommand;
import com.iohao.game.action.skeleton.core.commumication.ChannelContext;
import com.iohao.game.action.skeleton.core.flow.ActionAfter;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.core.flow.attr.FlowAttr;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * 默认的ActionAfter
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
@Slf4j
public final class DefaultActionAfter implements ActionAfter {
    /** rpc oneway request */
    static final byte REQUEST_ONEWAY = (byte) 0x02;

    @Override
    public void execute(final FlowContext flowContext) {
        final ResponseMessage response = flowContext.getResponse();

        ChannelContext channelContext = getChannelContext(flowContext);

        if (Objects.isNull(channelContext)) {
            return;
        }

        // 有错误就响应给调用方
        if (response.hasError()) {
            channelContext.sendResponse(response);
            return;
        }

        // action 方法返回值是 void 的，不做处理
        ActionCommand actionCommand = flowContext.getActionCommand();
        if (actionCommand.getActionMethodReturnInfo().isVoid()) {
            return;
        }

        // 将数据回传给调用方
        channelContext.sendResponse(response);
    }

    private ChannelContext getChannelContext(FlowContext flowContext) {
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
