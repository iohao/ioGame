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

import com.iohao.game.common.kit.concurrent.FixedNameThreadFactory;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.concurrent.*;

/**
 * 线程执行器管理域父类
 *
 * @author 渔民小镇
 * @date 2023-12-01
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
abstract sealed class AbstractThreadExecutorRegion implements ThreadExecutorRegion
        permits
        UserThreadExecutorRegion,
        UserVirtualThreadExecutorRegion,
        SimpleThreadExecutorRegion {

    /** 线程执行器 */
    final ThreadExecutor[] threadExecutors;

    AbstractThreadExecutorRegion(String threadName, int executorSize) {
        this.threadExecutors = new ThreadExecutor[executorSize];

        for (int i = 0; i < executorSize; i++) {
            // 线程名：name-线程总数-当前线程编号
            int threadNo = i + 1;
            String threadNamePrefix = String.format("%s-%s-%s", threadName, executorSize, threadNo);
            var executor = this.createExecutorService(threadNamePrefix);
            this.threadExecutors[i] = new ThreadExecutor(threadNamePrefix, executor, threadNo);
        }
    }

    protected ExecutorService createExecutorService(String name) {
        ThreadFactory threadFactory = new FixedNameThreadFactory(name);

        return new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                threadFactory);
    }
}
