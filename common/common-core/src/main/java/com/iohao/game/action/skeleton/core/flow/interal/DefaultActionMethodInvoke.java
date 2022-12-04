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
