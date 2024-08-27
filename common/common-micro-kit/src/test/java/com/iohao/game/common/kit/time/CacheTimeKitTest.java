package com.iohao.game.common.kit.time;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * @author 渔民小镇
 * @date 2024-08-27
 * @since 21.16
 */
public class CacheTimeKitTest {

    @Test
    public void nowLocalDate() {
        Assert.assertTrue(CacheTimeKit.nowLocalDate().isEqual(LocalDate.now()));
        Assert.assertTrue(CacheTimeKit.nowLocalDateTime().isBefore(LocalDateTime.now()));

        var result = System.currentTimeMillis() - CacheTimeKit.currentTimeMillis();
        Assert.assertTrue(Math.abs(result) < 10);
    }
}