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
package com.iohao.game.action.skeleton.core.flow.internal;

import com.iohao.game.action.skeleton.core.flow.ActionMethodInOut;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.core.flow.attr.FlowAttr;
import com.iohao.game.common.kit.MoreKit;
import com.iohao.game.common.kit.concurrent.executor.ThreadExecutor;
import lombok.Getter;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 业务框架插件 - <a href="https://www.yuque.com/iohao/game/zoqabk4gez3bckis">业务线程监控插件</a>
 * <p>
 * for example
 * <pre>{@code
 * BarSkeletonBuilder builder = ...;
 * // 业务线程监控插件，将插件添加到业务框架中
 * var threadMonitorInOut = new ThreadMonitorInOut();
 * builder.addInOut(threadMonitorInOut);
 * }</pre>
 * <p>
 * 打印预览
 * <pre>
 *     业务线程[RequestMessage-8-1] 共执行了 1 次业务，平均耗时 1 ms, 剩余 91 个任务未执行
 *     业务线程[RequestMessage-8-2] 共执行了 1 次业务，平均耗时 1 ms, 剩余 0 个任务未执行
 *     业务线程[RequestMessage-8-3] 共执行了 1 次业务，平均耗时 1 ms, 剩余 36 个任务未执行
 *     业务线程[RequestMessage-8-4] 共执行了 1 次业务，平均耗时 1 ms, 剩余 0 个任务未执行
 *     业务线程[RequestMessage-8-5] 共执行了 1 次业务，平均耗时 1 ms, 剩余 88 个任务未执行
 *     业务线程[RequestMessage-8-6] 共执行了 1 次业务，平均耗时 1 ms, 剩余 0 个任务未执行
 *     业务线程[RequestMessage-8-7] 共执行了 7 次业务，平均耗时 1 ms, 剩余 56 个任务未执行
 *     业务线程[RequestMessage-8-8] 共执行了 1 次业务，平均耗时 1 ms, 剩余 0 个任务未执行
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-11-22
 */
@Getter
public final class ThreadMonitorInOut implements ActionMethodInOut {
    final ThreadMonitorRegion region = new ThreadMonitorRegion();

    @Override
    public void fuckIn(FlowContext flowContext) {
        flowContext.inOutStartTime();
    }

    @Override
    public void fuckOut(FlowContext flowContext) {
        ThreadExecutor threadExecutor = flowContext.option(FlowAttr.threadExecutor);
        if (Objects.nonNull(threadExecutor)) {
            region.update(flowContext.getInOutTime(), threadExecutor);
        }
    }

    /**
     * 业务线程监控插件 - 线程监控相关信息
     */
    @Getter
    public static class ThreadMonitorRegion {
        final Map<String, ThreadMonitor> map = new NonBlockingHashMap<>();

        private ThreadMonitor getStatThread(ThreadExecutor threadExecutor) {
            String name = threadExecutor.name();
            ThreadMonitor threadMonitor = this.map.get(name);

            // 无锁化
            if (Objects.isNull(threadMonitor)) {
                ThreadMonitor newValue = ThreadMonitor.create(name, threadExecutor);
                return MoreKit.putIfAbsent(this.map, name, newValue);
            }

            return threadMonitor;
        }

        void update(long time, ThreadExecutor threadExecutor) {
            this.getStatThread(threadExecutor).increment(time);
        }

        public void forEach(Consumer<ThreadMonitor> action) {
            this.map.values()
                    .stream()
                    .filter(ThreadMonitor::notEmpty)
                    .forEach(action);
        }

        @Override
        public String toString() {
            Map<String, ThreadMonitor> sortMap = new TreeMap<>(this.map);
            return sortMap.values().stream()
                    .filter(ThreadMonitor::notEmpty)
                    .map(ThreadMonitor::toString)
                    .collect(Collectors.joining("\n"));
        }
    }

    /**
     * 线程监控相关信息
     *
     * @param name         业务线程名
     * @param executeCount 线程已执行任务的次数
     * @param totalTime    执行任务的总耗时
     * @param executor     ThreadPoolExecutor
     */
    public record ThreadMonitor(String name, LongAdder executeCount, LongAdder totalTime, ThreadExecutor executor) {

        public static ThreadMonitor create(String name, ThreadExecutor executor) {
            return new ThreadMonitor(name, new LongAdder(), new LongAdder(), executor);
        }

        void increment(long time) {
            this.executeCount.increment();
            this.totalTime.add(time);
        }

        boolean notEmpty() {
            return this.executeCount.sum() > 0;
        }

        /**
         * 平均耗时
         *
         * @return 平均耗时
         */
        public long getAvgTime() {
            return this.totalTime.sum() / this.executeCount.sum();
        }

        /**
         * 当前剩余还没有执行的任务数
         *
         * @return 剩余还没有执行的任务数
         */
        public int countRemaining() {
            return Optional.ofNullable(this.executor)
                    .map(ThreadExecutor::getWorkQueue)
                    .orElse(0);
        }

        @Override
        public String toString() {
            return String.format("业务线程[%s] 共执行了 %s 次业务，平均耗时 %d ms, 剩余 %d 个任务未执行"
                    , this.name
                    , this.executeCount
                    , this.getAvgTime()
                    , this.countRemaining()
            );
        }
    }
}
