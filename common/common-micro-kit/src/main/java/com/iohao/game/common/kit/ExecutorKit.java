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
package com.iohao.game.common.kit;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 线程执行器工具
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
@UtilityClass
public class ExecutorKit {
    /**
     * 创建单个线程执行器
     *
     * @param namePrefix 线程名
     * @return 执行器
     */
    public ExecutorService newSingleThreadExecutor(String namePrefix) {
        ThreadFactory threadFactory = createThreadFactory(namePrefix);
        return newSingleThreadExecutor(threadFactory);
    }

    /**
     * 创建单个线程执行器
     *
     * @param threadFactory 线程创建工厂
     * @return 执行器
     */
    public ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory) {
        return Executors.newSingleThreadExecutor(threadFactory);
    }

    /**
     * 创建线程池
     *
     * @param namePrefix 线程名
     * @return 执行器
     */
    public ExecutorService newCacheThreadPool(String namePrefix) {
        ThreadFactory threadFactory = createThreadFactory(namePrefix);
        return newCacheThreadPool(threadFactory);
    }

    /**
     * 线程池
     *
     * @param threadFactory 线程创建工厂
     * @return 执行器
     */
    public ExecutorService newCacheThreadPool(ThreadFactory threadFactory) {
        return Executors.newCachedThreadPool(threadFactory);
    }

    /**
     * 创建固定大小线程执行器
     *
     * @param corePoolSize  容量
     * @param threadFactory 线程工厂
     * @return 执行器
     */
    public ExecutorService newFixedThreadPool(int corePoolSize, ThreadFactory threadFactory) {
        return Executors.newFixedThreadPool(corePoolSize, threadFactory);
    }

    /**
     * 创建固定大小线程执行器
     *
     * @param corePoolSize 容量
     * @param namePrefix   线程名
     * @return 执行器
     */
    public ExecutorService newFixedThreadPool(int corePoolSize, String namePrefix) {
        ThreadFactory threadFactory = createThreadFactory(namePrefix);
        return newFixedThreadPool(corePoolSize, threadFactory);
    }

    /**
     * 创建单个线程调度执行器
     *
     * @param threadFactory 线程创建工厂
     * @return 调度 执行器
     */
    public ScheduledExecutorService newSingleScheduled(ThreadFactory threadFactory) {
        return newScheduled(1, threadFactory);
    }

    /**
     * 创建单个线程调度执行器
     *
     * @param namePrefix 线程名
     * @return 调度 执行器
     */
    public ScheduledExecutorService newSingleScheduled(String namePrefix) {
        ThreadFactory threadFactory = createThreadFactory(namePrefix);
        return newScheduled(1, threadFactory);
    }

    /**
     * 创建指定数量 - 的线程调度执行器
     *
     * @param corePoolSize 容量
     * @param namePrefix   线程名
     * @return 指定数量的 调度 执行器
     */
    public ScheduledExecutorService newScheduled(int corePoolSize, String namePrefix) {
        ThreadFactory threadFactory = createThreadFactory(namePrefix);
        return newScheduled(corePoolSize, threadFactory);
    }

    /**
     * 创建指定数量 - 的线程调度执行器
     *
     * @param corePoolSize  容量
     * @param threadFactory 线程创建工厂
     * @return 指定数量的 调度 执行器
     */
    public ScheduledExecutorService newScheduled(int corePoolSize, ThreadFactory threadFactory) {
        return new ScheduledThreadPoolExecutor(corePoolSize, threadFactory);
    }

    /**
     * 创建 线程工厂
     * <pre>
     *     daemon 参数默认是 true
     * </pre>
     *
     * @param namePrefix 线程名
     * @return 线程工厂
     */
    public ThreadFactory createThreadFactory(String namePrefix) {
        return createThreadFactory(namePrefix, true);
    }

    /**
     * 创建线程工厂
     *
     * @param namePrefix 线程名前缀
     * @param daemon     置是否守护线程
     * @return 线程工厂
     */
    public ThreadFactory createThreadFactory(@NonNull String namePrefix, boolean daemon) {
        final AtomicLong threadNumber = new AtomicLong();

        return runnable -> {

            String threadName = namePrefix + threadNumber.getAndIncrement();

            Thread thread = new Thread(runnable, threadName);

            thread.setDaemon(daemon);

            return thread;
        };

    }
}
