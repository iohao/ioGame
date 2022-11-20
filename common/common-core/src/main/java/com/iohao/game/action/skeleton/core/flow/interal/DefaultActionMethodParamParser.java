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
import com.iohao.game.action.skeleton.core.ValidatorKit;
import com.iohao.game.action.skeleton.core.exception.ActionErrorEnum;
import com.iohao.game.action.skeleton.core.flow.ActionMethodParamParser;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.core.flow.attr.FlowAttr;
import com.iohao.game.action.skeleton.core.flow.parser.MethodParsers;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;

import java.util.Objects;

/**
 * 业务方法参数解析器
 *
 * @author 渔民小镇
 * @date 2022-01-12
 */
public final class DefaultActionMethodParamParser implements ActionMethodParamParser {

    @Override
    public Object[] listParam(final FlowContext flowContext) {

        var actionCommand = flowContext.getActionCommand();
        if (!actionCommand.isMethodHasParam()) {
            return METHOD_PARAMS;
        }

        // 请求、响应对象
        var request = flowContext.getRequest();
        var response = flowContext.getResponse();

        // 方法参数信息 数组
        final var paramInfos = actionCommand.getParamInfos();

        final var len = paramInfos.length;
        final var params = new Object[len];

        for (int i = 0; i < len; i++) {
            // 方法参数信息
            ActionCommand.ParamInfo paramInfo = paramInfos[i];
            Class<?> paramClazz = paramInfo.getActualTypeArgumentClazz();

            // flow 上下文
            if (FlowContext.class.isAssignableFrom(paramClazz)) {
                params[i] = flowContext;
                continue;
            }

            if (Objects.nonNull(request.getData())) {

                // 得到方法参数解析器，把字节解析成 pb 对象
                var methodParser = MethodParsers.me().getMethodParser(paramClazz);
                // 业务参数
                var param = methodParser.parseParam(request.getData(), paramInfo);
                params[i] = param;

                flowContext.option(FlowAttr.data, param);

                // 如果开启了验证
                if (paramInfo.isValidator()) {
                    extractedValidator(response, paramInfo, param);
                }
            }

        }

        return params;
    }

    private static void extractedValidator(ResponseMessage response, ActionCommand.ParamInfo paramInfo, Object param) {
        // 获取分组信息
        Class<?>[] groups = paramInfo.getValidatorGroups();
        // 进行 JSR380 相关的验证
        String validateMsg = ValidatorKit.validate(param, groups);
        // 有错误消息，表示验证不通过
        if (Objects.nonNull(validateMsg)) {
            response.setValidatorMsg(validateMsg);
            response.setResponseStatus(ActionErrorEnum.validateErrCode.getCode());
        }
    }

    public static DefaultActionMethodParamParser me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final DefaultActionMethodParamParser ME = new DefaultActionMethodParamParser();
    }
}
