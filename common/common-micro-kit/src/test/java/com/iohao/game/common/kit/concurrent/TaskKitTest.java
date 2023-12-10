package com.iohao.game.common.kit.concurrent;

import com.iohao.game.common.kit.RandomKit;
import io.netty.util.Timeout;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author 渔民小镇
 * @date 2023-12-02
 */
@Slf4j
public class TaskKitTest {

    @Test
    public void onceTaskListener() throws InterruptedException {
        Timeout timeout = TaskKit.newTimeout(task -> {
            //
            log.info("1 Seconds");
        }, 1, TimeUnit.SECONDS);

        log.info("timeout : {}", timeout);

        // 只执行一次，2 秒后执行
        TaskKit.runOnce(() -> log.info("2 Seconds"), 2, TimeUnit.SECONDS);
        // 只执行一次，1 分钟后执行
        TaskKit.runOnce(() -> log.info("1 Minute"), 1, TimeUnit.MINUTES);

        // 只执行一次，500、800 milliseconds 后
        TaskKit.runOnce(() -> log.info("500 delayMilliseconds"), 500);
        TaskKit.runOnce(() -> log.info("800 delayMilliseconds"), 800);

        // 只执行一次，1500 Milliseconds后执行，当 theTriggerUpdate 为 true 时，才执行 onUpdate
        boolean theTriggerUpdate = RandomKit.randomBoolean();
        TaskKit.runOnce(new OnceTaskListener() {
            @Override
            public void onUpdate() {
                log.info("1500 delayMilliseconds");
            }

            @Override
            public boolean triggerUpdate() {
                return theTriggerUpdate;
            }

        }, 1500, TimeUnit.MILLISECONDS);

        TimeUnit.SECONDS.sleep(3);
    }

    @Test
    public void intervalTaskListener() throws InterruptedException {

        // 每分钟调用一次
        TaskKit.runIntervalMinute(() -> log.info("tick 1 Minute"), 1);
        // 每 2 分钟调用一次
        TaskKit.runIntervalMinute(() -> log.info("tick 2 Minute"), 2);

        // 每 2 秒调用一次
        TaskKit.runInterval(() -> log.info("tick 2 Seconds"), 2, TimeUnit.SECONDS);
        // 每 30 分钟调用一次
        TaskKit.runInterval(() -> log.info("tick 30 Minute"), 30, TimeUnit.MINUTES);

        //【示例 - 移除任务】每秒调用一次，当 hp 为 0 时就移除当前 TimerListener
        TaskKit.runInterval(new IntervalTaskListener() {
            int hp = 2;

            @Override
            public void onUpdate() {
                hp--;
                log.info("剩余 hp:2-{}", hp);
            }

            @Override
            public boolean isActive() {
                // 当返回 false 则表示不活跃，会从监听列表中移除当前 TimerListener
                return hp != 0;
            }
        }, 1, TimeUnit.SECONDS);

        //【示例 - 跳过执行】每秒调用一次，当 triggerUpdate 返回值为 true，即符合条件时才执行 onUpdate 方法
        TaskKit.runInterval(new IntervalTaskListener() {
            int hp;

            @Override
            public void onUpdate() {
                log.info("current hp:{}", hp);
            }

            @Override
            public boolean triggerUpdate() {
                hp++;
                // 当返回值为 true 时，会执行 onUpdate 方法
                return hp % 2 == 0;
            }
        }, 1, TimeUnit.SECONDS);

        //【示例 - 指定线程执行器】每秒调用一次
        // 如果有耗时的任务，比如涉及一些 io 操作的，建议指定执行器来执行当前回调（onUpdate 方法），以避免阻塞其他任务。
        ExecutorService executorService = TaskKit.getCacheExecutor();

        TaskKit.runInterval(new IntervalTaskListener() {
            @Override
            public void onUpdate() {
                log.info("执行耗时的 IO 任务，开始");

                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                log.info("执行耗时的 IO 任务，结束");
            }

            @Override
            public Executor getExecutor() {
                // 指定执行器来执行当前回调（onUpdate 方法），以避免阻塞其他任务。
                return executorService;
            }
        }, 1, TimeUnit.SECONDS);

        TimeUnit.SECONDS.sleep(20);
    }

//    @Test
//    public void concurrent() throws InterruptedException {
//        LongAdder[] arrays = TaskKit.arrays;
//        log.info("arrays : {}", Arrays.toString(arrays));
//
//        for (int i = 0; i < 10; i++) {
//            extractedThread(arrays.length);
//            extractedThread(5);
//        }
//
//        TaskKit.addIntervalTaskListener(() -> {
//            // print
//            log.info("arrays : {}", Arrays.toString(arrays));
//        }, 1, TimeUnit.SECONDS);
//
//        TimeUnit.SECONDS.sleep(5);
//    }

    private static void extractedThread(int length) {
        new Thread(() -> {
            for (int j = 1; j < length; j++) {
                var tempValue = j;
                TaskKit.runInterval(new IntervalTaskListener() {
                    public String getValue() {
                        return length + " - " + tempValue;
                    }

                    @Override
                    public void onUpdate() {

                    }
                }, j, TimeUnit.SECONDS);
            }
        }).start();
    }
}