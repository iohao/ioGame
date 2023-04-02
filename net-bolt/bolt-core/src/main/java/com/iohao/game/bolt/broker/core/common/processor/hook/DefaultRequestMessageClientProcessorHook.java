/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iohao.game.bolt.broker.core.common.processor.hook;

import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.common.kit.ExecutorKit;
import com.iohao.game.common.kit.concurrent.DaemonThreadFactory;

import java.util.concurrent.Executor;

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
    final boolean the2N;

    public DefaultRequestMessageClientProcessorHook() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        executors = new Executor[availableProcessors];

        for (int i = 0; i < availableProcessors; i++) {
            String namePrefix = "RequestMessage-" + i;
            executors[i] = ExecutorKit.newSingleThreadExecutor(new DaemonThreadFactory(namePrefix));
        }

        executorLength = executors.length;
        the2N = (executorLength & (executorLength - 1)) == 0;
    }

    @Override
    public void processLogic(BarSkeleton barSkeleton, FlowContext flowContext) {
        long userId = flowContext.getUserId();

        if (userId == 0) {
            // 如果没有登录，使用 channelId 计算
            userId = flowContext.getRequest().getHeadMetadata().getChannelId().hashCode();
            userId = Math.abs(userId);
        }

        // 根据 userId 获取对应的 Executor
        int index = (int) (the2N ? (userId & (executorLength - 1)) : (userId % executorLength));

        // 执行业务框架
        executors[index].execute(() -> barSkeleton.handle(flowContext));
    }
}