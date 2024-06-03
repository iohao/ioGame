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
package com.iohao.game.common.kit.concurrent;

/**
 * 任务监听回调，使用场景有：一次性延时任务、任务调度、轻量可控的延时任务、轻量的定时入库辅助功能 ...等其他扩展场景。
 * <a href="https://www.yuque.com/iohao/game/gzsl8pg0si1l4bu3">相关文档</a>
 *
 * <pre>
 *     这些使用场景都有一个共同特点，即监听回调。接口提供了 4 个方法，如下
 *     1. {@link TaskListener#onUpdate()}，监听回调
 *     2. {@link TaskListener#triggerUpdate()}，是否触发 {@link TaskListener#onUpdate()} 监听回调方法
 *     3. {@link TaskListener#onException(Throwable)} ，异常回调。在执行 {@link TaskListener#triggerUpdate()} 和 {@link TaskListener#onUpdate()} 方法时，如果触发了异常，异常将被该方法捕获。
 *     4. {@link TaskListener#getExecutor()}，指定执行器来执行上述方法，目的是不占用业务线程。
 * </pre>
 *
 *
 * @author 渔民小镇
 * @date 2023-12-06
 * @since 21.9
 */
public interface TaskListener extends CommonTaskListener {
}
