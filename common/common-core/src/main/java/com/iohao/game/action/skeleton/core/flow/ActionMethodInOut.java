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
package com.iohao.game.action.skeleton.core.flow;


import com.iohao.game.action.skeleton.core.ActionCommand;

/**
 * inout 接口
 * <pre>
 *     {@link ActionCommand} 执行前,后的逻辑钩子类
 *
 *     毫无疑问的是这个类的方法名过于刺激，但并不会影响我们的发挥
 *
 *     通过这个接口,你可以做很多事情，当然这要看你的想象力有多丰富了
 *
 *     例如: 日志记录，执行时间打印。 等等 (可参考框架内置的实现类)
 *     see {@link com.iohao.game.action.skeleton.core.flow.interal.DebugInOut}
 * </pre>
 *
 * @author 渔民小镇
 * @date 2021-12-12
 */

public interface ActionMethodInOut {

    /**
     * fuck前
     * <pre>
     *     这个方法不要做耗时计算, 因为是在执行你的业务方法前运行的.
     *     建议做一些时间记录等非耗时运算
     * </pre>
     *
     * @param flowContext inout 上下文
     */
    void fuckIn(FlowContext flowContext);

    /**
     * fuck后
     * <pre>
     *     当执行这个方法时, 已经把响应数据发送到客户端了
     * </pre>
     *
     * @param flowContext inout 上下文
     */
    void fuckOut(FlowContext flowContext);
}
