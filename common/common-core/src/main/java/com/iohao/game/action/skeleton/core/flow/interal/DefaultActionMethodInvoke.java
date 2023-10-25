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

import com.iohao.game.action.skeleton.core.flow.ActionMethodExceptionProcess;
import com.iohao.game.action.skeleton.core.flow.ActionMethodInvoke;
import com.iohao.game.action.skeleton.core.flow.FlowContext;

/**
 * default DefaultActionMethodInvoke
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
public final class DefaultActionMethodInvoke implements ActionMethodInvoke {

    @Override
    public Object invoke(final FlowContext flowContext) {
        final var actionCommand = flowContext.getActionCommand();
        final Object controller = flowContext.getActionController();
        final var params = flowContext.getMethodParams();

        // 方法下标
        var actionMethodIndex = actionCommand.getActionMethodIndex();
        // 方法访问器
        var actionMethodAccess = actionCommand.getActionMethodAccess();

        try {
            // 调用开发者在 action 类中编写的业务方法，即 action
            return actionMethodAccess.invoke(controller, actionMethodIndex, params);
        } catch (Throwable e) {
            // true 业务方法有异常
            flowContext.setError(true);

            // 异常处理
            var barSkeleton = flowContext.getBarSkeleton();
            ActionMethodExceptionProcess exceptionProcess = barSkeleton.getActionMethodExceptionProcess();
            // 把业务方法抛出的异常,交由异常处理类来处理
            return exceptionProcess.processException(e);
        }
    }
}