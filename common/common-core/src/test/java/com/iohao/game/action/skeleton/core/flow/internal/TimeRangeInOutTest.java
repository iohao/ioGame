package com.iohao.game.action.skeleton.core.flow.internal;

import com.iohao.game.action.skeleton.core.BarMessageKit;
import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.common.kit.RandomKit;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author 渔民小镇
 * @date 2023-11-29
 */
public class TimeRangeInOutTest {

    @Test
    public void fuckOut() {
        TimeRangeInOut inOut = new TimeRangeInOut();
        setListener(inOut);

        RequestMessage requestMessage = BarMessageKit.createRequestMessage(CmdInfo.of(1, 1));
        FlowContext flowContext = new FlowContext();
        flowContext.setRequest(requestMessage);

        TimeRangeInOut.TimeRangeDayRegion region = inOut.region;

        LocalDate localDate = LocalDate.now();

        LongAdder count = new LongAdder();
        for (int i = 0; i < 10000; i++) {
            int hour = RandomKit.randomInt(24);
            int minute = RandomKit.randomInt(60);
            LocalTime localTime = LocalTime.of(hour, minute);
            region.update(localDate, localTime, flowContext);
            count.increment();
        }

        print(inOut);

        System.out.println(count);
    }

    private static void print(TimeRangeInOut inOut) {
        TimeRangeInOut.TimeRangeDayRegion region = inOut.region;

        // 取指定日期的 TimeRangeDay 对象
        LocalDate localDate = LocalDate.now();
        TimeRangeInOut.TimeRangeDay timeRangeDay = region.getTimeRangeDay(localDate);
        System.out.println(timeRangeDay);
    }

    private void setListener(TimeRangeInOut inOut) {
        inOut.setListener(new TimeRangeInOut.ChangeListener() {
            @Override
            public void callbackYesterday(TimeRangeInOut.TimeRangeDay timeRangeYesterday) {
                // 昨日的全天统计数据对象
                System.out.println(timeRangeYesterday);

                timeRangeYesterday.stream().forEach(timeRangeHour -> {
                    // 几点
                    int hour = timeRangeHour.getHour();
                    // 一小时的 action 调用次数
                    LongAdder count = timeRangeHour.count();

                    // 该小时的分钟阶段
                    List<TimeRangeInOut.TimeRangeMinute> timeRangeMinutes = timeRangeHour.minuteList();
                    for (TimeRangeInOut.TimeRangeMinute timeRangeMinute : timeRangeMinutes) {
                        // 该分钟阶段的 action 调用次数
                        LongAdder minuteCount = timeRangeMinute.count();
                    }

                });
            }

            @Override
            public List<TimeRangeInOut.TimeRangeMinute> createListenerTimeRangeMinuteList() {
                return List.of(
                        TimeRangeInOut.TimeRangeMinute.create(0, 29),
                        TimeRangeInOut.TimeRangeMinute.create(30, 59)
                );
            }
        });
    }
}