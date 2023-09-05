/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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
package com.iohao.game.action.skeleton.core.flow.interal;

import com.iohao.game.action.skeleton.core.ActionCommand;
import com.iohao.game.action.skeleton.core.exception.MsgException;
import com.iohao.game.action.skeleton.core.flow.ActionMethodResultWrap;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.core.flow.attr.FlowAttr;
import com.iohao.game.action.skeleton.core.flow.parser.MethodParser;
import com.iohao.game.action.skeleton.core.flow.parser.MethodParsers;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;

/**
 * 结果包装器
 *
 * @author 渔民小镇
 * @date 2022-01-12
 */
public final class DefaultActionMethodResultWrap implements ActionMethodResultWrap {

    @Override
    public void wrap(final FlowContext flowContext) {
        final ResponseMessage responseMessage = flowContext.getResponse();
        // 业务方法的返回值
        final Object result = flowContext.getMethodResult();

        // 如果有异常错误，异常处理
        if (flowContext.isError()) {

            MsgException msgException = (MsgException) result;
            int code = msgException.getMsgCode();
            responseMessage.setResponseStatus(code);

            flowContext.option(FlowAttr.msgException, msgException.getMessage());

            return;
        }

        ActionCommand.ActionMethodReturnInfo actionMethodReturnInfo = flowContext.getActionCommand().getActionMethodReturnInfo();

        /*
         * action 方法返回值是 void 类型的，但是在广播时又复用了这个 action 为 void 的路由
         * 所以这里的逻辑最后以 result 是否为 null 来决定是否继续做一下步的处理
         *
         * action 返回值是 void && 结果为 null ，就不做处理
         * 如果 result 不为 null，就是来自广播的结果
         */
        if (actionMethodReturnInfo.isVoid() && result == null) {
            return;
        }

        // 得到 action 返回值的解析器，将解析后的结果保存到 flowContext 中
        MethodParser paramParser = MethodParsers.getMethodParser(actionMethodReturnInfo);
        Object methodResult = paramParser.parseResult(actionMethodReturnInfo, result);
        flowContext.setMethodResult(methodResult);

        // 将 action （业务方法返回值），保存到响应对象中
        responseMessage.setData(methodResult);
    }
}
