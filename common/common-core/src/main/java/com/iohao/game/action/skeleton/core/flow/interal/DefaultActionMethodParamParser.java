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

            // 得到方法参数解析器，把字节解析成 action 业务参数
            var methodParser = MethodParsers.me().getMethodParser(paramClazz);
            var param = methodParser.parseParam(request.getData(), paramInfo);
            params[i] = param;

            flowContext.option(FlowAttr.actionBizParam, param);

            // 如果开启了验证
            if (paramInfo.isValidator()) {
                extractedValidator(response, paramInfo, param);
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
}
