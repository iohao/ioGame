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
package com.iohao.game.bolt.broker.core.common.processor.hook;

import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.common.kit.ExecutorKit;
import com.iohao.game.common.kit.concurrent.ThreadCreator;

import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

/**
 * 框架提供的 RequestMessageClientProcessorHook 默认实现
 * <pre>
 *     userId 与 Executor 关联，确保同一玩家使用的业务线程是相同的。
 *
 *     如果玩家没有登录，则使用的是当前线程，这个当前线程指的是 bolt 的用户线程
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-03-31
 */
final class DefaultRequestMessageClientProcessorHook implements RequestMessageClientProcessorHook {
    final Executor[] executors;
    final int executorLength;

    public DefaultRequestMessageClientProcessorHook() {
        int availableProcessors = availableProcessors2n();
        executorLength = availableProcessors;
        executors = new Executor[executorLength];

        for (int i = 0; i < availableProcessors; i++) {
            // 线程名：RequestMessage-线程总数-当前线程编号
            int threadNo = i + 1;
            String threadNamePrefix = String.format("RequestMessage-%s-%s", availableProcessors, threadNo);
            executors[i] = ExecutorKit.newSingleThreadExecutor(new TheThreadFactory(threadNamePrefix));
        }
    }

    @Override
    public void processLogic(BarSkeleton barSkeleton, FlowContext flowContext) {
        long userId = flowContext.getUserId();

        if (userId == 0) {
            HeadMetadata headMetadata = flowContext.getRequest().getHeadMetadata();
            // 如果没有登录，使用 channelId 计算；如果 channelId 不存在，则使用 cmd
            userId = Optional.ofNullable(headMetadata.getChannelId())
                    .map(String::hashCode)
                    .map(Math::abs)
                    .orElseGet(headMetadata::getCmdMerge);
        }

        // 根据 userId 获取对应的 Executor
        int index = (int) (userId & (executorLength - 1));

        // 执行业务框架
        executors[index].execute(() -> barSkeleton.handle(flowContext));
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
}