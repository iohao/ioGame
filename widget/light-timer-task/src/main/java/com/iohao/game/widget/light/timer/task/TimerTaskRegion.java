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

import org.cache2k.Cache;

import java.util.Objects;

/**
 * 定时任务域
 * <pre>
 *     负责定时任务的： 存储 获取
 * </pre>
 *
 * @author 渔民小镇
 * @date 2021-12-25
 */
public interface TimerTaskRegion {
    /**
     * 定时任务管理
     *
     * @return 定时任务管理
     */
    Cache<String, TimerTask> getCache();

    /**
     * region name
     *
     * @return regionName
     */
    String name();

    /**
     * 通过 key 删除定时任务
     *
     * @param key key
     */
    default void removeTimerTask(String key) {
        this.getCache().remove(key);
    }

    /**
     * getTimerTask
     *
     * @param key key
     * @return TimerTask
     */
    default TimerTask getTimerTask(String key) {
        return this.getCache().get(key);
    }

    /**
     * put 定时任务
     *
     * @param cacheKey  cacheKey
     * @param timerTask 定时任务
     */
    default void put(String cacheKey, TimerTask timerTask) {
        this.getCache().put(cacheKey, timerTask);
    }

    /**
     * 暂停 定时任务域 下的所有 定时任务
     * <pre>
     *     这个方法方这里不太好， 暂时放这吧
     * </pre>
     *
     * @param timeMillis 时间
     */
    default void pauseTask(long timeMillis) {
        Cache<String, TimerTask> cache = this.getCache();
        for (String key : cache.keys()) {
            TimerTask timerTask = cache.get(key);
            if (Objects.nonNull(timerTask)) {
                timerTask.pause(timeMillis);
            }
        }
    }
}
