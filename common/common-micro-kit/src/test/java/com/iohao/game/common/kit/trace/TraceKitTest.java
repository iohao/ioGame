package com.iohao.game.common.kit.trace;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * @author 渔民小镇
 * @date 2024-01-21
 */
public class TraceKitTest {
    static final ExecutorService executorService = Executors.newCachedThreadPool();
    final int internalLoop = 10000;
    final int mainLoop = 10;
    final CountDownLatch countDownLatch = new CountDownLatch(mainLoop);
    final Map<String, String> map = new ConcurrentHashMap<>();

    @AfterClass
    public static void afterClass() {
        executorService.shutdown();
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
            throw new RuntimeException(e);
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

}