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
        return Objects.isNull(value) ? defVal : value.toString();
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

        try {
            return Boolean.parseBoolean(value.toString());
        } catch (Throwable e) {
            return defVal;
        }
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

        try {
            return Integer.parseInt(value.toString());
        } catch (Throwable e) {
            return defVal;
        }
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
            // 理论上在这里做数据类型解析会好一些，但现在不着急

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
