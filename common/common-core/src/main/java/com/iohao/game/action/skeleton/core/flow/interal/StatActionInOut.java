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
import lombok.Getter;
import lombok.Setter;
import org.jctools.maps.NonBlockingHashMap;

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
    /** 统计值更新后调用 */
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
        StatAction statAction = this.region.update(flowContext, time);

        if (Objects.nonNull(this.listener)) {
            // 统计值更新后所执行的回调方法
            this.listener.changed(statAction, time, flowContext);
        }
    }

    /**
     * 统计域，管理所有 StatAction 统计记录
     */
    public static final class StatActionRegion {
        final Map<CmdInfo, StatAction> map = new NonBlockingHashMap<>();

        private StatAction update(FlowContext flowContext, long time) {
            CmdInfo cmdInfo = flowContext.getCmdInfo();
            StatAction statAction = getStatAction(cmdInfo);
            statAction.update(flowContext, time);
            return statAction;
        }

        public StatAction getStatAction(CmdInfo cmdInfo) {
            StatAction statAction = this.map.get(cmdInfo);

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

        public int size() {
            return this.map.size();
        }
    }

    /**
     * action 统计记录，与 action 是对应关系 1:1
     */
    public static final class StatAction {
        @Getter
        final CmdInfo cmdInfo;
        /** action 执行次数统计 */
        final LongAdder executeCount = new LongAdder();
        /** 总耗时 */
        final LongAdder totalTime = new LongAdder();
        /** action 异常触发次数 */
        final LongAdder errorCount = new LongAdder();
        /** 最大耗时 */
        @Getter
        volatile long maxTime;

        public StatAction(CmdInfo cmdInfo) {
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
            return "StatAction{" +
                    CmdKit.toString(this.cmdInfo.getCmdMerge()) +
                    ", 执行[" + this.executeCount + "]次" +
                    ", 总耗时[" + this.totalTime + "]" +
                    ", 平均耗时[" + this.getAvgTime() + "]" +
                    ", 最大耗时[" + this.maxTime + "]" +
                    ", 异常[" + this.getErrorCount() + "]次" +
                    '}';
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
         * @param time        action 调用耗时
         * @param flowContext flowContext
         */
        void changed(StatAction statAction, long time, FlowContext flowContext);
    }
}
