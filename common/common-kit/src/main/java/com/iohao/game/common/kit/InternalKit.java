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

import com.iohao.game.common.kit.concurrent.TaskKit;
import io.netty.util.TimerTask;
import lombok.experimental.UtilityClass;

import java.util.concurrent.TimeUnit;

/**
 * 内部工具类，开发者不要用在耗时 io 的任务上
 * <pre>
 *     已经废弃，将在下个大版本中移除，请使用 TaskKit 代替
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-06-30
 * @see TaskKit
 */
@Deprecated
@UtilityClass
public class InternalKit {

    /**
     * 使用其他线程执行任务
     *
     * @param command 任务
     */
    @Deprecated
    public void execute(Runnable command) {
        TaskKit.execute(command);
    }

    /**
     * 执行任务
     *
     * @param task 任务
     */
    @Deprecated
    public void newTimeoutSeconds(TimerTask task) {
        newTimeout(task, 0, TimeUnit.SECONDS);
    }

    /**
     * 延迟一定时间后执行任务；
     *
     * @param task  任务
     * @param delay 延迟时间
     * @param unit  延迟时间单位
     */
    @Deprecated
    public void newTimeout(TimerTask task, long delay, TimeUnit unit) {
        TaskKit.newTimeout(task, delay, unit);
    }
}

