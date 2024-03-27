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
        return createCacheBuilder()
                .build();
    }

    /**
     * 创建一个缓存管理构建器
     *
     * @return 缓存管理构建器
     */
    public Cache2kBuilder<String, TimerTask> createCacheBuilder() {
        return Cache2kBuilder.of(String.class, TimerTask.class)
                .sharpExpiry(true)
                .eternal(false)
                .entryCapacity(10240)
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
                });
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
