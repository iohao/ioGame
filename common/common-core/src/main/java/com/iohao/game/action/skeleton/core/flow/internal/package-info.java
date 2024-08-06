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
 * 业务框架 - <a href="https://www.yuque.com/iohao/game/bsgvzglvlr5tenao">业务框架插件</a>。
 * <pre>
 *     ioGame ActionMethodInOut 是业务框架的插件机制，通过这个接口你可以做很多事情，这要看你的想象力有多丰富了。比如：
 *     1. 开发者想记录执行时间比较长的 action 业务
 *     2. 哪个业务方法执行的次数最多
 *     3. 将 userId 保存到 ThreadLocal 中
 *
 *     ioGame 已经提供了一些内置插件，随着时间的推移，插件的数量会不断的增加。开发者如有需要，可扩展一些符合自身业务的插件。
 *     不同的插件提供了不同的关注点，比如我们可以使用调用、监控等插件相互配合，可以让我们在开发阶段就知道是否存在性能问题。
 *     合理利用好各个插件，可以让我们在开发阶段就能知道问题所在，提前发现问题，提前预防问题。
 * </pre>
 * for example
 * <pre>{@code
 * // 业务框架构建器
 * BarSkeletonBuilder builder = ...;
 *
 * // 控制台输出插件，将插件添加到业务框架中
 * var debugInOut = new DebugInOut();
 * builder.addInOut(debugInOut);
 *
 * // action 调用统计插件，将插件添加到业务框架中
 * var statActionInOut = new StatActionInOut();
 * builder.addInOut(statActionInOut);
 *
 * // 业务线程监控插件，将插件添加到业务框架中
 * var threadMonitorInOut = new ThreadMonitorInOut();
 * builder.addInOut(threadMonitorInOut);
 *
 * // 全链路调用日志跟踪插件，将插件添加到业务框架中
 * builder.addInOut(new TraceIdInOut());
 * }</pre>
 *
 * @author 渔民小镇
 * @date 2024-08-05
 */
package com.iohao.game.action.skeleton.core.flow.internal;