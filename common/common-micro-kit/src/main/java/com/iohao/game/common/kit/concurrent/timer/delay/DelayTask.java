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
package com.iohao.game.common.kit.concurrent.timer.delay;

import com.iohao.game.common.kit.concurrent.TaskListener;

import java.time.Duration;

/**
 * 轻量可控的延时任务，任务到达指定时间后会执行、任务可取消、任务可增加延时时间
 *
 * @author 渔民小镇
 * @date 2024-09-01
 * @since 21.16
 */
public interface DelayTask {

    /**
     * get taskId
     *
     * @return taskId
     */
    String getTaskId();

    /**
     * 获取任务监听对象
     *
     * @param <T> t
     * @return 任务监听
     */
    <T extends TaskListener> T getTaskListener();

    /**
     * 是否活跃的任务
     *
     * @return true 活跃的
     */
    boolean isActive();

    /**
     * 取消任务
     */
    void cancel();

    /**
     * 剩余的延时时间 millis
     *
     * @return 剩余的延时时间 millis
     */
    long getMillis();

    /**
     * 增加延时时间
     *
     * @param duration duration
     * @return DelayTask
     */
    default DelayTask plusTime(Duration duration) {
        return this.plusTimeMillis(duration.toMillis());
    }

    /**
     * 增加延时时间
     * <p>
     * for example
     * <pre>{@code
     *     DelayTask delayTask = ...;
     *     delayTask.plusTimeMillis(500);  // 增加 0.5 秒的延时时间
     *     delayTask.plusTimeMillis(-500); // 减少 0.5 秒的延时时间
     * }</pre>
     *
     * @param millis millis（当为负数时，表示减少延时时间）
     * @return DelayTask
     */
    DelayTask plusTimeMillis(long millis);

    /**
     * 减少延时时间
     * <p>
     * for example
     * <pre>{@code
     *     DelayTask delayTask = ...;
     *     delayTask.minusTimeMillis(500);  // 减少 0.5 秒的延时时间
     *     delayTask.minusTimeMillis(-500); // 增加 0.5 秒的延时时间
     * }</pre>
     *
     * @param millis millis（当为负数时，表示增加延时时间）
     * @return DelayTask
     */
    default DelayTask minusTimeMillis(long millis) {
        return this.plusTimeMillis(-millis);
    }

    /**
     * 减少延时时间
     *
     * @param duration duration
     * @return DelayTask
     */
    default DelayTask minusTime(Duration duration) {
        return this.minusTimeMillis(duration.toMillis());
    }

    /**
     * 启动延时任务
     *
     * @return DelayTask
     */
    DelayTask task();
}
