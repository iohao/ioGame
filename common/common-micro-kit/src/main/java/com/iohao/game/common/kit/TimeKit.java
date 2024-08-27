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
import com.iohao.game.common.kit.time.*;

import lombok.Setter;
import lombok.experimental.UtilityClass;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 时间工具
 *
 * @author 渔民小镇
 * @date 2023-07-12
 * @deprecated 请使用 {@link com.iohao.game.common.kit.time} 相关类
 */
@Deprecated
@UtilityClass
public class TimeKit {
    /**
     * @deprecated 请使用 {@link ConfigTimeKit#getDefaultZoneId()}
     */
    @Deprecated
    public ZoneId defaultZoneId = ConfigTimeKit.getDefaultZoneId();
    /**
     * @deprecated 请使用 {@link FormatTimeKit#ofPattern(String)}
     */
    @Deprecated
    public DateTimeFormatter defaultFormatter = FormatTimeKit.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * @deprecated 请使用 {@link FormatTimeKit#ofPattern(String)}
     */
    @Deprecated
    public final DateTimeFormatter dateFormatterYMD = FormatTimeKit.ofPattern("yyyy-MM-dd");
    @Deprecated
    final DateTimeFormatter dateFormatterYMDShort = FormatTimeKit.ofPattern("yyyyMMdd");

    volatile LocalDate localDate;
    volatile long currentTimeMillis;

    /** 时间更新策略 */
    @Setter
    @Deprecated
    UpdateCurrentTimeMillis updateCurrentTimeMillis;

    /**
     * 获取 LocalDate，默认每分钟更新一次，可有效减少 LocalDate 对象的创建。
     *
     * @return LocalDate
     * @deprecated 请使用 {@link CacheTimeKit#nowLocalDate()}
     */
    @Deprecated
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
     * @deprecated 请使用 {@link CacheTimeKit#currentTimeMillis()} 代替
     */
    @Deprecated
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

    /**
     * toSecond
     *
     * @param localDateTime localDateTime
     * @return toSecond
     * @deprecated {@link ToTimeKit#toSeconds(LocalDateTime)}
     */
    @Deprecated
    public int toSecond(LocalDateTime localDateTime) {
        // 获取毫秒数
        return ToTimeKit.toSeconds(localDateTime);
    }

    /**
     * toMilli
     *
     * @param localDateTime localDateTime
     * @return timeMillis
     * @deprecated 请使用 {@link ToTimeKit#toMillis(LocalDateTime)}
     */
    @Deprecated
    public long toMilli(LocalDateTime localDateTime) {
        // 获取毫秒数
        return ToTimeKit.toMillis(localDateTime);
    }

    /**
     * toMilli
     *
     * @param localDate localDate
     * @return toMilli
     * @deprecated 请使用 {@link ToTimeKit#toMillis(LocalDateTime)}
     */
    @Deprecated
    public long toMilli(LocalDate localDate) {
        var localDateTime = LocalDateTime.of(localDate, LocalTime.MIDNIGHT);
        // 转换为毫秒
        return ToTimeKit.toMillis(localDateTime);
    }

    /**
     * toInstant
     *
     * @param localDateTime localDateTime
     * @return Instant
     * @deprecated 请使用 {@link ToTimeKit#toInstant(LocalDateTime)}
     */
    @Deprecated
    public Instant toInstant(LocalDateTime localDateTime) {
        return localDateTime
                .atZone(defaultZoneId)
                .toInstant();
    }

    /**
     * toLocalDateTime
     *
     * @param milliseconds milliseconds
     * @return LocalDateTime
     * @deprecated {@link ToTimeKit#toLocalDateTime(long)}
     */
    @Deprecated
    public LocalDateTime toLocalDateTime(long milliseconds) {
        return ToTimeKit.toLocalDateTime(milliseconds);
    }

    /**
     * formatter
     *
     * @param localDateTime localDateTime
     * @return fmt
     * @deprecated 请使用 {@link FormatTimeKit#format(TemporalAccessor)}
     */
    @Deprecated
    public String formatter(LocalDateTime localDateTime) {
        return FormatTimeKit.format(localDateTime);
    }

    /**
     * formatter
     *
     * @return fmt
     * @deprecated 请使用 {@link FormatTimeKit#format()}
     */
    @Deprecated
    public String formatter() {
        return formatter(currentTimeMillis());
    }

    /**
     * formatter
     *
     * @param milliseconds milliseconds
     * @return String
     * @deprecated {@link FormatTimeKit#format(long)}
     */
    @Deprecated
    public String formatter(long milliseconds) {
        return FormatTimeKit.format(milliseconds);
    }

    /**
     * 将 LocalDate 转为 number
     *
     * @param localDate localDate
     * @return long
     */
    @Deprecated
    public long localDateToNumber(LocalDate localDate) {
        return toMilli(localDate);
    }

    /**
     * 过期检测
     *
     * @param epochDay 格式 yyyyMMdd
     * @return true 表示日期已经过期
     * @deprecated {@link ExpireTimeKit#expireLocalDate(long)}
     */
    @Deprecated
    public boolean expireLocalDate(long epochDay) {
        return ExpireTimeKit.expireLocalDate(epochDay);
    }

    /**
     * 过期检测
     * <pre>
     *     与当前时间做比较，查看是否过期
     * </pre>
     *
     * @param milliseconds 需要检测的时间
     * @return true milliseconds 已经过期
     * @deprecated {@link ExpireTimeKit#expireMillis(long)}
     */
    @Deprecated
    public boolean expire(long milliseconds) {
        // 时间 - 当前时间
        return ExpireTimeKit.expireMillis(milliseconds);
    }

    @Deprecated
    public interface UpdateCurrentTimeMillis {
        default long getCurrentTimeMillis() {
            return System.currentTimeMillis();
        }
    }

    @Deprecated
    interface TimeUpdatingStrategy {
        default void update() {
        }
    }

    @Deprecated
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

    @Deprecated
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
