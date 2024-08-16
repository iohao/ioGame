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
package com.iohao.game.bolt.broker.core.common;

import com.iohao.game.common.kit.RuntimeKit;
import com.iohao.game.common.kit.concurrent.FixedNameThreadFactory;
import com.iohao.game.common.kit.concurrent.executor.ThreadExecutor;
import com.iohao.game.common.kit.concurrent.executor.ThreadExecutorRegion;

import java.util.concurrent.*;

/**
 * @author 渔民小镇
 * @date 2024-08-10
 * @since 21.15
 */
final class ProcessorSelectorThreadExecutorRegion implements ThreadExecutorRegion {
    final ThreadExecutor[] threadExecutors;
    final int executorLength;

    ProcessorSelectorThreadExecutorRegion() {
        threadExecutors = new ThreadExecutor[RuntimeKit.availableProcessors2n];
        executorLength = threadExecutors.length - 1;

        String threadName = "ProcessorSelector";
        for (int i = 0; i < threadExecutors.length; i++) {
            // 线程名：name-线程总数-当前线程编号
            int threadNo = i + 1;
            String threadNamePrefix = String.format("%s-%s-%s", threadName, threadExecutors.length, threadNo);
            var executor = this.createExecutorService(threadNamePrefix);
            this.threadExecutors[i] = new ThreadExecutor(threadNamePrefix, executor, threadNo);
        }
    }

    @Override
    public ThreadExecutor getThreadExecutor(long executorIndex) {
        int index = (int) (executorIndex & (this.executorLength));
        return this.threadExecutors[index];
    }

    private ExecutorService createExecutorService(String threadNamePrefix) {
        return new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new FixedNameThreadFactory(threadNamePrefix));
    }
}
