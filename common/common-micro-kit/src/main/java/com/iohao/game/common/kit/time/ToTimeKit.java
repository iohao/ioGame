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

import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * 日期与时间 - 转换工具
 *
 * @author 渔民小镇
 * @date 2024-08-27
 * @since 21.16
 */
@UtilityClass
public final class ToTimeKit {

    /**
     * 将 LocalDateTime 转为 Millis
     *
     * @param localDateTime LocalDateTime
     * @return Millis
     */
    public long toMillis(LocalDateTime localDateTime) {
        return toInstant(localDateTime).toEpochMilli();
    }

    /**
     * 将 LocalDateTime 转为 Second
     *
     * @param localDateTime localDateTime
     * @return Second
     */
    public int toSeconds(LocalDateTime localDateTime) {
        return (int) toInstant(localDateTime).getEpochSecond();
    }

    /**
     * 将 Millis 转为 LocalDateTime
     *
     * @param timeMillis Millis
     * @return LocalDateTime
     */
    public LocalDateTime toLocalDateTime(long timeMillis) {
        var instant = Instant.ofEpochMilli(timeMillis);
        var zonedDateTime = instant.atZone(ConfigTimeKit.defaultZoneId);
        return zonedDateTime.toLocalDateTime();
    }

    /**
     * Converts this localDateTime to an Instant
     *
     * @param localDateTime localDateTime
     * @return Instant
     */
    public Instant toInstant(LocalDateTime localDateTime) {
        return localDateTime
                .atZone(ConfigTimeKit.defaultZoneId)
                .toInstant();
    }
}
