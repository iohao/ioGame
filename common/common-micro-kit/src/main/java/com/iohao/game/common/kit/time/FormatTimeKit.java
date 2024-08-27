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
package com.iohao.game.common.kit.time;

import com.iohao.game.common.kit.MoreKit;
import lombok.experimental.UtilityClass;
import org.jctools.maps.NonBlockingHashMap;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Map;
import java.util.Objects;

/**
 * 日期与时间 - 格式化工具
 *
 * @author 渔民小镇
 * @date 2024-08-27
 * @since 21.16
 */
@UtilityClass
public final class FormatTimeKit {
    private final Map<String, DateTimeFormatter> map = new NonBlockingHashMap<>();
    private final DateTimeFormatter defaultFormatter = ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * get singleton DateTimeFormatter by pattern
     *
     * @param pattern the pattern to use, not null
     * @return the formatter based on the pattern, not null
     */
    public DateTimeFormatter ofPattern(String pattern) {
        var dateTimeFormatter = map.get(pattern);

        // 无锁化
        if (Objects.isNull(dateTimeFormatter)) {
            return MoreKit.putIfAbsent(map, pattern, DateTimeFormatter.ofPattern(pattern));
        }

        return dateTimeFormatter;
    }

    /**
     * 将当前时间格式化为 yyyy-MM-dd HH:mm:ss
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public String format() {
        return format(CacheTimeKit.nowLocalDateTime());
    }

    /**
     * 将指定的 Millis 格式化为 yyyy-MM-dd HH:mm:ss
     *
     * @param timeMillis Millis
     * @return yyyy-MM-dd HH:mm:ss
     */
    public String format(long timeMillis) {
        var localDateTime = ToTimeKit.toLocalDateTime(timeMillis);
        return format(localDateTime);
    }

    /**
     * 将 TemporalAccessor 格式化为 yyyy-MM-dd HH:mm:ss
     *
     * @param temporal TemporalAccessor
     * @return yyyy-MM-dd HH:mm:ss
     */
    public String format(TemporalAccessor temporal) {
        return defaultFormatter.format(temporal);
    }

    /**
     * 将 TemporalAccessor 格式化为指定的格式
     *
     * @param temporal TemporalAccessor
     * @param pattern  指定的格式
     * @return 指定的格式
     */
    public String format(TemporalAccessor temporal, String pattern) {
        return ofPattern(pattern).format(temporal);
    }
}
