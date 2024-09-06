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
import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 轻量可控的延时任务工具类
 *
 * @author 渔民小镇
 * @date 2024-09-01
 * @since 21.16
 */
@UtilityClass
public class DelayTaskKit {
    /** 轻量可控延时任务域接口 */
    @Getter
    DelayTaskRegion delayTaskRegion = new SimpleDelayTaskRegion();

    /**
     * 设置轻量可控的延时任务域
     *
     * @param delayTaskRegion delayTaskRegion
     */
    public void setDelayTaskRegion(DelayTaskRegion delayTaskRegion) {
        Objects.requireNonNull(delayTaskRegion);

        var delayTaskRegionOld = DelayTaskKit.delayTaskRegion;
        DelayTaskKit.delayTaskRegion = delayTaskRegion;

        if (delayTaskRegionOld instanceof DelayTaskRegionEnhance stop) {
            stop.stop();
        }
    }

    /**
     * 通过 taskId 取消任务
     *
     * @param taskId taskId
     */
    public void cancel(String taskId) {
        delayTaskRegion.cancel(taskId);
    }

    /**
     * get Optional DelayTask by taskId
     *
     * @param taskId taskId
     * @return Optional DelayTask
     */
    public Optional<DelayTask> optional(String taskId) {
        return delayTaskRegion.optional(taskId);
    }

    /**
     * 如果 taskId 存在，就执行给定操作
     *
     * @param taskId   taskId
     * @param consumer 给定操作
     */
    public void ifPresent(String taskId, Consumer<DelayTask> consumer) {
        DelayTaskKit.optional(taskId).ifPresent(consumer);
    }

    /**
     * 创建一个轻量可控的延时任务
     *
     * @param taskListener 任务监听回调
     * @return 轻量可控的延时任务
     */
    public DelayTask of(TaskListener taskListener) {
        return delayTaskRegion.of(taskListener);
    }

    /**
     * 创建一个轻量可控的延时任务
     *
     * @param taskId       taskId （如果 taskId 相同，会覆盖之前的延时任务）
     * @param taskListener 任务监听回调
     * @return 轻量可控的延时任务
     */
    public DelayTask of(String taskId, TaskListener taskListener) {
        return delayTaskRegion.of(taskId, taskListener);
    }
}
