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

/**
 * 调度任务监听，使用 HashedWheelTimer 来模拟 ScheduledExecutorService 调度。
 * <p>
 * 当 {@code isActive } 返回 true 时，才会执行 {@code triggerUpdate 和 onUpdate} 方法
 * <p>
 * example
 * <pre>{@code
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
 *
 * @author 渔民小镇
 * @date 2023-12-01
 * @see TaskKit
 */
public interface IntervalTaskListener extends CommonTaskListener {
    /**
     * 是否活跃
     *
     * @return false 表示不活跃，会从监听列表中移除当前 TimerListener
     */
    default boolean isActive() {
        return true;
    }
}