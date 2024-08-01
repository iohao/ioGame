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

import com.iohao.game.common.kit.exception.ThrowKit;
import com.iohao.game.common.kit.id.IdKit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 定时任务执行 抽象类
 *
 * @author 渔民小镇
 * @date 2021-12-25
 */
@Getter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
@SuppressWarnings("unchecked")
public abstract class AbstractTimerTask implements TimerTask {
    @Serial
    private static final long serialVersionUID = -8201895378376640589L;

    /**
     * 多少毫秒后触发 execute 方法
     * <pre>
     *     例如设置 5000, 那么5秒后将触发执行方法
     * </pre>
     *
     * @see TimerTask#execute()
     */
    @Setter
    long delayExecutionTime;
    /** true 任务活跃中 */
    AtomicBoolean activity = new AtomicBoolean(true);

    /** 缓存唯一 key */
    @Setter
    String cacheKey;

    /** 任务启动时间 */
    long startTimeMillis;

    /**
     * 子类提供的 定时任务存储器
     *
     * @return 定时任务存储器
     */
    abstract protected TimerTaskRegion getTimerTaskRegion();

    /**
     * 取消定时任务
     */
    @Override
    public void cancel() {
        TimerTaskRegion timerTaskRegion = this.getTimerTaskRegion();
        timerTaskRegion.removeTimerTask(this.cacheKey);
    }

    /**
     * 启动任务延时器
     *
     * @return me
     */
    @Override
    public <T extends TimerTask> T task() {

        if (!activity.get()) {
            return (T) this;
        }

        if (Objects.isNull(this.cacheKey)) {
            // 随机分配一个 key
            this.cacheKey = IdKit.sid();
        }

        if (this.delayExecutionTime <= 0) {
            ThrowKit.ofRuntimeException("必须配置执行时间");
        }

        // 任务启动时间 = 当前时间 + 预期延后执行时间
//        this.startTimeMillis = System.currentTimeMillis() + delayExecutionTime;

        TimerTaskRegion timerTaskRegion = this.getTimerTaskRegion();
        timerTaskRegion.put(cacheKey, this);

        return (T) this;
    }

    /**
     * 暂停任务一段时间
     * <pre>
     *     连续调用暂停时间不会累加, 需要到任务加入到队列中
     * </pre>
     *
     * @param stopTimeMillis 暂停的时间
     */
    @Override
    public void pause(long stopTimeMillis) {
        // 取消任务
        this.cancel();

        // 当前时间
        long currentTimeMillis = System.currentTimeMillis();

        // 已经使用的时间 = 延迟时间 - (任务启动时间 - 当前时间)
        long runTime = this.delayExecutionTime - (this.startTimeMillis - currentTimeMillis);

        // 新的延迟时间 = 原有延迟时间 - 已经使用的时间 + 需要暂停的时间
        this.delayExecutionTime = this.delayExecutionTime - runTime + stopTimeMillis;

        // 重新启动任务
        this.task();
    }

    @Override
    public long getCacheExpiryTime() {
        long currentTimeMillis = System.currentTimeMillis();
        // 过期时间 = 当前时间 + 延迟时间
        this.startTimeMillis = currentTimeMillis + this.delayExecutionTime;
        return this.startTimeMillis;
    }
}