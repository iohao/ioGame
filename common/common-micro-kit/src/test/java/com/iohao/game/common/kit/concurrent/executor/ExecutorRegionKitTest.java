package com.iohao.game.common.kit.concurrent.executor;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
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
    public void name() {
        ExecutorRegion executorRegion = ExecutorRegionKit.getExecutorRegion();
        ExecutorRegion executorRegion2 = ExecutorRegionKit.createExecutorRegion();

        // ExecutorRegion
        Assert.assertEquals(executorRegion, ExecutorRegionKit.getExecutorRegion());
        Assert.assertNotEquals(executorRegion, executorRegion2);

        // global single SimpleThreadExecutor
        Assert.assertEquals(executorRegion.getSimpleThreadExecutor(0),
                executorRegion.getSimpleThreadExecutor(0));
        Assert.assertEquals(executorRegion.getSimpleThreadExecutor(0),
                ExecutorRegionKit.getSimpleThreadExecutor(0));

        Assert.assertEquals(executorRegion2.getSimpleThreadExecutor(0),
                executorRegion2.getSimpleThreadExecutor(0));
        Assert.assertEquals(executorRegion2.getSimpleThreadExecutor(0),
                ExecutorRegionKit.getSimpleThreadExecutor(0));

        // UserThreadExecutor
        Assert.assertEquals(executorRegion.getUserThreadExecutor(0)
                , executorRegion.getUserThreadExecutor(0));

        Assert.assertEquals(executorRegion2.getUserThreadExecutor(0)
                , executorRegion2.getUserThreadExecutor(0));

        Assert.assertNotEquals(executorRegion.getUserThreadExecutor(0)
                , executorRegion2.getUserThreadExecutor(0));
    }

    @Test
    public void userThreadExecutor() {
        long userId = 1;

        ExecutorRegion executorRegion = ExecutorRegionKit.getExecutorRegion();
        ThreadExecutor userThreadExecutor = executorRegion.getUserThreadExecutor(userId);

        extracted(userThreadExecutor);
    }

    private void extracted(ThreadExecutor threadExecutor) {
        Amount amount = new Amount();

        int loop = 100_000;
        for (int i = 0; i < loop; i++) {
            threadExecutor.execute(amount::inc);
        }

        CountDownLatch countDownLatch = new CountDownLatch(1);
        threadExecutor.execute(() -> {
            log.info("end - amount : {}", amount);
            countDownLatch.countDown();
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }

        Assert.assertEquals(amount.num, loop);
        log.info("threadExecutor : {}", threadExecutor);
    }

    @Test
    public void userVirtualThreadExecutor() {
        long userId = 1;

        ExecutorRegion executorRegion = ExecutorRegionKit.getExecutorRegion();
        ThreadExecutor userVirtualThreadExecutor = executorRegion.getUserVirtualThreadExecutor(userId);

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
    public void simpleThreadExecutor() {
        long userId = 1;

        ThreadExecutor simpleThreadExecutor = ExecutorRegionKit.getSimpleThreadExecutor(userId);
        extracted(simpleThreadExecutor);
    }

    void sleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static final class Amount {
        int num;

        void inc() {
            this.num++;
        }

        @Override
        public String toString() {
            return "Amount{" +
                    "num=" + num +
                    '}';
        }
    }
}