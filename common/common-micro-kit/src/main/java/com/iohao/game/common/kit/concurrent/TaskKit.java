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
package com.iohao.game.common.kit.concurrent;

import com.iohao.game.common.kit.CollKit;
import com.iohao.game.common.kit.ExecutorKit;
import com.iohao.game.common.kit.collect.SetMultiMap;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * 任务消费相关的内部工具类，开发者不要用在耗时 io 的任务上
 * <p>
 * example - 使用其他线程执行任务
 * <pre>{@code
 *     TaskKit.execute(()->{
 *         log.info("你的逻辑");
 *     });
 * }
 * </pre>
 * example - netty TimerTask
 * <pre>{@code
 *     // 3 秒后执行
 *     TaskKit.newTimeout(new TimerTask() {
 *         @Override
 *         public void run(Timeout timeout) {
 *             log.info("3-newTimeout : {}", timeout);
 *         }
 *     }, 3, TimeUnit.SECONDS);
 * }
 * </pre>
 * example - TaskListener 监听回调。内部使用 HashedWheelTimer 来模拟 ScheduledExecutorService 调度
 * <pre>{@code
 *     // 只执行一次，2 秒后执行
 *     TaskKit.runOnce(() -> log.info("2 Seconds"), 2, TimeUnit.SECONDS);
 *     // 只执行一次，1 分钟后执行
 *     TaskKit.runOnce(() -> log.info("1 Minute"), 1, TimeUnit.MINUTES)
 *     // 只执行一次，500、800 milliseconds 后
 *     TaskKit.runOnce(() -> log.info("500 delayMilliseconds"), 500);
 *     TaskKit.runOnce(() -> log.info("800 delayMilliseconds"), 800);
 *
 *     // 每分钟调用一次
 *     TaskKit.runIntervalMinute(() -> log.info("tick 1 Minute"), 1);
 *     // 每 2 分钟调用一次
 *     TaskKit.runIntervalMinute(() -> log.info("tick 2 Minute"), 2);
 *
 *     // 每 2 秒调用一次
 *     TaskKit.runInterval(() -> log.info("tick 2 Seconds"), 2, TimeUnit.SECONDS);
 *     // 每 30 分钟调用一次
 *     TaskKit.runInterval(() -> log.info("tick 30 Minute"), 30, TimeUnit.MINUTES);
 * }
 * </pre>
 * example - TaskListener - 高级用法
 * <pre>{@code
 *      //【示例 - 移除任务】每秒调用一次，当 hp 为 0 时就移除当前 Listener
 *     TaskKit.runInterval(new IntervalTaskListener() {
 *         int hp = 2;
 *
 *         @Override
 *         public void onUpdate() {
 *             hp--;
 *             log.info("剩余 hp:2-{}", hp);
 *         }
 *
 *         @Override
 *         public boolean isActive() {
 *             // 当返回 false 则表示不活跃，会从监听列表中移除当前 Listener
 *             return hp != 0;
 *         }
 *     }, 1, TimeUnit.SECONDS);
 *
 *     //【示例 - 跳过执行】每秒调用一次，当 triggerUpdate 返回值为 true，即符合条件时才执行 onUpdate 方法
 *     TaskKit.runInterval(new IntervalTaskListener() {
 *         int hp;
 *
 *         @Override
 *         public void onUpdate() {
 *             log.info("current hp:{}", hp);
 *         }
 *
 *         @Override
 *         public boolean triggerUpdate() {
 *             hp++;
 *             // 当返回值为 true 时，会执行 onUpdate 方法
 *             return hp % 2 == 0;
 *         }
 *     }, 1, TimeUnit.SECONDS);
 *
 *     //【示例 - 指定线程执行器】每秒调用一次
 *     // 如果有耗时的任务，比如涉及一些 io 操作的，建议指定执行器来执行当前回调（onUpdate 方法），以避免阻塞其他任务。
 *     TaskKit.runInterval(new IntervalTaskListener() {
 *         @Override
 *         public void onUpdate() {
 *             log.info("执行耗时的 IO 任务，开始");
 *
 *             try {
 *                 TimeUnit.SECONDS.sleep(3);
 *             } catch (InterruptedException e) {
 *                 throw new RuntimeException(e);
 *             }
 *
 *             log.info("执行耗时的 IO 任务，结束");
 *         }
 *
 *         @Override
 *         public Executor getExecutor() {
 *             // 指定执行器来执行当前回调（onUpdate 方法），以避免阻塞其他任务。
 *             return TaskKit.getCacheExecutor();
 *         }
 *     }, 1, TimeUnit.SECONDS);
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-12-02
 */
@UtilityClass
public class TaskKit {
    /** 执行一些没有 io 操作的逻辑 */
    private final HashedWheelTimer wheelTimer = new HashedWheelTimer();
    /** 内置的 cacheExecutor 执行器 */
    @Getter
    final ExecutorService cacheExecutor = ExecutorKit.newCacheThreadPool("ioGameThread-");
    /** 虚拟线程执行器 */
    @Getter
    final ExecutorService virtualExecutor = ExecutorKit.newVirtualExecutor("ioGameVirtual-");
    final SetMultiMap<TickTimeUnit, IntervalTaskListener> intervalTaskListenerMap = SetMultiMap.of();

    record TickTimeUnit(long tick, TimeUnit timeUnit) {
    }

    /**
     * 使用其他线程执行任务
     *
     * @param command 任务
     */
    public void execute(Runnable command) {
        cacheExecutor.execute(command);
    }

    /**
     * 使用虚拟线程执行任务
     *
     * @param command 任务
     */
    public void executeVirtual(Runnable command) {
        virtualExecutor.execute(command);
    }

    /**
     * 返回一个 CompletableFuture，该任务会在 virtualExecutor（虚拟线程） 中异步运行，结果将从 Supplier 中获得
     *
     * @param supplier supplier
     * @param <U>      u
     * @return CompletableFuture
     * @see TaskKit#virtualExecutor
     */
    public <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
        return CompletableFuture.supplyAsync(supplier, virtualExecutor);
    }

    /**
     * 延迟一定时间后执行任务；
     *
     * @param task  任务
     * @param delay 延迟时间
     * @param unit  延迟时间单位
     * @return Timeout
     */
    public Timeout newTimeout(TimerTask task, long delay, TimeUnit unit) {
        return wheelTimer.newTimeout(task, delay, unit);
    }

    /**
     * 添加 OnceTaskListener 监听回调，只会执行一次
     *
     * @param taskListener taskListener
     * @param delay        延迟时间
     * @param unit         延迟时间单位
     */
    public void runOnce(OnceTaskListener taskListener, long delay, TimeUnit unit) {
        newTimeout(taskListener, delay, unit);
    }

    /**
     * 一秒后执行 OnceTaskListener 监听回调，只会执行一次
     *
     * @param taskListener taskListener
     */
    public void runOnceSecond(OnceTaskListener taskListener) {
        runOnce(taskListener, 1, TimeUnit.SECONDS);
    }

    /**
     * 添加 OnceTaskListener 监听回调，只会执行一次
     *
     * @param taskListener      taskListener
     * @param delayMilliseconds delayMilliseconds
     */
    public void runOnceMillis(OnceTaskListener taskListener, long delayMilliseconds) {
        runOnce(taskListener, delayMilliseconds, TimeUnit.MILLISECONDS);
    }

    /**
     * 添加调度任务监听
     *
     * @param taskListener 调度任务监听
     * @param tickMinute   每 tickMinute 分钟，会调用一次监听
     */
    public void runIntervalMinute(IntervalTaskListener taskListener, long tickMinute) {
        runInterval(taskListener, tickMinute, TimeUnit.MINUTES);
    }

    /**
     * 添加任务监听回调
     * <pre>
     *     每 tick 时间单位，会调用一次任务监听
     * </pre>
     *
     * @param taskListener 任务监听
     * @param tick         tick 时间间隔；每 tick 时间间隔，会调用一次监听
     * @param timeUnit     tick 时间单位
     */
    public void runInterval(IntervalTaskListener taskListener, long tick, TimeUnit timeUnit) {
        TickTimeUnit tickTimeUnit = new TickTimeUnit(tick, timeUnit);

        Set<IntervalTaskListener> intervalTaskListeners = intervalTaskListenerMap.get(tickTimeUnit);

        // 无锁化
        if (CollKit.isEmpty(intervalTaskListeners)) {
            intervalTaskListeners = intervalTaskListenerMap.ofIfAbsent(tickTimeUnit, (initSet) -> {
                // 使用 HashedWheelTimer 来模拟 ScheduledExecutorService 调度
                foreverTimerTask(tick, timeUnit, initSet);
            });
        }

        intervalTaskListeners.add(taskListener);
    }

    private void foreverTimerTask(long tick, TimeUnit timeUnit, Set<IntervalTaskListener> set) {

        // 启动定时器
        TaskKit.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) {

                if (set.isEmpty()) {
                    TaskKit.newTimeout(this, tick, timeUnit);
                    return;
                }

                set.forEach(intervalTaskListener -> {
                    var executor = intervalTaskListener.getExecutor();

                    // 如果指定了执行器，就将执行流程放到执行器中，否则使用当前线程
                    if (Objects.nonNull(executor)) {
                        executor.execute(() -> executeFlowTimerListener(intervalTaskListener, set));
                    } else {
                        executeFlowTimerListener(intervalTaskListener, set);
                    }
                });

                TaskKit.newTimeout(this, tick, timeUnit);
            }
        }, tick, timeUnit);
    }

    private void executeFlowTimerListener(IntervalTaskListener taskListener, Set<IntervalTaskListener> set) {
        try {
            // 移除不活跃的监听
            if (!taskListener.isActive()) {
                set.remove(taskListener);
                return;
            }

            if (taskListener.triggerUpdate()) {
                taskListener.onUpdate();
            }
        } catch (Throwable e) {
            taskListener.onException(e);
        }
    }

    public void stop() {
        wheelTimer.stop();
        cacheExecutor.shutdown();
        virtualExecutor.shutdown();
    }
}