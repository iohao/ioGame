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
package com.iohao.game.common.kit.concurrent.executor;

/**
 * 线程执行器管理域
 * <pre>
 *     支持指定执行器来消费任务
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-12-01
 */
public interface ThreadExecutorRegion {
    /**
     * 根据 index 获取对应的 Executor
     *
     * @param index index 不能是负数
     * @return Executor 线程执行器
     */
    ThreadExecutor getThreadExecutor(long index);

    /**
     * 根据 index 获取对应的 Executor 来执行任务
     *
     * @param runnable 任务
     * @param index    index
     */
    default void execute(Runnable runnable, long index) {
        this.getThreadExecutor(index).execute(runnable);
    }
}
