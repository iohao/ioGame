package com.iohao.game.common.kit;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * @author 渔民小镇
 * @date 2024-06-20
 */
public class TimeKitTest {
    @Test
    public void test() {
        long millis = TimeKit.currentTimeMillis();
        Assert.assertTrue(millis > 0);

        LocalDate localDate = TimeKit.nowLocalDate();
        Assert.assertTrue(localDate.isEqual(LocalDate.now()));
    }
}