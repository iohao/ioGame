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

import com.iohao.game.common.kit.concurrent.TaskKit;
import lombok.Setter;
import lombok.experimental.UtilityClass;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

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
    public final DateTimeFormatter dateFormatterYMD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    final DateTimeFormatter dateFormatterYMDShort = DateTimeFormatter.ofPattern("yyyyMMdd");

    /** 时间更新策略 */
    @Setter
    UpdateCurrentTimeMillis updateCurrentTimeMillis = new SecondUpdateCurrentTimeMillis();

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

    public long toMilli(LocalDate localDate) {
        // 转换为毫秒
        return localDate.toEpochDay() * 24 * 60 * 60 * 1000;
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
     * 将 LocalDate 转为 number
     *
     * @param localDate localDate
     * @return number，格式 yyyyMMdd
     */
    public long localDateToNumber(LocalDate localDate) {
        String format = dateFormatterYMDShort.format(localDate);
        return Long.parseLong(format);
    }

    /**
     * 过期检测
     *
     * @param localDateNumber 格式 yyyyMMdd
     * @return true 表示日期已经过期
     * @see TimeKit#localDateToNumber(LocalDate)
     */
    public boolean expireLocalDate(long localDateNumber) {
        long currentLocalDate = localDateToNumber(LocalDate.now());
        return currentLocalDate > localDateNumber;
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
        default long getCurrentTimeMillis() {
            return System.currentTimeMillis();
        }
    }

    private final class SecondUpdateCurrentTimeMillis implements UpdateCurrentTimeMillis {
        volatile long currentTimeMillis;

        public SecondUpdateCurrentTimeMillis() {
            TaskKit.runInterval(() -> {
                // 每秒更新一次当前时间
                currentTimeMillis = System.currentTimeMillis();
            }, 1, TimeUnit.SECONDS);
        }

        @Override
        public long getCurrentTimeMillis() {
            return this.currentTimeMillis;
        }
    }
}
