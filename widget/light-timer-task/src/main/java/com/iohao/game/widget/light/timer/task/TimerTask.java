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
package com.iohao.game.widget.light.timer.task;

import org.cache2k.expiry.ValueWithExpiryTime;

import java.io.Serializable;

/**
 * 定时任务
 *
 * @author 渔民小镇
 * @date 2021-12-25
 */
public interface TimerTask extends ValueWithExpiryTime, Serializable {
    /**
     * 执行方法
     * <pre>
     *     时间到就执行, 否则就不执行
     * </pre>
     */
    void execute();

    /**
     * 启动任务延时器
     *
     * @return me
     */
    <T extends TimerTask> T task();

    /**
     * 取消定时任务
     */
    void cancel();

    /**
     * 暂停任务一段时间
     * <pre>
     *     连续调用暂停时间不会累加, 需要到任务加入到队列中
     * </pre>
     *
     * @param stopTimeMillis 暂停的时间
     */
    void pause(long stopTimeMillis);

}
