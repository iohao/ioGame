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
    public void timerListener() throws InterruptedException {


        // 每秒调用一次
        InternalKit.addSecondsTimerListener(() -> log.info("tick 1 Seconds"));
        // 每分钟调用一次
        InternalKit.addMinuteTimerListener(() -> log.info("tick 1 Minute"));

        // 每 2 秒调用一次
        InternalKit.addTimerListener(() -> log.info("tick 2 Seconds"), 2, TimeUnit.SECONDS);
        // 每 30 分钟调用一次
        InternalKit.addTimerListener(() -> log.info("tick 30 Minute"), 30, TimeUnit.MINUTES);

        TimeUnit.SECONDS.sleep(7);
    }

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