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
package com.iohao.game.common.kit;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.jctools.maps.NonBlockingHashMap;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 内部工具类，开发者不要用在耗时 io 的任务上
 * <pre>{@code
 *         // 执行一次
 *         InternalKit.newTimeoutSeconds(new TimerTask() {
 *             @Override
 *             public void run(Timeout timeout) {
 *                 log.info("one : {}", timeout);
 *             }
 *         });
 *
 *         // 3 秒后执行
 *         InternalKit.newTimeout(new TimerTask() {
 *             @Override
 *             public void run(Timeout timeout) {
 *                 log.info("3-newTimeout : {}", timeout);
 *             }
 *         }, 3, TimeUnit.SECONDS);
 * }
 * </pre>
 * <p>
 * 使用其他线程执行任务
 * <pre>{@code
 *         InternalKit.execute(()->{
 *             log.info("你的逻辑");
 *         });
 * }
 * </pre>
 * TimerListener 监听回调。内部使用 HashedWheelTimer 来模拟 ScheduledExecutorService 调度
 * <p>
 * example
 * <pre>{@code
 *     // 每秒钟调用一次 onUpdate 方法
 *     InternalKit.addSecondsTimerListener(new YourTimerListener());
 *     // 每分钟调用一次 onUpdate 方法
 *     InternalKit.addMinuteTimerListener(new YourTimerListener());
 *     // 每 10 秒钟调用一次 onUpdate 方法
 *     InternalKit.addTimerListener(new YourTimerListener(), 10, TimeUnit.SECONDS);
 *     // 每 30 分钟调用一次 onUpdate 方法
 *     InternalKit.addTimerListener(new YourTimerListener(), 30, TimeUnit.MINUTES);
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-06-30
 */
@UtilityClass
public class InternalKit {
    /** 时间精度为 1 秒钟，执行一些没有 io 操作的逻辑 */
    private final HashedWheelTimer wheelTimer = new HashedWheelTimer();
    @Getter
    private final ExecutorService executor = ExecutorKit.newCacheThreadPool("InternalKit");
    final Map<TickTimeUnit, List<TimerListener>> timerListenerMap = new NonBlockingHashMap<>();

    /**
     * 使用其他线程执行任务
     *
     * @param command 任务
     */
    public void execute(Runnable command) {
        executor.execute(command);
    }

    public void newTimeoutSeconds(TimerTask task) {
        wheelTimer.newTimeout(task, 0, TimeUnit.SECONDS);
    }

    public void newTimeout(TimerTask task, long delay, TimeUnit unit) {
        wheelTimer.newTimeout(task, delay, unit);
    }

    /**
     * 添加 TimerListener 监听回调
     * <pre>
     *     框架每分钟会调用一次 TimerListener
     * </pre>
     *
     * @param timerListener TimerListener 监听
     */
    public void addMinuteTimerListener(TimerListener timerListener) {
        addTimerListener(timerListener, 1, TimeUnit.MINUTES);
    }

    /**
     * 添加 TimerListener 监听回调
     * <pre>
     *     框架每秒钟会调用一次 TimerListener
     * </pre>
     *
     * @param timerListener TimerListener 监听
     */
    public void addSecondsTimerListener(TimerListener timerListener) {
        addTimerListener(timerListener, 1, TimeUnit.SECONDS);
    }

    /**
     * 添加 TimerListener 监听回调
     * <pre>
     *     使用 HashedWheelTimer 来模拟 ScheduledExecutorService 调度
     * </pre>
     *
     * @param timerListener TimerListener 监听
     * @param tick          tick 次数
     * @param timeUnit      tick 时间单位
     */
    public void addTimerListener(TimerListener timerListener, long tick, TimeUnit timeUnit) {
        TickTimeUnit tickTimeUnit = new TickTimeUnit(tick, timeUnit);

        List<TimerListener> timerListeners = timerListenerMap.get(tickTimeUnit);

        if (CollKit.isEmpty(timerListeners)) {
            timerListeners = timerListenerMap.putIfAbsent(tickTimeUnit, new CopyOnWriteArrayList<>());

            if (Objects.isNull(timerListeners)) {
                timerListeners = timerListenerMap.get(tickTimeUnit);

                foreverTimeout(tick, timeUnit, timerListeners);
            }
        }

        timerListeners.add(timerListener);
    }

    private void foreverTimeout(long tick, TimeUnit timeUnit, List<TimerListener> timerListeners) {

        // 启动定时器
        InternalKit.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) {

                if (timerListeners.isEmpty()) {
                    InternalKit.newTimeout(this, tick, timeUnit);
                    return;
                }

                timerListeners.forEach(timerListener -> {
                    if (timerListener.triggerUpdate()) {
                        var executor = timerListener.getExecutor();

                        // Timer 监听回调
                        if (Objects.nonNull(executor)) {
                            executor.execute(timerListener::onUpdate);
                        } else {
                            timerListener.onUpdate();
                        }
                    }

                    if (!timerListener.isActive()) {
                        timerListeners.remove(timerListener);
                    }

                });

                InternalKit.newTimeout(this, tick, timeUnit);
            }
        }, tick, timeUnit);
    }

    record TickTimeUnit(long tick, TimeUnit timeUnit) {
    }

    /**
     * Timer 监听回调
     * <p>
     * example
     * <pre>{@code
     *     // 每秒钟调用一次 onUpdate 方法
     *     InternalKit.addSecondsTimerListener(new YourTimerListener());
     *     // 每分钟调用一次 onUpdate 方法
     *     InternalKit.addMinuteTimerListener(new YourTimerListener());
     *     // 每 10 秒钟调用一次 onUpdate 方法
     *     InternalKit.addTimerListener(new YourTimerListener(), 10, TimeUnit.SECONDS);
     * }
     * </pre>
     */
    public interface TimerListener {
        /**
         * 是否触发 onUpdate 监听回调方法
         *
         * @return true 执行 onUpdate 方法
         */
        default boolean triggerUpdate() {
            return true;
        }

        /**
         * Timer 监听回调
         */
        void onUpdate();

        /**
         * 执行 onUpdate 的执行器
         * <pre>
         *     如果返回 null 将在 HashedWheelTimer 中执行。
         *
         *     如果有耗时的任务，比如涉及一些 io 操作的，建议指定执行器来执行当前回调（onUpdate 方法），以避免阻塞其他任务。
         * </pre>
         * 示例
         * <pre>{@code
         *     default Executor getExecutor() {
         *         // 耗时任务，指定一个执行器来消费当前 onUpdate
         *         return InternalKit.executor;
         *     }
         * }
         * </pre>
         *
         * @return 执行器
         */
        default Executor getExecutor() {
            return null;
        }

        /**
         * 是否活跃
         *
         * @return false 表示不活跃，会从监听列表中移除当前 TimerListener
         */
        default boolean isActive() {
            return true;
        }
    }
}

