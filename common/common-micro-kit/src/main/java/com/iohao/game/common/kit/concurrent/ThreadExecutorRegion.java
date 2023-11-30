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
package com.iohao.game.common.kit.concurrent;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程执行器管理域
 * <pre>
 *     默认的实现中使用 Executor[] 数组，具体数量是不大于 Runtime.getRuntime().availableProcessors() 的 2 次幂。
 *     假设 availableProcessors 的值分别为 4、8、12、16、32 时，对应的数量则是 4、8、8、16、32。
 *
 *     4、8、12、16、32 （availableProcessors 的值）
 *     4、8、 8、16、32 （对应的数量）
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-11-30
 */
public final class ThreadExecutorRegion {
    final int executorLength;
    /** 线程执行器 */
    final ThreadExecutor[] threadExecutors;

    ThreadExecutorRegion() {
        this("RequestMessage");
    }

    public ThreadExecutorRegion(String threadName) {

        int availableProcessors = availableProcessors2n();
        this.executorLength = availableProcessors;
        this.threadExecutors = new ThreadExecutor[this.executorLength];

        for (int i = 0; i < availableProcessors; i++) {
            // 线程名：RequestMessage-线程总数-当前线程编号
            int threadNo = i + 1;
            String threadNamePrefix = String.format("%s-%s-%s", threadName, availableProcessors, threadNo);

            var executor = new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(),
                    new TheThreadFactory(threadNamePrefix));

            this.threadExecutors[i] = new ThreadExecutor(threadNamePrefix, executor, threadNo);
        }
    }

    public void execute(Runnable runnable, long userId) {
        // 根据 userId 获取对应的 Executor
        this.getThreadExecutor(userId).execute(runnable);
    }

    public ThreadExecutor getThreadExecutor(long userId) {
        int index = (int) (userId & (executorLength - 1));
        return this.threadExecutors[index];
    }

    private int availableProcessors2n() {
        int n = Runtime.getRuntime().availableProcessors();
        n |= (n >> 1);
        n |= (n >> 2);
        n |= (n >> 4);
        n |= (n >> 8);
        n |= (n >> 16);
        return (n + 1) >> 1;
    }

    private static class TheThreadFactory extends ThreadCreator implements ThreadFactory {

        public TheThreadFactory(String threadNamePrefix) {
            super(threadNamePrefix);
            this.setDaemon(true);
        }

        @Override
        public Thread newThread(Runnable runnable) {
            return createThread(runnable);
        }

        @Override
        protected String nextThreadName() {
            return this.getThreadNamePrefix();
        }
    }

    public static ThreadExecutorRegion me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final ThreadExecutorRegion ME = new ThreadExecutorRegion();
    }
}
