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
package com.iohao.game.common.kit.concurrent;

import com.iohao.game.common.kit.CollKit;
import com.iohao.game.common.kit.ExecutorKit;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.jctools.maps.NonBlockingHashMap;
import org.jctools.maps.NonBlockingHashSet;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 任务消费相关的内部工具类，开发者不要用在耗时 io 的任务上
 *
 * <pre>{@code
 *         // 执行一次
 *         TaskKit.newTimeoutSeconds(new TimerTask() {
 *             @Override
 *             public void run(Timeout timeout) {
 *                 log.info("one : {}", timeout);
 *             }
 *         });
 *
 *         // 3 秒后执行
 *         TaskKit.newTimeout(new TimerTask() {
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
 *         TaskKit.execute(()->{
 *             log.info("你的逻辑");
 *         });
 * }
 * </pre>
 * TimerListener 监听回调。内部使用 HashedWheelTimer 来模拟 ScheduledExecutorService 调度
 * <p>
 * example
 * <pre>{@code
 *     // 每秒钟调用一次 onUpdate 方法
 *     TaskKit.addSecondsTimerListener(new YourTimerListener());
 *     // 每分钟调用一次 onUpdate 方法
 *     TaskKit.addMinuteTimerListener(new YourTimerListener());
 *     // 每 10 秒钟调用一次 onUpdate 方法
 *     TaskKit.addTimerListener(new YourTimerListener(), 10, TimeUnit.SECONDS);
 *     // 每 30 分钟调用一次 onUpdate 方法
 *     TaskKit.addTimerListener(new YourTimerListener(), 30, TimeUnit.MINUTES);
 * }
 * </pre>
 * example - TimerListener - 高级用法
 * <pre>{@code
 *         //【示例 - 移除任务】每秒调用一次，当 hp 为 0 时就移除当前 TimerListener
 *         TaskKit.addTimerListener(new TimerListener() {
 *             int hp = 2;
 *
 *             @Override
 *             public void onUpdate() {
 *                 hp--;
 *                 log.info("剩余 hp:2-{}", hp);
 *             }
 *
 *             @Override
 *             public boolean isActive() {
 *                 // 当返回 false 则表示不活跃，会从监听列表中移除当前 TimerListener
 *                 return hp != 0;
 *             }
 *         }, 1, TimeUnit.SECONDS);
 *
 *
 *         //【示例 - 跳过执行】每秒调用一次，当 triggerUpdate 返回值为 true，即符合条件时才执行 onUpdate 方法
 *         TaskKit.addTimerListener(new TimerListener() {
 *             int hp;
 *
 *             @Override
 *             public void onUpdate() {
 *                 log.info("current hp:{}", hp);
 *             }
 *
 *             @Override
 *             public boolean triggerUpdate() {
 *                 hp++;
 *                 // 当返回值为 true 时，会执行 onUpdate 方法
 *                 return hp % 2 == 0;
 *             }
 *         }, 1, TimeUnit.SECONDS);
 *
 *         //【示例 - 指定线程执行器】每秒调用一次
 *         // 如果有耗时的任务，比如涉及一些 io 操作的，建议指定执行器来执行当前回调（onUpdate 方法），以避免阻塞其他任务。
 *         TaskKit.addTimerListener(new TimerListener() {
 *             @Override
 *             public void onUpdate() {
 *                 log.info("执行耗时的 IO 任务，开始");
 *
 *                 try {
 *                     TimeUnit.SECONDS.sleep(3);
 *                 } catch (InterruptedException e) {
 *                     throw new RuntimeException(e);
 *                 }
 *
 *                 log.info("执行耗时的 IO 任务，结束");
 *             }
 *
 *             @Override
 *             public Executor getExecutor() {
 *                 // 指定执行器来执行当前回调（onUpdate 方法），以避免阻塞其他任务。
 *                 return TaskKit.getCacheExecutor();
 *             }
 *         }, 1, TimeUnit.SECONDS);
 * }
 *
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
    final ExecutorService cacheExecutor = ExecutorKit.newCacheThreadPool("ioGameTaskKit");
    final Map<TickTimeUnit, Set<TimerListener>> timerListenerMap = new NonBlockingHashMap<>();

    /**
     * 使用其他线程执行任务
     *
     * @param command 任务
     */
    public void execute(Runnable command) {
        cacheExecutor.execute(command);
    }

    /**
     * 执行任务
     *
     * @param task 任务
     */
    public void newTimeoutSeconds(TimerTask task) {
        wheelTimer.newTimeout(task, 0, TimeUnit.SECONDS);
    }

    /**
     * 延迟一定时间后执行任务；
     *
     * @param task  任务
     * @param delay 延迟时间
     * @param unit  延迟时间单位
     */
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

        Set<TimerListener> timerListeners = timerListenerMap.get(tickTimeUnit);

        if (CollKit.isEmpty(timerListeners)) {
            timerListeners = timerListenerMap.putIfAbsent(tickTimeUnit, new NonBlockingHashSet<>());

            if (Objects.isNull(timerListeners)) {
                timerListeners = timerListenerMap.get(tickTimeUnit);

                foreverTimeout(tick, timeUnit, timerListeners);
            }
        }

        timerListeners.add(timerListener);
    }

    private void foreverTimeout(long tick, TimeUnit timeUnit, Set<TimerListener> timerListeners) {

        // 启动定时器
        TaskKit.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) {

                if (timerListeners.isEmpty()) {
                    TaskKit.newTimeout(this, tick, timeUnit);
                    return;
                }

                timerListeners.forEach(timerListener -> {
                    var executor = timerListener.getExecutor();

                    // 如果指定了执行器，就将执行流程放到执行器中，否则使用当前线程
                    if (Objects.nonNull(executor)) {
                        executor.execute(() -> flowTimerListener(timerListener, timerListeners));
                    } else {
                        flowTimerListener(timerListener, timerListeners);
                    }

                });

                TaskKit.newTimeout(this, tick, timeUnit);
            }
        }, tick, timeUnit);
    }

    private void flowTimerListener(TimerListener timerListener, Set<TimerListener> timerListeners) {

        if (timerListener.triggerUpdate()) {
            timerListener.onUpdate();
        }

        // 移除不活跃的监听
        if (!timerListener.isActive()) {
            timerListeners.remove(timerListener);
        }
    }

    record TickTimeUnit(long tick, TimeUnit timeUnit) {
    }
}
