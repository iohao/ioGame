package com.iohao.game.action.skeleton.protocol.wrapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author 渔民小镇
 * @date 2023-06-09
 */
@Slf4j
public class WrapperKitTest {

    @Test
    public void of() {
        IntValue of = WrapperKit.of(1);
        log.info("of : {}", of);

        int intV = 1;
        Object of1 = WrapperKit.of(intV);
        log.info("of1 : {}", of1);
    }

    @Test
    public void isWrapper() {
        Assert.assertTrue(WrapperKit.isWrapper(int.class));
        Assert.assertTrue(WrapperKit.isWrapper(Integer.class));
        Assert.assertTrue(WrapperKit.isWrapper(IntValue.class));

        Assert.assertTrue(WrapperKit.isWrapper(long.class));
        Assert.assertTrue(WrapperKit.isWrapper(Long.class));
        Assert.assertTrue(WrapperKit.isWrapper(LongValue.class));

        Assert.assertTrue(WrapperKit.isWrapper(boolean.class));
        Assert.assertTrue(WrapperKit.isWrapper(Boolean.class));
        Assert.assertTrue(WrapperKit.isWrapper(BoolValue.class));

        Assert.assertTrue(WrapperKit.isWrapper(String.class));
        Assert.assertTrue(WrapperKit.isWrapper(StringValue.class));
    }
}