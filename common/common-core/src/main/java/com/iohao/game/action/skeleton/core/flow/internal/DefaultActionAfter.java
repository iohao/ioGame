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
package com.iohao.game.action.skeleton.core.flow.internal;

import com.iohao.game.action.skeleton.core.ActionCommand;
import com.iohao.game.action.skeleton.core.commumication.ChannelContext;
import com.iohao.game.action.skeleton.core.flow.ActionAfter;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.core.flow.FlowContextKit;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;

/**
 * flow - 默认的 ActionAfter
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
public final class DefaultActionAfter implements ActionAfter {
    @Override
    public void execute(final FlowContext flowContext) {

        ChannelContext channelContext = FlowContextKit.getChannelContext(flowContext);

        // 有错误就响应给调用方
        final ResponseMessage response = flowContext.getResponse();
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
}
