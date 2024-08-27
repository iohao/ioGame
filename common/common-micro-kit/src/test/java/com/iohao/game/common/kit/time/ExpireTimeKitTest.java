package com.iohao.game.common.kit.time;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * @author 渔民小镇
 * @date 2024-08-27
 * @since 21.16
 */
public class ExpireTimeKitTest {

    @Test
    public void expireLocalDate() {
        LocalDate localDate = CacheTimeKit.nowLocalDate()
                .minusDays(1);

        Assert.assertTrue(ExpireTimeKit.expireLocalDate(localDate));

        long timeMillis = System.currentTimeMillis() - 1;
        Assert.assertTrue(ExpireTimeKit.expireMillis(timeMillis));
    }
}