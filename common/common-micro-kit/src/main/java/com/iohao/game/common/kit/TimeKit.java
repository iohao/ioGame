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

import com.iohao.game.common.kit.concurrent.TaskKit;
import lombok.Setter;
import lombok.experimental.UtilityClass;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 时间工具
 *
 * @author 渔民小镇
 * @date 2023-07-12
 */
@UtilityClass
public class TimeKit {
    public ZoneId defaultZoneId = TimeFormatterKit.defaultZoneId;
    /** 请使用 {@link TimeFormatterKit#defaultFormatter}  代替 */
    @Deprecated
    public DateTimeFormatter defaultFormatter = TimeFormatterKit.defaultFormatter;
    /** 请使用 {@link TimeFormatterKit#ofPattern(String)}  代替 */
    @Deprecated
    public final DateTimeFormatter dateFormatterYMD = TimeFormatterKit.ofPattern("yyyy-MM-dd");
    final DateTimeFormatter dateFormatterYMDShort = TimeFormatterKit.ofPattern("yyyyMMdd");

    volatile LocalDate localDate;
    volatile long currentTimeMillis;

    /** 时间更新策略 */
    @Setter
    UpdateCurrentTimeMillis updateCurrentTimeMillis;

    /**
     * 获取 LocalDate，默认每分钟更新一次，可有效减少 LocalDate 对象的创建。
     *
     * @return LocalDate
     */
    public LocalDate nowLocalDate() {

        if (Objects.nonNull(localDate)) {
            return localDate;
        }

        if (Objects.isNull(localDate)) {
            LocalDateTimeUpdatingStrategy.me().update();
        }

        return LocalDate.now();
    }

    /**
     * 获取 currentTimeMillis 的时间，默认每秒更新一次，如果对时间要求不需要很精准的，可以考虑使用。
     *
     * @return System.currentTimeMillis()
     */
    public long currentTimeMillis() {

        if (currentTimeMillis != 0) {
            return currentTimeMillis;
        }

        if (Objects.nonNull(updateCurrentTimeMillis)) {
            return updateCurrentTimeMillis.getCurrentTimeMillis();
        }

        if (currentTimeMillis == 0) {
            SecondUpdatingStrategy.me().update();
        }

        return System.currentTimeMillis();
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
        return localDateTime.format(TimeFormatterKit.defaultFormatter);
    }

    public String formatter() {
        return formatter(currentTimeMillis());
    }

    public String formatter(long milliseconds) {
        LocalDateTime localDateTime = toLocalDateTime(milliseconds);
        return localDateTime.format(TimeFormatterKit.defaultFormatter);
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

    interface TimeUpdatingStrategy {
        default void update() {
        }
    }

    private final class LocalDateTimeUpdatingStrategy implements TimeUpdatingStrategy {

        private LocalDateTimeUpdatingStrategy() {
            localDate = LocalDate.now();

            TaskKit.runInterval(() -> {
                // 每分钟更新一次当前时间
                localDate = LocalDate.now();
            }, 1, TimeUnit.MINUTES);
        }

        public static LocalDateTimeUpdatingStrategy me() {
            return Holder.ME;
        }

        /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
        private static class Holder {
            static final LocalDateTimeUpdatingStrategy ME = new LocalDateTimeUpdatingStrategy();
        }
    }

    private final class SecondUpdatingStrategy implements TimeUpdatingStrategy {
        private SecondUpdatingStrategy() {
            currentTimeMillis = System.currentTimeMillis();

            TaskKit.runInterval(() -> {
                // 每秒更新一次当前时间
                currentTimeMillis = System.currentTimeMillis();
            }, 1, TimeUnit.SECONDS);
        }

        public static SecondUpdatingStrategy me() {
            return Holder.ME;
        }

        /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
        private static class Holder {
            static final SecondUpdatingStrategy ME = new SecondUpdatingStrategy();
        }
    }
}
