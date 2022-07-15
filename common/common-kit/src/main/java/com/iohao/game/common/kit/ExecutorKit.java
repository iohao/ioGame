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
package com.iohao.game.common.kit;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import lombok.experimental.UtilityClass;

import java.util.concurrent.*;

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
    public static ExecutorService newSingleThreadExecutor(String namePrefix) {
        ThreadFactory threadFactory = createThreadFactory(namePrefix);
        return newSingleThreadExecutor(threadFactory);
    }

    /**
     * 创建单个线程执行器
     *
     * @param threadFactory 线程创建工厂
     * @return 执行器
     */
    public static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory) {
        return Executors.newSingleThreadExecutor(threadFactory);
    }

    /**
     * 创建线程池
     *
     * @param namePrefix 线程名
     * @return 执行器
     */
    public static ExecutorService newCacheThreadPool(String namePrefix) {
        ThreadFactory threadFactory = createThreadFactory(namePrefix);
        return newCacheThreadPool(threadFactory);
    }

    /**
     * 线程池
     *
     * @param threadFactory 线程创建工厂
     * @return 执行器
     */
    public static ExecutorService newCacheThreadPool(ThreadFactory threadFactory) {
        return Executors.newCachedThreadPool(threadFactory);
    }

    /**
     * 创建固定大小线程执行器
     *
     * @param corePoolSize  容量
     * @param threadFactory 线程工厂
     * @return 执行器
     */
    public static ExecutorService newFixedThreadPool(int corePoolSize, ThreadFactory threadFactory) {
        return Executors.newFixedThreadPool(corePoolSize, threadFactory);
    }

    /**
     * 创建固定大小线程执行器
     *
     * @param corePoolSize 容量
     * @param namePrefix   线程名
     * @return 执行器
     */
    public static ExecutorService newFixedThreadPool(int corePoolSize, String namePrefix) {
        ThreadFactory threadFactory = createThreadFactory(namePrefix);
        return newFixedThreadPool(corePoolSize, threadFactory);
    }

    /**
     * 创建单个线程调度执行器
     *
     * @param threadFactory 线程创建工厂
     * @return 调度 执行器
     */
    public static ScheduledExecutorService newSingleScheduled(ThreadFactory threadFactory) {
        return newScheduled(1, threadFactory);
    }

    /**
     * 创建单个线程调度执行器
     *
     * @param namePrefix 线程名
     * @return 调度 执行器
     */
    public static ScheduledExecutorService newSingleScheduled(String namePrefix) {
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
    public static ScheduledExecutorService newScheduled(int corePoolSize, String namePrefix) {
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
    public static ScheduledExecutorService newScheduled(int corePoolSize, ThreadFactory threadFactory) {
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
    public static ThreadFactory createThreadFactory(String namePrefix) {
        return createThreadFactory(namePrefix, true);
    }

    /**
     * 创建线程工厂
     *
     * @param namePrefix 线程名前缀
     * @param daemon     置是否守护线程
     * @return 线程工厂
     */
    public static ThreadFactory createThreadFactory(String namePrefix, boolean daemon) {
        return ThreadFactoryBuilder.create()
                .setNamePrefix(namePrefix)
                .setDaemon(daemon)
                .build();
    }
}
