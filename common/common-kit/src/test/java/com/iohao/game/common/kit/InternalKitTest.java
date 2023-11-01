package com.iohao.game.common.kit;

import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author 渔民小镇
 * @date 2023-07-02
 */
@Slf4j
public class InternalKitTest {

    @Test
    public void newTimeout() throws InterruptedException {
        InternalKit.newTimeoutSeconds(new TimerTask() {
            @Override
            public void run(Timeout timeout) {
                log.info("1-newTimeoutSeconds : {}", timeout);
                InternalKit.newTimeoutSeconds(this);
            }
        });

        InternalKit.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) {
                log.info("3-newTimeout : {}", timeout);
                InternalKit.newTimeout(this, 3, TimeUnit.SECONDS);
            }
        }, 3, TimeUnit.SECONDS);

        TimeUnit.SECONDS.sleep(22);
    }
}