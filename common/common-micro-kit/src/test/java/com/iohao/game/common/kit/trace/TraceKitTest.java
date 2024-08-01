package com.iohao.game.common.kit.trace;

import com.iohao.game.common.kit.concurrent.executor.ExecutorRegion;
import com.iohao.game.common.kit.concurrent.executor.ExecutorRegionKit;
import com.iohao.game.common.kit.concurrent.executor.ThreadExecutor;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * @author 渔民小镇
 * @date 2024-01-21
 */
@Slf4j
public class TraceKitTest {
    static final ExecutorService executorService = Executors.newCachedThreadPool();
    final int internalLoop = 10000;
    final int mainLoop = 10;
    final CountDownLatch countDownLatch = new CountDownLatch(mainLoop);
    final Map<String, String> map = new ConcurrentHashMap<>();

    @After
    public void tearDown() {
        executorService.shutdown();

        sleep(22);
    }

    @Test
    public void newTraceId() {
        Assert.assertEquals(map.size(), mainLoop * internalLoop);
    }

    @Test
    public void newTraceIdUUID() {
        TraceKit.setDefaultTraceIdSupplier(() -> UUID.randomUUID().toString());

        newTraceIdProcess();
        Assert.assertEquals(map.size(), mainLoop * internalLoop);
    }

    @Test
    public void newTraceIdTemp() {

        TraceKit.setDefaultTraceIdSupplier(new TraceIdSupplier() {
            int i;

            @Override
            public String get() {
                i++;
                return Integer.toString(i);
            }
        });

        newTraceIdProcess();

        Assert.assertNotEquals(map.size(), mainLoop * internalLoop);
    }

    void newTraceIdProcess() {

        for (int i = 0; i < mainLoop; i++) {
            loopTraceId(data -> {
                for (String datum : data) {
                    map.put(datum, datum);
                }
            });
        }

        await();

        System.out.println(map.size());
        System.out.println("------- id show -------");
        int i = 0;
        for (String s : map.keySet()) {
            if (i++ > 1) {
                break;
            }

            System.out.println(s);
        }
    }

    void await() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    void loopTraceId(Consumer<String[]> consumer) {

        executorService.execute(() -> {
            int len = internalLoop;
            String[] data = new String[len];
            for (int i = 0; i < len; i++) {
                data[i] = TraceKit.newTraceId();
            }

            consumer.accept(data);

            countDownLatch.countDown();
        });
    }

    @Test
    public void name() {
        String traceIdName = "uuid";

        String id1 = TraceKit.newTraceId(traceIdName);
        Assert.assertNotNull(id1);
        log.info("traceId : {}", id1);

        TraceKit.putTraceIdSupplier(traceIdName, () -> UUID.randomUUID().toString());
        String id2 = TraceKit.newTraceId(traceIdName);
        Assert.assertNotNull(id2);
        log.info("traceId : {}", id2);
    }

    @Test
    public void testMDC() {

        long userId = 1;
        ExecutorRegion executorRegion = ExecutorRegionKit.getExecutorRegion();
        executorRegion.getUserThreadExecutor(userId).execute(() -> {
            MDC.put(TraceKit.traceName, "user thread");
            extractedVirtual(userId);
            MDC.clear();
        });

        sleep(55);
    }

    private void extractedVirtual(long userId) {
        ExecutorRegion executorRegion = ExecutorRegionKit.getExecutorRegion();
        ThreadExecutor userVirtualThreadExecutor = executorRegion.getUserVirtualThreadExecutor(userId);

        log.info("0-1 : {}", MDC.get(TraceKit.traceName));
        Assert.assertEquals("user thread", MDC.get(TraceKit.traceName));

        extracted(userVirtualThreadExecutor, "3-1", () -> {
            log.info("3-1 : {}", MDC.get(TraceKit.traceName));

            Assert.assertEquals("3-1", MDC.get(TraceKit.traceName));
        });

        extracted(userVirtualThreadExecutor, "4-1", () -> {
            log.info("4-1 : {}", MDC.get(TraceKit.traceName));

            Assert.assertEquals("4-1", MDC.get(TraceKit.traceName));
        });

        sleep(50);

        log.info("0-2 : {}", MDC.get(TraceKit.traceName));
        Assert.assertEquals("user thread", MDC.get(TraceKit.traceName));

        sleep(10);

    }

    private void extracted(ThreadExecutor userVirtualThreadExecutor, String traceId, Runnable command) {
        userVirtualThreadExecutor.execute(() -> {
            try {
                log.info("1-1 : {} - {}", MDC.get(TraceKit.traceName), traceId);
                Assert.assertNull(MDC.get(TraceKit.traceName));

                MDC.put(TraceKit.traceName, traceId);

                command.run();

                log.info("1-2 : {} - {}", MDC.get(TraceKit.traceName), traceId);
                Assert.assertEquals(MDC.get(TraceKit.traceName), traceId);
            } finally {
                MDC.clear();
            }
        });
    }

    void sleep(int milliseconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
}