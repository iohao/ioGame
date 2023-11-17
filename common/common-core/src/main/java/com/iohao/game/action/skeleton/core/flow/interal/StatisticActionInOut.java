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
import com.iohao.game.action.skeleton.core.flow.attr.FlowAttr;
import com.iohao.game.action.skeleton.core.flow.attr.FlowOption;
import lombok.Getter;
import lombok.Setter;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

/**
 * 业务框架 action 统计插件
 * <pre>
 *     <a href="https://www.yuque.com/iohao/game/znapzm1dqgehdyw8">action 统计插件-文档</a>
 *
 *     StatisticActionInOut 是 action 统计插件，可以用来统计各 action 业务方法耗时相关数据;
 *     如 action 的执行次数、总耗时、平均耗时、最大耗时等；
 *     开发者可以通过这些数据来分析出项目中的热点方法、耗时方法，从而做到精准优化。
 *
 *     // 统计打印预览
 *     "StatRecord{cmd[1 - 0], 执行[1]次, 总耗时[8], 平均耗时[8], 最大耗时[8], 异常[0]次}"
 * </pre>
 * <p>
 * 使用示例
 * <pre>{@code
 *        BarSkeletonBuilder builder = ...;
 *        StatisticActionInOut statisticActionInOut = new StatisticActionInOut();
 *         // 设置统计值更新后所执行的回调处理
 *         statisticActionInOut.setStatCallback((record, time, flowContext) -> {
 *             // 简单打印
 *             System.out.println(record);
 *         });
 *
 *         // 将插件添加到业务框架中
 *         builder.addInOut(statisticActionInOut);
 *
 *         // 统计域
 *         StatisticActionInOut.StatRegion statRegion = statisticActionInOut.getStatRegion();
 *         // 遍历所有的统计数据
 *         statRegion.forEach((cmdInfo, record) -> {
 *             // 简单打印
 *             System.out.println(record);
 *             // 开发者可以定时的将这些数据保存到日志或 DB 中
 *         });
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-11-17
 * @see StatRecord
 * @see StatRegion
 * @see StatCallback
 */
public final class StatisticActionInOut implements ActionMethodInOut {
    final FlowOption<Long> inOutStartTime = FlowAttr.inOutStartTime;
    /** 统计域（管理器） */
    @Getter
    final StatRegion statRegion = new StatRegion();
    /** 单次完成回调，统计值更新后所执行的回调方法 */
    @Setter
    StatCallback statCallback;

    @Override
    public void fuckIn(FlowContext flowContext) {
        // 记录当前时间
        if (!flowContext.hasOption(inOutStartTime)) {
            flowContext.option(inOutStartTime, System.currentTimeMillis());
        }
    }

    @Override
    public void fuckOut(FlowContext flowContext) {
        long startTime = flowContext.option(inOutStartTime);
        long time = System.currentTimeMillis() - startTime;
        StatRecord statRecord = this.statRegion.update(flowContext, time);

        if (Objects.nonNull(statCallback)) {
            // 统计值更新后所执行的回调方法
            statCallback.accept(statRecord, time, flowContext);
        }
    }

    /**
     * 统计域，管理所有统计记录
     */
    public static class StatRegion {
        Map<CmdInfo, StatRecord> map = new NonBlockingHashMap<>();

        private StatRecord update(FlowContext flowContext, long time) {
            CmdInfo cmdInfo = flowContext.getCmdInfo();
            StatRecord statRecord = getStatRecord(cmdInfo);
            statRecord.update(flowContext, time);
            return statRecord;
        }

        public StatRecord getStatRecord(CmdInfo cmdInfo) {
            StatRecord statRecord = this.map.get(cmdInfo);

            if (Objects.isNull(statRecord)) {
                statRecord = this.map.putIfAbsent(cmdInfo, new StatRecord(cmdInfo));
                if (Objects.isNull(statRecord)) {
                    statRecord = this.map.get(cmdInfo);
                }
            }

            return statRecord;
        }

        public void forEach(BiConsumer<CmdInfo, StatRecord> action) {
            this.map.forEach(action);
        }

        public Stream<StatRecord> stream() {
            return this.map.values().stream();
        }

        public int size() {
            return this.map.size();
        }
    }

    /**
     * 统计记录，与 action 是对应关系 1:1
     */
    public static class StatRecord {
        @Getter
        final CmdInfo cmdInfo;
        /** action 执行次数统计 */
        final LongAdder executeCount = new LongAdder();
        /** 总耗时 */
        final LongAdder totalTime = new LongAdder();
        /** action 异常次数 */
        final LongAdder errorCount = new LongAdder();
        /** 最大耗时 */
        @Getter
        volatile long maxTime;

        public StatRecord(CmdInfo cmdInfo) {
            this.cmdInfo = cmdInfo;
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
         * 平均耗时
         *
         * @return 平均耗时
         */
        public long getAvgTime() {
            return this.getTotalTime() / this.getExecuteCount();
        }


        public long getExecuteCount() {
            return this.executeCount.sum();
        }

        public long getTotalTime() {
            return this.totalTime.sum();
        }

        public long getErrorCount() {
            return this.errorCount.sum();
        }

        @Override
        public String toString() {
            return "StatRecord{" +
                    CmdKit.toString(this.cmdInfo.getCmdMerge()) +
                    ", 执行[" + this.executeCount + "]次" +
                    ", 总耗时[" + this.totalTime + "]" +
                    ", 平均耗时[" + this.getAvgTime() + "]" +
                    ", 最大耗时[" + this.maxTime + "]" +
                    ", 异常[" + this.errorCount + "]次" +
                    '}';
        }
    }

    /**
     * 记录值更新回调
     */
    public interface StatCallback {
        /**
         * 回调方法
         * <pre>单次完成回调，统计值更新后所执行的回调方法</pre>
         *
         * @param record      record
         * @param time        action 调用耗时
         * @param flowContext flowContext
         */
        void accept(StatRecord record, long time, FlowContext flowContext);
    }
}
