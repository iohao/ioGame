package com.iohao.game.common.kit.concurrent.executor;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author 渔民小镇
 * @date 2024-01-23
 */
@Slf4j
public class ExecutorRegionKitTest {
    @After
    public void tearDown() {
        sleep();
    }

    @Test
    public void userThreadExecutor() {
        long userId = 1;

        ThreadExecutor userThreadExecutor = ExecutorRegionKit.getUserThreadExecutor(userId);

        userThreadExecutor.execute(() -> {
            // print 1
            log.info("userThreadExecutor : 1");
        });

        userThreadExecutor.execute(() -> {
            // print 2
            log.info("userThreadExecutor : 2");
        });
    }

    @Test
    public void getUserVirtualThreadExecutor() {
        long userId = 1;

        ThreadExecutor userVirtualThreadExecutor = ExecutorRegionKit.getUserVirtualThreadExecutor(userId);

        userVirtualThreadExecutor.execute(() -> {
            // print 1
            log.info("userVirtualThreadExecutor : 1");
        });

        userVirtualThreadExecutor.execute(() -> {
            // print 2
            log.info("userVirtualThreadExecutor : 2");
        });
    }

    @Test
    public void getSimpleThreadExecutor() {
        long userId = 1;

        ThreadExecutor simpleThreadExecutor = ExecutorRegionKit.getSimpleThreadExecutor(userId);

        simpleThreadExecutor.execute(() -> {
            // print 1
            log.info("simpleThreadExecutor : 1");
        });

        simpleThreadExecutor.execute(() -> {
            // print 2
            log.info("simpleThreadExecutor : 2");
        });
    }

    void sleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}