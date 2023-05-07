/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General  License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General  License for more details.
 *
 * You should have received a copy of the GNU General  License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.iohao.game.action.skeleton.core.flow.interal;

import com.iohao.game.action.skeleton.core.ActionCommand;
import com.iohao.game.action.skeleton.core.commumication.ChannelContext;
import com.iohao.game.action.skeleton.core.flow.ActionAfter;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.core.flow.attr.FlowAttr;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;

/**
 * 默认的ActionAfter
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
public final class DefaultActionAfter implements ActionAfter {
    /** rpc oneway request */
    static final byte REQUEST_ONEWAY = (byte) 0x02;

    @Override
    public void execute(final FlowContext flowContext) {
        final ResponseMessage response = flowContext.getResponse();

        ChannelContext channelContext = getChannelContext(flowContext);

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
