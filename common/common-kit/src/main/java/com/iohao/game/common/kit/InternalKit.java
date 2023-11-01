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
import io.netty.util.TimerTask;
import lombok.experimental.UtilityClass;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 内部工具类，开发者不要用在耗时 io 的任务上
 *
 * @author 渔民小镇
 * @date 2023-06-30
 */
@UtilityClass
public class InternalKit {
    /** 时间精度为 1 秒钟，执行一些没有 io 操作的逻辑 */
    private final HashedWheelTimer timerSeconds = new HashedWheelTimer(1, TimeUnit.SECONDS);
    private final ExecutorService executor = ExecutorKit.newCacheThreadPool("InternalKit");

    /**
     * example
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
     *
     * @param task task
     */
    public void newTimeoutSeconds(TimerTask task) {
        timerSeconds.newTimeout(task, 0, TimeUnit.SECONDS);
    }

    /**
     * example
     * <pre>{@code
     *         // 每隔 3 秒执行一次
     *         InternalKit.newTimeout(new TimerTask() {
     *             @Override
     *             public void run(Timeout timeout) {
     *                 log.info("3-newTimeout : {}", timeout);
     *                 InternalKit.newTimeout(this, 3, TimeUnit.SECONDS);
     *             }
     *         }, 3, TimeUnit.SECONDS);
     *
     * }
     * </pre>
     *
     * @param task  task
     * @param delay delay
     * @param unit  unit
     */
    public void newTimeout(TimerTask task, long delay, TimeUnit unit) {
        timerSeconds.newTimeout(task, delay, unit);
    }

    public void execute(Runnable command) {
        executor.execute(command);
    }
}
