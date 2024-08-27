package com.iohao.game.common.kit;

import com.iohao.game.common.kit.time.CacheTimeKit;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

/**
 * @author 渔民小镇
 * @date 2024-06-20
 */
public class TimeKitTest {
    @Test
    public void test() {
        long millis = CacheTimeKit.currentTimeMillis();
        Assert.assertTrue(millis > 0);

        LocalDate localDate = CacheTimeKit.nowLocalDate();
        Assert.assertTrue(localDate.isEqual(LocalDate.now()));
    }
}