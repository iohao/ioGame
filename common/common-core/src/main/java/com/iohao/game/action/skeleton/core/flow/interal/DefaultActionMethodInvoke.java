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

        // 方法声明了异常的处理方式
        try {
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


    private DefaultActionMethodInvoke() {

    }

    public static DefaultActionMethodInvoke me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final DefaultActionMethodInvoke ME = new DefaultActionMethodInvoke();
    }
}
