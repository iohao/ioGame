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
import com.iohao.game.action.skeleton.core.exception.MsgException;
import com.iohao.game.action.skeleton.core.flow.ActionMethodResultWrap;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.core.flow.attr.FlowAttr;
import com.iohao.game.action.skeleton.core.flow.parser.MethodParser;
import com.iohao.game.action.skeleton.core.flow.parser.MethodParsers;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * 结果包装器
 *
 * @author 渔民小镇
 * @date 2022-01-12
 */
@Slf4j
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

        MethodParser paramParser = MethodParsers.me().getMethodParser(actionMethodReturnInfo);

        // 根据返回值类型
        Object methodResult = paramParser.parseResult(actionMethodReturnInfo, result);
        // 重新赋值一下 methodResult 到 flowContext 中，方便在 DebugInOut 中的打印
        flowContext.setMethodResult(methodResult);

        // 业务方法返回值
        if (Objects.nonNull(methodResult)) {
            responseMessage.setData(methodResult);
        }
    }
}
