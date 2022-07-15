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

import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.cache2k.event.CacheEntryExpiredListener;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 缓存管理器工具
 *
 * @author 渔民小镇
 * @date 2021-12-25
 */
@UtilityClass
public class Cache2Kit {

    /**
     * 存储设置
     * <pre>
     *     不是必须的
     *     如果你需要在系统停止时, 将定时任务存储起来 (比如存储到 redis 中). 那么这里提供了一个接口实现
     *     当然, 也可以自己实现存储
     * </pre>
     */
    @Setter
    private StoreSetting storeSetting = new StoreSetting() {
    };

    /**
     * 创建一个新的缓存管理器
     *
     * @return 缓存管理器
     */
    public Cache<String, TimerTask> createCache() {
        return Cache2kBuilder.of(String.class, TimerTask.class)
                .sharpExpiry(true)
                .eternal(false)
                // 过期时间 - 在此时间后过期
                .expiryPolicy((key, value, loadTime, oldEntry) -> value.getCacheExpiryTime())
                // 过期时触发的监听事件
                .addListener((CacheEntryExpiredListener<String, TimerTask>) (cache, task) -> {
                    TimerTask timerTask = task.getValue();
                    ((AbstractTimerTask) timerTask).activity.set(false);
                    // 执行业务方法
                    timerTask.execute();
                    // 移除任务
                    timerTask.cancel();
                })
                .build();
    }

    /**
     * 序列化
     *
     * @param key   key
     * @param cache 缓存管理器
     */
    public void serialize(String key, Cache<String, TimerTask> cache) {
        ConcurrentMap<String, TimerTask> taskMap = cache.asMap();
        if (!taskMap.isEmpty()) {
            ConcurrentMap<String, TimerTask> map = new ConcurrentHashMap<>(taskMap);
            storeSetting.put(key, map);
        } else {
            storeSetting.remove(key);
        }
    }

    /**
     * 反序列化
     *
     * @param key    key
     * @param region 缓存管理器
     */
    public void deserialize(String key, Cache<String, TimerTask> region) {
        ConcurrentMap<String, TimerTask> map = storeSetting.get(key);
        if (Objects.nonNull(map) && !map.isEmpty()) {
            region.putAll(map);
            storeSetting.remove(key);
        }
    }

    /**
     * 定时任务存储设置
     */
    public interface StoreSetting {
        /**
         * 序列表保存
         *
         * @param key 缓存 key
         * @param map 任务map
         */
        default void put(String key, ConcurrentMap<String, TimerTask> map) {
        }

        /**
         * 移除序列化数据
         *
         * @param key 缓存 key
         */
        default void remove(String key) {
        }

        /**
         * 获取序列化数据
         *
         * @param key 缓存 key
         * @return 任务 map
         */
        default ConcurrentMap<String, TimerTask> get(String key) {
            return null;
        }
    }
}
