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
package com.iohao.game.common.kit;

import lombok.experimental.UtilityClass;
import org.jctools.maps.NonBlockingHashMap;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

/**
 * 时间格式化相关工具
 *
 * @author 渔民小镇
 * @date 2024-07-06
 * @since 21.11
 */
@UtilityClass
public class TimeFormatterKit {
    final Map<String, DateTimeFormatter> map = new NonBlockingHashMap<>();

    public ZoneId defaultZoneId = ZoneId.systemDefault();
    public DateTimeFormatter defaultFormatter = ofPattern("yyyy-MM-dd HH:mm:ss");

    public DateTimeFormatter ofPattern(String pattern) {
        DateTimeFormatter dateTimeFormatter = map.get(pattern);

        if (Objects.isNull(dateTimeFormatter)) {
            return MoreKit.putIfAbsent(map, pattern, DateTimeFormatter.ofPattern(pattern));
        }

        return dateTimeFormatter;
    }

    public String formatter() {
        return defaultFormatter.format(LocalDateTime.now());
    }
}
