package com.iohao.game.action.skeleton.protocol.wrapper;

import lombok.extern.slf4j.Slf4j;
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

        Integer intV = 1;
        Object of1 = WrapperKit.of(intV);
        log.info("of1 : {}", of1);
    }
}