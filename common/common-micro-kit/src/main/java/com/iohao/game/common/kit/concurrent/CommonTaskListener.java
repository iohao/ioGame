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

import java.util.concurrent.Executor;

/**
 * 任务监听回调
 *
 * @author 渔民小镇
 * @date 2024-05-31
 * @since 21.9
 */
interface CommonTaskListener {
    /**
     * 是否触发 onUpdate 监听回调方法
     *
     * @return true 执行 onUpdate 方法
     */
    default boolean triggerUpdate() {
        return true;
    }

    /**
     * Timer 监听回调
     */
    void onUpdate();

    /**
     * 异常回调
     * <pre>
     *     当 triggerUpdate 或 onUpdate 方法抛出异常时，将会传递到这里
     * </pre>
     *
     * @param e e
     */
    default void onException(Throwable e) {
        System.err.println(e.getMessage());
    }

    /**
     * 执行 onUpdate 的执行器
     * <pre>
     *     如果返回 null 将在 HashedWheelTimer 中执行。
     *
     *     如果有耗时的任务，比如涉及一些 io 操作的，建议指定执行器来执行当前回调（onUpdate 方法），以避免阻塞其他任务。
     * </pre>
     * 示例
     * <pre>{@code
     *     default Executor getExecutor() {
     *         // 耗时任务，指定一个执行器来消费当前 onUpdate
     *         return TaskKit.getCacheExecutor();
     *     }
     * }
     * </pre>
     *
     * @return 当返回值为 null 时，将使用当前线程（默认 HashedWheelTimer）执行，否则使用该执行器来执行
     */
    default Executor getExecutor() {
        return null;
    }
}
