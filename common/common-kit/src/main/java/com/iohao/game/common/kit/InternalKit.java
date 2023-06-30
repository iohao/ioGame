/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.iohao.game.common.kit;

import io.netty.util.HashedWheelTimer;
import io.netty.util.TimerTask;
import lombok.experimental.UtilityClass;

import java.util.concurrent.TimeUnit;

/**
 * 内部工具类，开发者不要使用
 *
 * @author 渔民小镇
 * @date 2023-06-30
 */
@UtilityClass
public class InternalKit {
    /** 时间精度为 1 秒钟，执行一些没有 io 操作的逻辑 */
    public final HashedWheelTimer timerSeconds = new HashedWheelTimer(1, TimeUnit.SECONDS);

    public void newTimeoutSeconds(TimerTask task) {
        timerSeconds.newTimeout(task, 0, TimeUnit.SECONDS);
    }

    public void newTimeout(TimerTask task, long delay, TimeUnit unit) {
        timerSeconds.newTimeout(task, delay, unit);
    }
}
