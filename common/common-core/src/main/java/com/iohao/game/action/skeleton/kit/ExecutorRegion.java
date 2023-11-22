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
package com.iohao.game.action.skeleton.kit;

import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 业务框架 action 线程 region
 * <pre>
 *     管理消费 action 的执行线程执行器 Executor
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-11-22
 * @see java.util.concurrent.Executor
 */
public interface ExecutorRegion {
    /**
     * 根据线程名得到 optional ThreadPoolExecutor
     *
     * @param threadName threadName
     * @return optional ThreadPoolExecutor，一定不为 null
     */
    Optional<ThreadPoolExecutor> getThreadPoolExecutorOptional(String threadName);

    /**
     * 根据线程名得到 ThreadPoolExecutor
     *
     * @param threadName threadName
     * @return ThreadPoolExecutor
     */
    default ThreadPoolExecutor getThreadPoolExecutor(String threadName) {
        return getThreadPoolExecutorOptional(threadName).orElse(null);
    }

    /**
     * get Executor
     *
     * @param threadName threadName
     * @return optional Executor，一定不为 null
     */
    Optional<Executor> getExecutorOptional(String threadName);

    /**
     * 当前剩余还没有执行的任务数
     *
     * @param threadName 线程名
     * @return 剩余还没有执行的任务数
     */
    int sizeRemaining(String threadName);
}
