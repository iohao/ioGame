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

import java.util.Optional;

/**
 * 轻量可控延时任务域接口，负责轻量可控延时任务的创建、获取、取消、统计任务数量 ...等相关操作。
 *
 * @author 渔民小镇
 * @date 2024-09-01
 * @since 21.16
 */
public interface DelayTaskRegion {

    /**
     * 通过 taskId 获取一个可控的延时任务 Optional
     *
     * @param taskId taskId
     * @return DelayTask Optional
     */
    Optional<DelayTask> optional(String taskId);

    /**
     * 根据 taskId 取消可控延时任务的执行。
     *
     * @param taskId taskId
     */
    void cancel(String taskId);

    /**
     * 统计当前延时任务的数量
     *
     * @return 当前延时任务数量
     */
    int count();

    /**
     * 创建一个可控的延时任务，并设置任务监听回调。
     * <pre>{@code
     * DelayTask delayTask = of(taskListener);
     * // 启动延时任务
     * delayTask.task();
     * }
     * </pre>
     *
     * @param taskListener 任务监听回调
     * @return 可控的延时任务
     */
    DelayTask of(TaskListener taskListener);

    /**
     * 创建一个可控的延时任务，并设置 taskId 和任务监听回调
     * <pre>{@code
     * DelayTask delayTask = of(taskId, taskListener);
     * // 启动延时任务
     * delayTask.task();
     * }
     * </pre>
     *
     * @param taskId       taskId
     * @param taskListener 任务监听回调
     * @return 可控的延时任务
     */
    DelayTask of(String taskId, TaskListener taskListener);
}
