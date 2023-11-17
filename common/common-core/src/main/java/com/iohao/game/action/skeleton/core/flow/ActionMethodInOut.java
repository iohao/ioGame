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
package com.iohao.game.action.skeleton.core.flow;


import com.iohao.game.action.skeleton.core.ActionCommand;

/**
 * inout 接口
 * <pre>
 *     <a href="https://www.yuque.com/iohao/game/bsgvzglvlr5tenao">插件介绍-文档</a>
 *
 *     {@link ActionCommand} 执行前与执行后的逻辑钩子类
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
