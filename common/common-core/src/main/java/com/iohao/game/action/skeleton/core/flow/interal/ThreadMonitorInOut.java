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
package com.iohao.game.action.skeleton.core.flow.interal;

import com.iohao.game.action.skeleton.core.flow.ActionMethodInOut;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.core.flow.attr.FlowAttr;
import com.iohao.game.action.skeleton.kit.ExecutorRegion;
import lombok.Getter;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

/**
 * 业务线程监控插件
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

        if (Objects.isNull(this.region.executorRegion)) {
            this.region.executorRegion = flowContext.option(FlowAttr.executorRegion);
        }
    }

    @Override
    public void fuckOut(FlowContext flowContext) {
        region.update(flowContext.getInOutTime());
    }

    @Getter
    public static class ThreadMonitorRegion {
        final Map<String, ThreadMonitor> map = new NonBlockingHashMap<>();
        ExecutorRegion executorRegion;

        ThreadMonitor getStatThread(String name) {
            ThreadMonitor threadMonitor = this.map.get(name);

            // 无锁化
            if (Objects.isNull(threadMonitor)) {
                ThreadPoolExecutor executor = Optional.ofNullable(this.executorRegion)
                        .map(region -> region.getThreadPoolExecutor(name))
                        .orElse(null);

                threadMonitor = this.map.putIfAbsent(name, ThreadMonitor.create(name, executor));

                if (Objects.isNull(threadMonitor)) {
                    threadMonitor = this.map.get(name);
                }
            }

            return threadMonitor;
        }

        void update(long time) {
            String name = Thread.currentThread().getName();
            this.getStatThread(name).increment(time);
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

    public record ThreadMonitor(String name, LongAdder executeCount, LongAdder totalTime, ThreadPoolExecutor executor) {

        public static ThreadMonitor create(String name, ThreadPoolExecutor executor) {
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
                    .map(threadPoolExecutor -> threadPoolExecutor.getQueue().size())
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
