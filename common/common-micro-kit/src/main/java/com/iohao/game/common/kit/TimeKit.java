/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 时间工具
 *
 * @author 渔民小镇
 * @date 2023-07-12
 */
@UtilityClass
public class TimeKit {
    public ZoneId defaultZoneId = ZoneId.systemDefault();
    public DateTimeFormatter defaultFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /** 时间更新策略 */
    UpdateCurrentTimeMillis updateCurrentTimeMillis = () -> {
    };

    public void setUpdateCurrentTimeMillis(UpdateCurrentTimeMillis updateCurrentTimeMillis) {
        TimeKit.updateCurrentTimeMillis = updateCurrentTimeMillis;
        updateCurrentTimeMillis.init();
    }

    public long currentTimeMillis() {
        return updateCurrentTimeMillis.getCurrentTimeMillis();
    }

    public int toSecond(LocalDateTime localDateTime) {
        // 获取毫秒数
        return (int) toInstant(localDateTime).getEpochSecond();
    }

    public long toMilli(LocalDateTime localDateTime) {
        // 获取毫秒数
        return toInstant(localDateTime).toEpochMilli();
    }

    public Instant toInstant(LocalDateTime localDateTime) {
        return localDateTime
                .atZone(defaultZoneId)
                .toInstant();
    }

    public LocalDateTime toLocalDateTime(long milliseconds) {
        Instant instant = Instant.ofEpochMilli(milliseconds);
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        return zonedDateTime.toLocalDateTime();
    }

    public String formatter(LocalDateTime localDateTime) {
        return localDateTime.format(defaultFormatter);
    }

    public String formatter() {
        return formatter(currentTimeMillis());
    }

    public String formatter(long milliseconds) {
        LocalDateTime localDateTime = toLocalDateTime(milliseconds);
        return localDateTime.format(defaultFormatter);
    }

    /**
     * 过期检测
     * <pre>
     *     与当前时间做比较，查看是否过期
     * </pre>
     *
     * @param milliseconds 需要检测的时间
     * @return true milliseconds 已经过期
     */
    public boolean expire(long milliseconds) {
        // 时间 - 当前时间
        return (milliseconds - currentTimeMillis()) <= 0;
    }

    public interface UpdateCurrentTimeMillis {
        void init();

        default long getCurrentTimeMillis() {
            return System.currentTimeMillis();
        }
    }
}
