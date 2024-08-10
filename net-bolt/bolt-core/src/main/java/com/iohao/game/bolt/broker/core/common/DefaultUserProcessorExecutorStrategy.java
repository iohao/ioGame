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

import com.iohao.game.bolt.broker.core.aware.UserProcessorExecutorAware;
import com.iohao.game.common.kit.MoreKit;
import com.iohao.game.common.kit.RuntimeKit;
import com.iohao.game.common.kit.concurrent.DaemonThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 默认策略
 *
 * @author 渔民小镇
 * @date 2022-11-11
 */
@Slf4j
final class DefaultUserProcessorExecutorStrategy implements UserProcessorExecutorStrategy {
    final Map<String, Executor> executorMap = new NonBlockingHashMap<>();

    @Override
    public Executor getExecutor(UserProcessorExecutorAware userProcessorExecutorAware) {

        if (userProcessorExecutorAware.inNettyThreadExecute()) {
            /*
             * 当 inNettyThreadExecute 为 true 时，即使设置了执行器也是无效的。
             * 如果开发者使用自定义的线程执行器，需要将 inNettyThreadExecute 设置为 false。
             *
             * 如果将 inNettyThreadExecute 设置为 false，会有两种情况
             *     1. 如果配置了自定义的线程执行器，则会优先使用自定义的线程执行器来执行业务；
             *     2. 如果没有配置，也就是返回 null，将会使用 bolt 默认的 ioThreadExecutor ；具体阅读 ProcessorManager.defaultExecutor 相关源码
             */
            return null;
        }

        String userProcessorName = userProcessorExecutorAware.getClass().getSimpleName();

        return switch (userProcessorName) {
            // RequestMessage 相关的单独一个池，使用单线程传递请求消息。
            case "RequestMessageClientProcessor" -> ofExecutorRequestMessage();
            case "RequestMessageBrokerProcessor" -> ofExecutorRequestMessage();
            // 其他 UserProcessor 使用 common 多线程消费任务
            default -> ofExecutorCommon();
        };
    }

    private Executor ofExecutorRequestMessage() {
        // 使用单线程传递请求消息
        return ofExecutorCommon("RequestMessage", 1);
    }

    private Executor ofExecutorCommon() {
        int corePoolSize = RuntimeKit.availableProcessors;
        return ofExecutorCommon("common", corePoolSize);
    }

    private Executor ofExecutorCommon(String name, int corePoolSize) {
        Executor executor = this.executorMap.get(name);

        if (Objects.isNull(executor)) {
            var tempExecutor = createExecutor(name, corePoolSize, corePoolSize);
            executor = MoreKit.firstNonNull(this.executorMap.putIfAbsent(name, tempExecutor), tempExecutor);

            if (executor != tempExecutor) {
                // 引用不相等就 shutdown
                ((ThreadPoolExecutor) tempExecutor).shutdown();
            }
        }

        return executor;
    }

    private Executor createExecutor(String userProcessorName, int corePoolSize, int maximumPoolSize) {

        /*
         * 下面对于 UserProcessor 提供了一些默认的 Executor 配置，
         * 开发者可以根据自身业务需要来定制 UserProcessorExecutorStrategy。
         */

        String namePrefix = String.format("Processor-Executor-%s-%d"
                , userProcessorName
                , maximumPoolSize);

        DaemonThreadFactory threadFactory = new DaemonThreadFactory(namePrefix);

        var executor = new ThreadPoolExecutor(
                corePoolSize, maximumPoolSize,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                threadFactory);

        // Processor-Executor
        log.debug("{} 【corePoolSize:{}】【maximumPoolSize:{}】 ",
                namePrefix,
                corePoolSize,
                maximumPoolSize
        );

        // 小预热
        for (int i = 0; i < corePoolSize; i++) {
            executor.execute(() -> {
            });
        }

        return executor;
    }
}
