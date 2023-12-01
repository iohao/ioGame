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
package com.iohao.game.common.kit.concurrent.executor;

import com.iohao.game.common.kit.concurrent.ThreadCreator;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 渔民小镇
 * @date 2023-12-01
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
abstract sealed class AbstractThreadExecutorRegion
        implements ThreadExecutorRegion
        permits UserThreadExecutorRegion, SimpleThreadExecutorRegion {

    final int executorLength;
    /** 线程执行器 */
    final ThreadExecutor[] threadExecutors;

    public AbstractThreadExecutorRegion(String threadName, int executorSize) {
        this.executorLength = executorSize;
        this.threadExecutors = new ThreadExecutor[executorSize];

        for (int i = 0; i < executorSize; i++) {
            // 线程名：RequestMessage-线程总数-当前线程编号
            int threadNo = i + 1;
            String threadNamePrefix = String.format("%s-%s-%s", threadName, executorSize, threadNo);

            var executor = new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(),
                    new InternalThreadFactory(threadNamePrefix));

            this.threadExecutors[i] = new ThreadExecutor(threadNamePrefix, executor, threadNo);
        }
    }

    private static class InternalThreadFactory extends ThreadCreator implements ThreadFactory {

        public InternalThreadFactory(String threadNamePrefix) {
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
}
