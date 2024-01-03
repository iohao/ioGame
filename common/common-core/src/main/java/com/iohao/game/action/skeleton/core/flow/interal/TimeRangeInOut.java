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
package com.iohao.game.action.skeleton.core.flow.interal;

import com.iohao.game.action.skeleton.core.flow.ActionMethodInOut;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.core.flow.attr.FlowOption;
import com.iohao.game.common.kit.CollKit;
import com.iohao.game.common.kit.TimeKit;
import com.iohao.game.common.kit.concurrent.TaskKit;
import lombok.Getter;
import org.jctools.maps.NonBlockingHashMap;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 每日各时间段调用统计插件
 * <pre>
 *     <a href="https://www.yuque.com/iohao/game/umzk2d6lovo4n9gz">文档</a>
 * </pre>
 * <pre>{@code
 *     BarSkeletonBuilder builder = ...;
 *     // 各时间段 action 调用统计插件，将插件添加到业务框架中
 *     var timeRangeInOut = new TimeRangeInOut();
 *     builder.addInOut(timeRangeInOut);
 * }
 * </pre>
 * 打印预览 - 一天各小时各分钟阶段的调用统计数据
 * <pre>
 *  2023-11-29 action 调用次数 共 [100] 次
 * 	0:00 共 8 次; - [15~30分钟 3 次] - [30~45分钟 2 次] - [45~59分钟 3 次]
 * 	1:00 共 9 次; - [0~15分钟 1 次] - [15~30分钟 4 次] - [30~45分钟 1 次] - [45~59分钟 3 次]
 * 	2:00 共 4 次; - [0~15分钟 1 次] - [15~30分钟 2 次] - [45~59分钟 1 次]
 * 	3:00 共 2 次; - [0~15分钟 1 次] - [15~30分钟 1 次]
 * 	4:00 共 1 次; - [0~15分钟 1 次]
 * 	5:00 共 4 次; - [0~15分钟 1 次] - [15~30分钟 1 次] - [30~45分钟 1 次] - [45~59分钟 1 次]
 * 	6:00 共 5 次; - [0~15分钟 1 次] - [15~30分钟 1 次] - [30~45分钟 1 次] - [45~59分钟 2 次]
 * 	7:00 共 4 次; - [15~30分钟 2 次] - [30~45分钟 1 次] - [45~59分钟 1 次]
 * 	8:00 共 4 次; - [0~15分钟 1 次] - [30~45分钟 3 次]
 * 	9:00 共 4 次; - [15~30分钟 2 次] - [30~45分钟 2 次]
 * 	10:00 共 5 次; - [15~30分钟 2 次] - [30~45分钟 1 次] - [45~59分钟 2 次]
 * 	11:00 共 3 次; - [15~30分钟 2 次] - [45~59分钟 1 次]
 * 	12:00 共 4 次; - [0~15分钟 2 次] - [30~45分钟 2 次]
 * 	13:00 共 1 次; - [30~45分钟 1 次]
 * 	14:00 共 5 次; - [0~15分钟 1 次] - [45~59分钟 4 次]
 * 	15:00 共 6 次; - [0~15分钟 1 次] - [15~30分钟 2 次] - [45~59分钟 3 次]
 * 	16:00 共 4 次; - [0~15分钟 1 次] - [15~30分钟 1 次] - [30~45分钟 1 次] - [45~59分钟 1 次]
 * 	17:00 共 7 次; - [0~15分钟 1 次] - [15~30分钟 3 次] - [30~45分钟 3 次]
 * 	18:00 共 2 次; - [0~15分钟 1 次] - [15~30分钟 1 次]
 * 	19:00 共 7 次; - [0~15分钟 1 次] - [15~30分钟 3 次] - [30~45分钟 3 次]
 * 	20:00 共 5 次; - [15~30分钟 3 次] - [30~45分钟 2 次]
 * 	21:00 共 3 次; - [15~30分钟 2 次] - [30~45分钟 1 次]
 * 	22:00 共 1 次; - [45~59分钟 1 次]
 * 	23:00 共 2 次; - [15~30分钟 1 次] - [45~59分钟 1 次]
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-11-29
 */
@Getter
public final class TimeRangeInOut implements ActionMethodInOut {
    public static final FlowOption<LocalDate> localDate = FlowOption.valueOf("TimeRangeInOut-LocalDate");
    public static final FlowOption<LocalTime> localTime = FlowOption.valueOf("TimeRangeInOut-LocalTime");

    final TimeRangeDayRegion region = new TimeRangeDayRegion();

    ChangeListener listener = new ChangeListener() {
    };

    public void setListener(ChangeListener listener) {
        this.listener = Objects.requireNonNull(listener);
    }

    @Override
    public void fuckIn(FlowContext flowContext) {
        flowContext.option(localDate, listener.nowLocalDate());
        flowContext.option(localTime, listener.nowLocalTime());
    }

    @Override
    public void fuckOut(FlowContext flowContext) {
        LocalDate localDate = flowContext.option(TimeRangeInOut.localDate);
        LocalTime localTime = flowContext.option(TimeRangeInOut.localTime);

        this.region.update(localDate, localTime, flowContext);
    }

    @Getter
    public class TimeRangeDayRegion {
        final Map<LocalDate, TimeRangeDay> map = new NonBlockingHashMap<>();

        public void forEach(BiConsumer<LocalDate, TimeRangeDay> action) {
            this.map.forEach(action);
        }

        public void update(LocalDate localDate, LocalTime localTime, FlowContext flowContext) {

            TimeRangeDay timeRangeDay = this.getTimeRangeDay(localDate);
            timeRangeDay.increment(localTime);

            // 变更回调
//            listener.changed(timeRangeDay, localTime, flowContext);
        }

        public TimeRangeDay getTimeRangeDay(LocalDate localDate) {

            TimeRangeDay timeRangeDay = this.map.get(localDate);

            // 无锁化
            if (Objects.isNull(timeRangeDay)) {

                TimeRangeDay rangeDay = TimeRangeInOut.this.listener.createTimeRangeDay(localDate);
                timeRangeDay = this.map.putIfAbsent(localDate, Objects.requireNonNull(rangeDay));

                if (Objects.isNull(timeRangeDay)) {
                    timeRangeDay = this.map.get(localDate);

                    /*
                     * 回调昨天的数据
                     * 原计划是想保留 map 中的数据让开发者自行处理，
                     * 考虑到开发者可能会忘记移除 map 中的数据，为防止造成隐患，这里就直接移除昨天的数据了。
                     */
                    TaskKit.execute(() -> {
                        LocalDate yesterdayLocalDate = localDate.minusDays(1);
                        Optional.ofNullable(this.map.remove(yesterdayLocalDate))
                                .ifPresent(timeRangeYesterday -> TimeRangeInOut.this.listener.callbackYesterday(timeRangeYesterday));
                    });
                }
            }

            return timeRangeDay;
        }
    }

    /**
     * 一天的调用统计对象
     *
     * @param localDate      日期
     * @param count          一天的 action 调用总次数
     * @param timeRangeHours 时间段
     */
    public record TimeRangeDay(LocalDate localDate, LongAdder count, TimeRangeHour[] timeRangeHours) {
        public static TimeRangeDay create(LocalDate localDate, List<TimeRangeHour> timeRangeHours) {

            TimeRangeDay timeRangeDay = new TimeRangeDay(localDate, new LongAdder(), new TimeRangeHour[24]);

            for (TimeRangeHour timeRangeHour : timeRangeHours) {
                int hour = timeRangeHour.getHour();
                timeRangeDay.timeRangeHours[hour] = timeRangeHour;
            }

            return timeRangeDay;
        }

        public Stream<TimeRangeHour> stream() {
            return Arrays.stream(this.timeRangeHours)
                    .filter(Objects::nonNull);
        }

        public TimeRangeHour getTimeRangeHour(LocalTime localTime) {
            var hour = localTime.getHour();
            return this.timeRangeHours[hour];
        }

        public void increment(LocalTime localTime) {

            this.count.increment();

            var timeRangeHour = this.getTimeRangeHour(localTime);

            if (Objects.nonNull(timeRangeHour)) {
                timeRangeHour.increment(localTime);
            }
        }

        @Override
        public String toString() {

            String localDateFormat = TimeKit.dateFormatterYMD.format(this.localDate);

            List<TimeRangeHour> timeRangeHoursList = stream()
                    .filter(timeRangeHour -> timeRangeHour.count.sum() > 0)
                    .toList();

            if (CollKit.isEmpty(timeRangeHoursList)) {
                // TimeRange
                return localDateFormat + " action 调用次数暂无数据";
            }

            StringBuilder builder = new StringBuilder();
            builder.append(localDateFormat).append(" action 调用次数 共 [").append(this.count).append("] 次");

            for (TimeRangeHour timeRangeHour : timeRangeHoursList) {
                builder.append("\n\t").append(timeRangeHour);
            }

            return builder.toString();
        }
    }

    /**
     * 一小时的调用统计对象
     *
     * @param hourTime   小时
     * @param count      一小时的 action 调用次数
     * @param minuteList 分钟时间段
     */
    public record TimeRangeHour(LocalTime hourTime, LongAdder count, List<TimeRangeMinute> minuteList) {

        public static TimeRangeHour create(int hour, List<TimeRangeMinute> minuteList) {
            var hourTime = LocalTime.of(hour, 0);
            return new TimeRangeHour(hourTime, new LongAdder(), minuteList);
        }

        void increment(LocalTime localTime) {
            this.count.increment();

            if (CollKit.isEmpty(this.minuteList)) {
                return;
            }

            int minute = localTime.getMinute();

            this.minuteList.stream()
                    .filter(timeRangeMinute -> timeRangeMinute.inRange(minute))
                    .findAny()
                    .ifPresent(TimeRangeMinute::increment);
        }

        public int getHour() {
            return this.hourTime.getHour();
        }

        @Override
        public String toString() {

            String hourStr = String.format("%d:00 共 %s 次;"
                    , this.getHour()
                    , this.count);

            if (CollKit.isEmpty(this.minuteList)) {
                return hourStr;
            }

            StringBuilder builder = new StringBuilder();
            builder.append(hourStr);

            this.minuteList.stream()
                    .filter(timeRangeMinute -> timeRangeMinute.count.sum() > 0)
                    .forEach(timeRangeMinute -> builder.append(" - ").append(timeRangeMinute));

            return builder.toString();
        }
    }


    /**
     * 分钟范围记录
     *
     * @param start 开始时间（分钟）
     * @param end   结束时间（分钟）
     * @param count 该时间范围所触发的执行次数
     */
    public record TimeRangeMinute(int start, int end, LongAdder count) {
        public static TimeRangeMinute create(int start, int end) {
            return new TimeRangeMinute(start, end, new LongAdder());
        }

        boolean inRange(int minute) {
            return minute >= this.start && minute <= this.end;
        }

        void increment() {
            this.count.increment();
        }

        @Override
        public String toString() {
            return String.format("[%d~%d分钟 %s 次]", this.start, this.end, this.count);
        }
    }

    /**
     * 监听
     */
    public interface ChangeListener {

//        default void changed(TimeRangeDay timeRangeDay, LocalTime localTime, FlowContext flowContext) {
//        }

        /**
         * 插件会在每天的 0:00 触发 callbackYesterday 方法，并将昨日的 TimeRangeDay 对象传入方法中
         *
         * @param timeRangeYesterday 一天的调用统计对象（一定不为 null）
         */
        default void callbackYesterday(TimeRangeDay timeRangeYesterday) {
        }

        default LocalDate nowLocalDate() {
            return LocalDate.now();
        }

        default LocalTime nowLocalTime() {
            return LocalTime.now();
        }

        default TimeRangeDay createTimeRangeDay(LocalDate localDate) {
            List<TimeRangeHour> timeRangeHourList = this.createListenerTimeRangeHourList();
            return TimeRangeDay.create(localDate, timeRangeHourList);
        }

        default List<TimeRangeHour> createListenerTimeRangeHourList() {
            // 创建对应 24 个小时的数据
            return IntStream.range(0, 24)
                    .mapToObj(this::createListenerTimeRangeHour)
                    .toList();
        }

        default TimeRangeHour createListenerTimeRangeHour(int hour) {
            List<TimeRangeMinute> timeRangeMinuteList = this.createListenerTimeRangeMinuteList();
            return TimeRangeHour.create(hour, timeRangeMinuteList);
        }

        default List<TimeRangeMinute> createListenerTimeRangeMinuteList() {
            return Collections.emptyList();
        }
    }
}