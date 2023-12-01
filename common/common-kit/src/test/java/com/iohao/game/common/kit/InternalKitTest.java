package com.iohao.game.common.kit;

import com.iohao.game.common.kit.concurrent.TimerListener;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

        //【示例 - 移除任务】每秒调用一次，当 hp 为 0 时就移除当前 TimerListener
        InternalKit.addTimerListener(new TimerListener() {
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
        InternalKit.addTimerListener(new TimerListener() {
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
        ExecutorService executorService = Executors.newCachedThreadPool();

        InternalKit.addTimerListener(new TimerListener() {
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