package com.iohao.game.common.kit.concurrent.timer.delay;

import com.iohao.game.common.kit.RandomKit;
import com.iohao.game.common.kit.concurrent.TaskListener;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * @author 渔民小镇
 * @date 2024-09-01
 */
@Slf4j
public class DelayTaskTest {
    DelayTaskRegion delayTaskRegion;

    @Before
    public void setUp() {
        DelayTaskKit.setDelayTaskRegion(new DebugDelayTaskRegion());
        delayTaskRegion = DelayTaskKit.delayTaskRegion;
    }

    @After
    public void tearDown() throws Exception {
        TimeUnit.SECONDS.sleep(3);
        log.info("--------剩余任务数量 {}", delayTaskRegion.count());
    }

    @Test
    public void runDelayTask() {
        log.info("演示 - 延时任务");

        long timeMillis = System.currentTimeMillis();
        // 1 秒后执行延时任务
        DelayTaskKit.of(() -> {
                    log.info("1 秒后执行的延时任务");
                    long value = System.currentTimeMillis() - timeMillis;
                    Assert.assertTrue(value > 990);
                })
                // N 秒后触发
                .plusTime(Duration.ofSeconds(1))
                // 启动任务
                .task();
    }

    @Test
    public void plusDelayTime() {
        // ---------------增加 - 延时时间---------------
        log.info("演示 - 增加延时时间");

        long timeMillis = System.currentTimeMillis();
        // 1 秒后执行延时任务
        DelayTask delayTask = DelayTaskKit.of(() -> {
                    long value = System.currentTimeMillis() - timeMillis;
                    log.info("增加延时时间，最终 {} ms 后，执行延时任务", value);
                    Assert.assertTrue(value > 1490);
                })
                // N 秒后触发
                .plusTime(Duration.ofSeconds(1))
                // 启动任务
                .task();

        // 增加 0.5 秒的延时
        delayTask.plusTimeMillis(500);
        log.info("{}", delayTask);
        // 最终 1.5 秒后执行延时任务
    }

    @Test
    public void minusDelayTime() {
        // ---------------减少 - 延时时间---------------
        log.info("演示 - 减少延时时间");

        long timeMillis = System.currentTimeMillis();
        // 1 秒后执行延时任务
        DelayTask delayTask = DelayTaskKit.of(() -> {
                    long value = System.currentTimeMillis() - timeMillis;
                    log.info("减少延时时间，最终 {} ms 后，执行延时任务", value);

                    Assert.assertTrue(value < 510);
                })
                // N 秒后触发
                .plusTime(Duration.ofSeconds(1))
                // 启动任务
                .task();

        // 减少 0.5 秒的延时时间
        delayTask.minusTime(Duration.ofMillis(100))
                .plusTimeMillis(-400)
        ;

        log.info("{}", delayTask);
        // 最终 0.5 秒后执行延时任务
    }

    @Test
    public void coverDelayTask() throws InterruptedException {
        log.info("演示 - 覆盖延时任务");

        String taskId = "1";

        DelayTaskKit.of(taskId, () -> log.info("执行任务 - 1"))
                // N 秒后触发
                .plusTime(Duration.ofSeconds(2))
                // 启动任务
                .task();

        TimeUnit.MILLISECONDS.sleep(500);

        long timeMillis = System.currentTimeMillis();

        // 因为 taskId 相同，所以会覆盖之前的延时任务
        DelayTask delayTask = DelayTaskKit.of(taskId, () -> {
                    long value = System.currentTimeMillis() - timeMillis;

                    log.info("执行任务 - 2，最终 {} ms 后，执行延时任务", value);

                    Assert.assertTrue(value > 990);
                })
                // N 秒后触发
                .plusTime(Duration.ofSeconds(1))
                // 启动任务
                .task();

        log.info("{}", delayTask);
    }

    @Test
    public void cancelDelayTask() throws InterruptedException {
        // -----------取消 - 延时任务；通过 DelayTask 来取消-----------
        log.info("演示 - 取消延时任务");

        DelayTask delayTask = DelayTaskKit.of(() -> {
                    log.info("取消 - 延时任务");
                })
                // N 秒后触发
                .plusTime(Duration.ofSeconds(2))
                // 启动任务
                .task();

        Assert.assertEquals(1, delayTaskRegion.count());

        log.info("0.5 秒后, 因为满足某个业务条件, 不想执行定时任务了");
        TimeUnit.MILLISECONDS.sleep(500);
        // 取消任务
        delayTask.cancel();

        Assert.assertFalse(delayTask.isActive());
        Assert.assertEquals(0, delayTaskRegion.count());

        // -----------取消 - 延时任务；通过 taskId 来取消-----------

        log.info("演示 - 通过 taskId 取消延时任务");

        String taskId = "1";
        // 在创建延时任务时，设置 taskId
        DelayTaskKit.of(taskId, () -> log.info("通过 taskId 取消 - 延时任务"))
                // N 秒后触发
                .plusTime(Duration.ofSeconds(1))
                // 启动任务
                .task();

        Assert.assertEquals(1, delayTaskRegion.count());

        log.info("0.5 秒后, 因为满足某个业务条件, 不想执行定时任务了");
        TimeUnit.MILLISECONDS.sleep(500);
        // 通过 taskId 取消任务
        DelayTaskKit.cancel(taskId);

        Assert.assertEquals(0, delayTaskRegion.count());
    }

    @Test
    public void optionalDelayTask() {
        String newTaskId = "1";
        DelayTaskKit.of(newTaskId, () -> log.info("hello DelayTask"))
                // 2.5 秒后触发
                .plusTime(Duration.ofSeconds(2))
                .plusTimeMillis(500)
                // 启动任务
                .task();

        // 在后续的业务中，可以通过 taskId 查找该延时任务
        Optional<DelayTask> optionalDelayTask = DelayTaskKit.optional(newTaskId);
        if (optionalDelayTask.isPresent()) {
            DelayTask delayTask = optionalDelayTask.get();
            log.info("{}", delayTask);
        }

        // 通过 taskId 查找延时任务，存在则执行给定逻辑
        DelayTaskKit.ifPresent(newTaskId, delayTask -> {
            delayTask.plusTimeMillis(500); // 增加 0.5 秒的延时时间
        });
    }

    @Test
    public void customTaskListener() {
        // minus
        // ---------------演示 - 增强 TaskListener ---------------

        DelayTaskKit.of(new TaskListener() {
                    @Override
                    public void onUpdate() {
                        log.info("1.7 秒后执行的延时任务");
                    }

                    @Override
                    public boolean triggerUpdate() {
                        // 是否触发 onUpdate 监听回调方法
                        return TaskListener.super.triggerUpdate();
                    }

                    @Override
                    public Executor getExecutor() {
                        // 指定一个执行器来消费当前 onUpdate
                        return TaskListener.super.getExecutor();
                    }

                    @Override
                    public void onException(Throwable e) {
                        // 异常回调
                        TaskListener.super.onException(e);
                    }
                })
                .plusTime(Duration.ofMillis(1700))
                .task();
    }

    @Test
    public void more() {
        DelayTask delayTask = DelayTaskKit.of(new ShootTaskListener("敌人"))
                // 1.5 秒后触发。（这里演示添加延时时间的两个方法）
                .plusTime(Duration.ofSeconds(1))
                .plusTimeMillis(500)
                // 启动任务
                .task();

        // 当为 true 时，冷却减 0.5 秒，并且幸运加成
        if (RandomKit.randomBoolean()) {
            delayTask.minusTime(Duration.ofMillis(500));
            // 得到监听器，并设置幸运加成
            ShootTaskListener shootTaskListener = delayTask.getTaskListener();
            shootTaskListener.setLuck(true);
        }
    }

    @Slf4j
    @Setter
    static final class ShootTaskListener implements TaskListener {
        final String targetEntity;
        /** 幸运加成 */
        boolean luck;
        int attack = 10;

        public ShootTaskListener(String targetEntity) {
            this.targetEntity = targetEntity;
        }

        @Override
        public void onUpdate() {
            int value = luck ? attack * 2 : attack;
            log.info("向【{}】开炮，造成 {} 伤害", targetEntity, value);
        }
    }
}