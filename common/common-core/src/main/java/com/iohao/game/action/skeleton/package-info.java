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
/**
 * 业务框架 - <a href="https://www.yuque.com/iohao/game/wiwpwusmktrv35i4">业务框架简介</a>。
 * <pre>
 *     如果说  sofa-bolt 是为了让 Java 程序员能将更多的精力放在基于网络通信的业务逻辑实现上，而业务框架正是解决业务逻辑如何方便实现这一问题上。
 *     业务框架是游戏框架的一部分，职责是简化程序员的业务逻辑实现，业务框架使程序员能够快速的开始编写游戏业务。
 * </pre>
 * <p>
 * for example
 * <pre>{@code
 * @ActionController(1)
 * public class DemoAction {
 *     @ActionMethod(0)
 *     public HelloReq here(HelloReq helloReq) {
 *         // 业务数据
 *         var newHelloReq = new HelloReq();
 *         newHelloReq.name = helloReq.name + ", I'm here ";
 *         return newHelloReq;
 *     }
 *
 *     // 注意，这个方法只是为了演示而写的；（ioGame21 开始支持）
 *     // 效果与上面的方法一样，只不过是用广播（推送）的方式将数据返回给请求方
 *     @ActionMethod(0)
 *     public void here(HelloReq helloReq, FlowContext flowContext) {
 *         // 业务数据
 *         var newHelloReq = new HelloReq();
 *         newHelloReq.name = helloReq.name + ", I'm here ";
 *
 *         flowContext.broadcastMe(newHelloReq);
 *     }
 *
 *     // 跨服调用示例，下面分别展示了同步与异步回调的写法
 *     void testShowInvokeModule(FlowContext flowContext) {
 *         var cmdInfo = CmdInfo.of(1,0);
 *         var yourData = ... 你的请求参数
 *
 *         // 跨服请求（异步回调 - 无阻塞）-- 路由、请求参数、回调。
 *         flowContext.invokeModuleMessageAsync(cmdInfo, yourData, responseMessage -> {
 *             var helloReq = responseMessage.getData(HelloReq.class);
 *             // --- 此异步回调，具备全链路调用日志跟踪 ---
 *             log.info("异步回调 : {}", helloReq);
 *         });
 *
 *
 *         // 跨服请求（同步 - 阻塞）-- 路由、请求参数。
 *         ResponseMessage responseMessage = flowContext.invokeModuleMessage(cmdInfo, yourData);
 *         var helloReq = responseMessage.getData(HelloReq.class);
 *         log.info("同步调用 : {}", helloReq);
 *     }
 * }
 * }</pre>
 *
 * @author 渔民小镇
 * @date 2022-09-23
 */
package com.iohao.game.action.skeleton;