package com.iohao.game.common.kit;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author 渔民小镇
 * @date 2024-08-10
 */
public class ByteKitTest {

    @Test
    public void toBytes() {
        assertLong(Long.MAX_VALUE);
        assertLong(Long.MIN_VALUE);
        assertLong(0);
    }

    private void assertLong(long value) {
        byte[] bytes = ByteKit.toBytes(value);
        long result = ByteKit.getLong(bytes);
        Assert.assertEquals(value, result);
    }
}