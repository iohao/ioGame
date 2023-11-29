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
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 内部工具类，开发者不要用在耗时 io 的任务上
 * <pre>{@code
 *         // 每秒执行一次
 *         InternalKit.newTimeoutSeconds(new TimerTask() {
 *             @Override
 *             public void run(Timeout timeout) {
 *                 log.info("1-newTimeoutSeconds : {}", timeout);
 *                 InternalKit.newTimeoutSeconds(this);
 *             }
 *         });
 *
 *         // 只执行一次
 *         InternalKit.newTimeoutSeconds(new TimerTask() {
 *             @Override
 *             public void run(Timeout timeout) {
 *                 log.info("one : {}", timeout);
 *             }
 *         });
 * }
 * </pre>
 * <pre>{@code
 *         // 每隔 3 秒执行一次
 *         InternalKit.newTimeout(new TimerTask() {
 *             @Override
 *             public void run(Timeout timeout) {
 *                 log.info("3-newTimeout : {}", timeout);
 *                 InternalKit.newTimeout(this, 3, TimeUnit.SECONDS);
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
 * Timer 监听回调
 * <p>
 * example
 * <pre>{@code
 *     // 每分钟调用一次 onUpdate 方法
 *     InternalKit.addMinuteTimerListener(new YourTimerListener());
 *     // 每秒钟调用一次 onUpdate 方法
 *     InternalKit.addSecondsTimerListeners(new YourTimerListener());
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-06-30
 */
@UtilityClass
public class InternalKit {
    /** 时间精度为 1 秒钟，执行一些没有 io 操作的逻辑 */
    private final HashedWheelTimer timerSeconds = new HashedWheelTimer();
    private final ExecutorService executor = ExecutorKit.newCacheThreadPool("InternalKit");
    private final List<TimerListener> minuteTimerListeners = new CopyOnWriteArrayList<>();
    private final List<TimerListener> secondsTimerListeners = new CopyOnWriteArrayList<>();


    public void newTimeoutSeconds(TimerTask task) {
        timerSeconds.newTimeout(task, 0, TimeUnit.SECONDS);
    }

    public void newTimeout(TimerTask task, long delay, TimeUnit unit) {
        timerSeconds.newTimeout(task, delay, unit);
    }

    /**
     * 使用其他线程执行任务
     *
     * @param command 任务
     */
    public void execute(Runnable command) {
        executor.execute(command);
    }

    /**
     * 添加 TimerListener 监听
     * <pre>
     *     框架每分钟会调用一次 TimerListener
     * </pre>
     *
     * @param timerListener TimerListener 监听
     */
    public void addMinuteTimerListener(TimerListener timerListener) {
        enableMinuteTime();

        minuteTimerListeners.add(timerListener);
    }

    public void removeMinuteTimerListener(TimerListener timerListener) {
        minuteTimerListeners.remove(timerListener);
    }

    /**
     * 添加 TimerListener 监听
     * <pre>
     *     框架每秒钟会调用一次 TimerListener
     * </pre>
     *
     * @param timerListener TimerListener 监听
     */
    public void addSecondsTimerListeners(TimerListener timerListener) {
        enableSecondsTimer();

        secondsTimerListeners.add(timerListener);
    }

    public void removeSecondsTimerListeners(TimerListener timerListener) {
        secondsTimerListeners.remove(timerListener);
    }

    private final AtomicBoolean minuteTimeEnable = new AtomicBoolean();
    private final AtomicBoolean secondsTimerEnable = new AtomicBoolean();

    private void enableSecondsTimer() {
        if (secondsTimerEnable.get()) {
            return;
        }

        if (secondsTimerEnable.compareAndSet(false, true)) {
            InternalKit.newTimeout(new TimerTask() {
                @Override
                public void run(Timeout timeout) {
                    secondsTimerListeners.forEach(TimerListener::onUpdate);
                    InternalKit.newTimeout(this, 1, TimeUnit.SECONDS);
                }
            }, 1, TimeUnit.SECONDS);
        }
    }

    private void enableMinuteTime() {
        if (minuteTimeEnable.get()) {
            return;
        }

        if (minuteTimeEnable.compareAndSet(false, true)) {
            InternalKit.newTimeout(new TimerTask() {
                @Override
                public void run(Timeout timeout) {
                    minuteTimerListeners.forEach(TimerListener::onUpdate);
                    InternalKit.newTimeout(this, 1, TimeUnit.MINUTES);
                }
            }, 1, TimeUnit.MINUTES);
        }
    }

    private void enableUpdateCurrentTimeMillis() {
        TimeKit.UpdateCurrentTimeMillis update = new TimeKit.UpdateCurrentTimeMillis() {
            volatile long currentTimeMillis = System.currentTimeMillis();

            @Override
            public void init() {
                // 每秒更新一次时间
                InternalKit.newTimeoutSeconds(new TimerTask() {
                    @Override
                    public void run(Timeout timeout) {
                        currentTimeMillis = System.currentTimeMillis();
                        InternalKit.newTimeoutSeconds(this);
                    }
                });
            }

            @Override
            public long getCurrentTimeMillis() {
                return currentTimeMillis;
            }
        };

        TimeKit.setUpdateCurrentTimeMillis(update);
    }

    /**
     * Timer 监听回调
     * <p>
     * example
     * <pre>{@code
     *     // 每分钟调用一次 onUpdate 方法
     *     InternalKit.addMinuteTimerListener(new YourTimerListener());
     *     // 每秒钟调用一次 onUpdate 方法
     *     InternalKit.addSecondsTimerListeners(new YourTimerListener());
     * }
     * </pre>
     */
    public interface TimerListener {
        /**
         * Timer 监听回调
         */
        void onUpdate();
    }
}

