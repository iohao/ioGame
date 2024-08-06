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
package com.iohao.game.action.skeleton.core.exception;

import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * MsgExceptionKit
 *
 * @author 渔民小镇
 * @date 2024-05-12
 * @since 21.8
 */
@Slf4j
@UtilityClass
public class MsgExceptionKit {
    /**
     * 将异常发送给当前用户
     *
     * @param e           异常
     * @param flowContext 当前用户的 flowContext
     */
    public void onException(Throwable e, FlowContext flowContext) {
        Objects.requireNonNull(flowContext);

        ResponseMessage response = flowContext.getResponse();

        if (e instanceof MsgException msgException) {
            response.setError(msgException.getMsgExceptionInfo());
        } else {
            // 系统其它错误，一般不是用户自定义的异常，很可能是用户引入的第三方包抛出的异常
            response.setError(ActionErrorEnum.systemOtherErrCode);
            log.error(e.getMessage(), e);
        }

        response.setData(null);
        flowContext.broadcastMe(response);
    }
}
