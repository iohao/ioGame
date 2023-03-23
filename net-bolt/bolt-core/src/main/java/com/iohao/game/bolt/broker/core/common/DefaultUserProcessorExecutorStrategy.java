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
package com.iohao.game.bolt.broker.core.common;

import com.alipay.remoting.NamedThreadFactory;
import com.iohao.game.bolt.broker.core.aware.UserProcessorExecutorAware;
import com.iohao.game.common.kit.log.IoGameLoggerFactory;
import org.slf4j.Logger;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 默认策略
 *
 * @author 渔民小镇
 * @date 2022-11-11
 */
class DefaultUserProcessorExecutorStrategy implements UserProcessorExecutorStrategy {
    static final Logger log = IoGameLoggerFactory.getLoggerCommon();

    final AtomicInteger id = new AtomicInteger();
    final Executor commonExecutor;

    DefaultUserProcessorExecutorStrategy() {
        this.commonExecutor = createExecutor("common");
    }

    @Override
    public Executor getExecutor(UserProcessorExecutorAware userProcessorExecutorAware) {
        String userProcessorName = userProcessorExecutorAware.getClass().getSimpleName();

        var requestMessageClientProcessor = "RequestMessageClientProcessor";
        if (requestMessageClientProcessor.equals(userProcessorName)) {
            // 游戏逻辑服请求处理单独一个池
            return this.createExecutor(userProcessorName);
        }

        // 其他类型的消息处理共用一个池
        return this.commonExecutor;
    }

    Executor createExecutor(String userProcessorName) {

        /*
         * 目前 bolt 默认的 io 线程池的配置是
         * corePoolSize 20
         * maximumPoolSize 400
         * keepAliveTime 60
         * unit TimeUnit.SECONDS
         * workQueue ArrayBlockingQueue
         * NamedThreadFactory daemon=true
         *
         *
         *
         * 下面对于 UserProcessor 提供了一些默认的 Executor 配置，
         * 开发者可以根据自身业务需要来定制 UserProcessorExecutorStrategy。
         * 如果有好的优化建议，欢迎 PR
         */

        String namePrefix = String.format("Processor-Executor-%s-%d"
                , userProcessorName
                , id.incrementAndGet());

        int corePoolSize = Runtime.getRuntime().availableProcessors();
        int maximumPoolSize = corePoolSize << 1;

        var executor = new ThreadPoolExecutor(
                corePoolSize, maximumPoolSize,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new NamedThreadFactory(namePrefix, true));

        // Processor-Executor
        log.debug("{} 【corePoolSize:{}】【maximumPoolSize:{}】 ",
                namePrefix,
                corePoolSize,
                maximumPoolSize
        );

        // 小预热
        int ready = corePoolSize >> 1;
        for (int i = 0; i < ready; i++) {
            executor.execute(() -> {
            });
        }

        return executor;
    }
}
