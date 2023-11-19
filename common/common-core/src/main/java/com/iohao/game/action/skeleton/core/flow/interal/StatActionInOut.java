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

import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.core.CmdKit;
import com.iohao.game.action.skeleton.core.flow.ActionMethodInOut;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.common.kit.CollKit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jctools.maps.NonBlockingHashMap;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

/**
 * 业务框架 action 调用统计插件
 * <pre>
 *     <a href="https://www.yuque.com/iohao/game/znapzm1dqgehdyw8">action 调用统计插件-文档</a>
 *
 *     StatActionInOut 是 action 调用统计插件，可以用来统计各 action 调用时的相关数据，
 *     如 action 的执行次数、总耗时、平均耗时、最大耗时、触发异常次数...等相关数据
 *     开发者可以通过这些数据来分析出项目中的热点方法、耗时方法，从而做到精准优化。
 *
 *     // StatAction 统计记录打印预览
 *     "StatAction{cmd[1 - 0], 执行[1]次, 总耗时[8], 平均耗时[8], 最大耗时[8], 异常[0]次}"
 * </pre>
 * <p>
 * 使用示例
 * <pre>{@code
 *         BarSkeletonBuilder builder = ...;
 *         // action 调用统计插件，将插件添加到业务框架中
 *         var statActionInOut = new StatActionInOut();
 *         builder.addInOut(statActionInOut);
 *
 *         // 设置 StatAction 统计记录更新后的监听处理
 *         statActionInOut.setListener((statAction, time, flowContext) -> {
 *             // 简单打印统计记录值 StatAction
 *             System.out.println(statAction);
 *         });
 *
 *         // 统计域（统计值的管理器）
 *         StatActionInOut.StatActionRegion region = statActionInOut.getRegion();
 *
 *         // 遍历所有的统计数据
 *         region.forEach((cmdInfo, statAction) -> {
 *             // 简单打印统计记录值 StatAction
 *             System.out.println(statAction);
 *             // 开发者可以定时的将这些数据保存到日志或 DB 中，用于后续的分析
 *         });
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-11-17
 * @see StatAction
 * @see StatActionRegion
 * @see StatActionChangeListener
 */
public final class StatActionInOut implements ActionMethodInOut {
    /** 统计域（管理 StatAction ） */
    @Getter
    final StatActionRegion region = new StatActionRegion();
    /** 统计值更新监听 */
    @Setter
    StatActionChangeListener listener;

    @Override
    public void fuckIn(FlowContext flowContext) {
        // 记录当前时间
        flowContext.inOutStartTime();
    }

    @Override
    public void fuckOut(FlowContext flowContext) {
        long time = flowContext.getInOutTime();

        // StatAction 与 action 是对应关系， 1:1
        this.region.update(time, flowContext);
    }

    /** 统计域，管理所有 StatAction 统计记录 */
    public final class StatActionRegion {
        final Map<CmdInfo, StatAction> map = new NonBlockingHashMap<>();

        void update(long time, FlowContext flowContext) {
            CmdInfo cmdInfo = flowContext.getCmdInfo();
            StatAction statAction = getStatAction(cmdInfo);
            statAction.update(flowContext, time);

            // 统计值更新后所执行的回调方法
            if (Objects.nonNull(StatActionInOut.this.listener)) {
                StatActionInOut.this.listener.flow(statAction, time, flowContext);
            }
        }

        public StatAction getStatAction(CmdInfo cmdInfo) {
            StatAction statAction = this.map.get(cmdInfo);

            // 无锁化
            if (Objects.isNull(statAction)) {
                statAction = this.map.putIfAbsent(cmdInfo, new StatAction(cmdInfo));
                if (Objects.isNull(statAction)) {
                    statAction = this.map.get(cmdInfo);
                }
            }

            return statAction;
        }

        public void forEach(BiConsumer<CmdInfo, StatAction> action) {
            this.map.forEach(action);
        }

        public Stream<StatAction> stream() {
            return this.map.values().stream();
        }
    }

    /** action 统计记录，与 action 是对应关系 1:1 */
    @Getter
    public final class StatAction {
        static final List<TimeRange> emptyRangeList = List.of(TimeRange.create(Long.MAX_VALUE - 1, Long.MAX_VALUE, ""));
        /** 时间范围 */
        final List<TimeRange> timeRangeList;
        @Getter(AccessLevel.PRIVATE)
        final TimeRange lastTimeRange;

        final CmdInfo cmdInfo;
        /** action 执行次数统计 */
        final LongAdder executeCount = new LongAdder();
        /** 总耗时 */
        final LongAdder totalTime = new LongAdder();
        /** action 异常触发次数 */
        final LongAdder errorCount = new LongAdder();
        /** 最大耗时 */
        volatile long maxTime;

        private StatAction(CmdInfo cmdInfo) {

            this.timeRangeList = Objects.isNull(StatActionInOut.this.listener)
                    ? emptyRangeList
                    : StatActionInOut.this.listener.createTimeRangeList();

            if (CollKit.isEmpty(this.timeRangeList)) {
                throw new IllegalArgumentException("this.timeRangeList is empty");
            }

            this.cmdInfo = cmdInfo;
            this.lastTimeRange = this.timeRangeList.get(this.timeRangeList.size() - 1);
        }

        private void update(FlowContext flowContext, long time) {
            // 调用次数 +1
            this.executeCount.increment();

            if (flowContext.isError()) {
                this.errorCount.increment();
            }

            if (time == 0) {
                return;
            }

            // 总耗时增加
            this.totalTime.add(time);

            // 记录最大耗时
            if (time > maxTime) {
                this.maxTime = time;
            }
        }

        /**
         * 根据消耗时间获取对应的时间范围对象；
         * <pre>
         *     如果没有找到对应的时间范围，取配置 List 中的最后一个元素。
         * </pre>
         *
         * @param time 消耗时间
         * @return 时间范围
         */
        public TimeRange getTimeRange(long time) {
            return this.timeRangeList.stream()
                    .filter(timeRange -> timeRange.inRange(time))
                    .findFirst()
                    .orElse(this.lastTimeRange);
        }

        /**
         * 平均耗时
         *
         * @return 平均耗时
         */
        public long getAvgTime() {
            return this.totalTime.sum() / this.executeCount.sum();
        }

        @Override
        public String toString() {
            String rangeStr = "";
            if (Objects.nonNull(StatActionInOut.this.listener)) {
                var builder = new StringBuilder();
                for (TimeRange timeRange : this.timeRangeList) {
                    if (timeRange.count.sum() == 0) {
                        continue;
                    }

                    builder.append("\n\t").append(timeRange);
                }

                rangeStr = builder.toString();
            }

            return String.format("StatAction{%s, 执行[%s]次, 异常[%s]次, 平均耗时[%d], 最大耗时[%s], 总耗时[%s] %s"
                    , CmdKit.toString(this.cmdInfo.getCmdMerge())
                    , this.executeCount
                    , this.errorCount
                    , this.getAvgTime()
                    , this.maxTime
                    , this.totalTime
                    , rangeStr
            );
        }
    }

    /**
     * StatAction 更新监听
     */
    public interface StatActionChangeListener {
        /**
         * StatAction 统计记录更新后调用
         *
         * @param statAction  action 统计记录
         * @param time        action 执行耗时
         * @param flowContext flowContext
         */
        void changed(StatAction statAction, long time, FlowContext flowContext);

        /**
         * 创建时间范围；如果想将统计分得更细，只需要创建更多的时间范围。
         * <p>
         * 参考示例
         * <pre>{@code
         *      List.of(
         *          TimeRange.create(500, 1000),
         *          TimeRange.create(1000, 1500),
         *          TimeRange.create(1500, 2000),
         *          TimeRange.create(2000, Long.MAX_VALUE, "> 2000"))
         * }
         * </pre>
         *
         * @return 时间范围，List 必须非空
         */
        default List<TimeRange> createTimeRangeList() {
            return List.of(
                    TimeRange.create(500, 1000),
                    TimeRange.create(1000, 1500),
                    TimeRange.create(1500, 2000),
                    TimeRange.create(2000, Long.MAX_VALUE, "> 2000"));
        }

        /**
         * 触发条件，触发 updateTimeRange 方法的前置条件
         * <pre>
         *     开发者可以通常此方法来决定是否触发 updateTimeRange 方法。
         *
         *     比如可以在该方法内判断，只对某个或某些玩家来做监控。
         * </pre>
         *
         * @param statAction  action 统计记录
         * @param time        action 执行耗时
         * @param flowContext flowContext
         * @return true 表示满足条件；当为 true 时，会调用 updateTimeRange 方法
         */
        default boolean triggerUpdateTimeRange(StatAction statAction, long time, FlowContext flowContext) {
            return time == Long.MAX_VALUE;
        }

        /**
         * StatAction 统计记录更新中调用，当 trigger 方法为 true 时会调用
         *
         * @param statAction  action 统计记录
         * @param time        action 执行耗时
         * @param flowContext flowContext
         */
        default void updateTimeRange(StatAction statAction, long time, FlowContext flowContext) {
            statAction.getTimeRange(time).increment();
        }

        /**
         * StatAction 更新监听流程
         * <pre>
         *     默认实现的流程为
         *     1 先判断 triggerUpdateTimeRange 是否满足条件
         *     2 当 triggerUpdateTimeRange 为 true 时，会执行 updateTimeRange
         *     3 无论如何 changed 总是会被执行的
         * </pre>
         *
         * @param statAction  action 统计记录
         * @param time        action 执行耗时
         * @param flowContext flowContext
         */
        default void flow(StatAction statAction, long time, FlowContext flowContext) {
            if (this.triggerUpdateTimeRange(statAction, time, flowContext)) {
                this.updateTimeRange(statAction, time, flowContext);
            }

            this.changed(statAction, time, flowContext);
        }
    }

    /**
     * 时间范围记录
     *
     * @param start 开始时间
     * @param end   结束时间
     * @param count 该时间范围所触发的执行次数
     * @param name  name
     */
    public record TimeRange(long start, long end, LongAdder count, String name) {
        /**
         * 创建时间范围
         *
         * @param start 开始时间
         * @param end   结束时间
         * @return TimeRange
         */
        public static TimeRange create(long start, long end) {
            return create(start, end, start + " ~ " + end);
        }

        /**
         * 创建时间范围，并指定名称
         *
         * @param start 开始时间
         * @param end   结束时间
         * @param name  打印时的名称
         * @return TimeRange
         */
        public static TimeRange create(long start, long end, String name) {
            return new TimeRange(start, end, new LongAdder(), name);
        }

        boolean inRange(long time) {
            return time >= this.start && time <= this.end;
        }

        /**
         * 统计 + 1
         */
        void increment() {
            this.count.increment();
        }

        @Override
        public String toString() {
            return String.format("%s ms 的请求共 [%d] 个", this.name, this.count.sum());
        }
    }
}
