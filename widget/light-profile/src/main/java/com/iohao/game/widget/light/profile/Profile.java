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
package com.iohao.game.widget.light.profile;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Profile
 * <pre>
 *     配置文件中的的管理域
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-01-02
 */
@Slf4j
@ToString
public class Profile {
    /** key */
    String key;

    Map<String, Object> map = new ConcurrentHashMap<>();

    Profile() {
    }

    /**
     * 获取 string 值
     *
     * @param key key
     * @return 不存在返回 "" 空字符串
     */
    public String get(String key) {
        return get(key, "");
    }

    /**
     * 获取 string 值
     *
     * @param key    key
     * @param defVal 默认值
     * @return key 不存在, 返回默认值
     */
    public String get(String key, String defVal) {
        Object value = map.get(key);
        return Objects.isNull(value) ? defVal : String.valueOf(value);
    }

    /**
     * 获取 bool 值
     *
     * @param key key
     * @return key 不存在, 返回false
     */
    public boolean getBool(String key) {
        return getBool(key, false);
    }

    /**
     * 获取 bool 值
     *
     * @param key    key
     * @param defVal 默认值
     * @return key 不存在, 返回默认值
     */
    public boolean getBool(String key, boolean defVal) {
        Object value = map.get(key);

        return value instanceof Boolean ? (boolean) value : defVal;
    }

    /**
     * 获取 int 值
     *
     * @param key key
     * @return key 不存在, 返回0
     */
    public int getInt(String key) {
        return getInt(key, 0);
    }

    /**
     * 获取 int 值
     *
     * @param key    key
     * @param defVal 默认值
     * @return key 不存在, 返回默认值
     */
    public int getInt(String key, int defVal) {
        Object value = map.get(key);

        return value instanceof Integer ? (int) value : defVal;
    }

    /**
     * 将 Properties 中的属性加载到当前对象中
     *
     * @param properties Properties
     */
    public void load(Properties properties) {
        for (Object o : properties.keySet()) {
            String key = o.toString();

            Object value = properties.get(o);

            this.map.put(key, value);
        }
    }

    /**
     * 需要加载的配置文件
     *
     * @param urls 需要加载的配置文件
     */
    public void load(List<URL> urls) {
        // 需要加载的配置文件
        urls.forEach(url -> {

            try (InputStream inputStream = url.openStream()) {
                Properties properties = new Properties();
                properties.load(inputStream);

                this.load(properties);

            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        });
    }

}
