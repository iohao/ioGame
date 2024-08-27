package com.iohao.game.common.kit.time;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

/**
 * @author 渔民小镇
 * @date 2024-08-27
 * @since 21.16
 */
@Slf4j
public class FormatTimeKitTest {

    @Test
    public void format() {
        String pattern = "yyyy-MM-dd";
        Assert.assertEquals(FormatTimeKit.ofPattern(pattern), FormatTimeKit.ofPattern(pattern));

        LocalDate localDate = CacheTimeKit.nowLocalDate();
        Assert.assertEquals(FormatTimeKit.format(localDate, pattern), FormatTimeKit.format(localDate, pattern));

        Assert.assertNotNull(FormatTimeKit.format(CacheTimeKit.nowLocalDateTime()));
        Assert.assertNotNull(FormatTimeKit.format());
    }

    @Test
    public void name() {
        LocalDate localDate = CacheTimeKit.nowLocalDate();
        long epochDay = localDate.toEpochDay();
        log.info("epochDay : {}", epochDay);
        log.info("epochDay : {}", localDate.plusDays(3).toEpochDay());


        String format = FormatTimeKit.format(System.currentTimeMillis());
        log.info("format : {}", format);
    }
}