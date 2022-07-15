/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License..
 */
package com.iohao.game.widget.light.timer.task;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
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
     * 启动定时器任务
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
            this.cacheKey = CacheKeyKit.uuid();
        }

        if (this.delayExecutionTime <= 0) {
            throw new RuntimeException("必须配置执行时间");
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
        System.out.println(LocalDateTime.now() + " _ " + this.delayExecutionTime);
        System.out.println(startTimeMillis - currentTimeMillis);
        return this.startTimeMillis;
    }
}