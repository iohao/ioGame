/*
 * ioGame
 * Copyright (C) 2021 - present  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
/**
 * 工具相关 - <a href="https://www.yuque.com/iohao/game/nykaacfzg4h1ynii">轻量可控的延时任务</a>，任务到达指定时间后会执行、任务可取消、任务可增加或减少延时时间、任务可被覆盖、可设置任务监听回调。
 * <p>
 * 轻量可控的延时任务 - 简介
 * <pre>
 *     我们知道，在 {@link com.iohao.game.common.kit.concurrent.TaskKit} 中，提供了一个任务、时间、延时监听、超时监听 ...等相结合的一个工具模块，通过 runOnce 可以执行一些延时任务；
 *     但有时，我们需要一些可控的延时任务，也就是延时时间可以根据后续的业务来变化，比如增加或减少延时的时间、取消任务 ...等可控的操作。
 * </pre>
 * 轻量可控的延时任务 - 特点
 * <pre>
 *     1. 任务到达指定时间后会执行
 *     2. 任务可取消
 *     3. 任务可增加、减少延时的时间
 *     4. 任务可被覆盖
 *     5. 可设置任务监听回调
 * </pre>
 * for example
 * <pre>{@code
 * public class DelayTaskTest {
 *     @Test
 *     public void runDelayTask() {
 *         // ---------------演示 - 延时任务---------------
 *         // 1 秒后执行延时任务
 *         DelayTaskKit.of(() -> {
 *                     log.info("1 秒后执行的延时任务");
 *                 })
 *                 .plusTime(Duration.ofSeconds(1)) // 1 秒后执行延时任务
 *                 .task(); // 启动任务
 *     }
 *
 *     @Test
 *     public void plusDelayTime() {
 *         // ---------------演示 - 增加延时时间---------------
 *         long timeMillis = System.currentTimeMillis();
 *
 *         DelayTask delayTask = DelayTaskKit.of(() -> {
 *                     long value = System.currentTimeMillis() - timeMillis;
 *                     log.info("增加延时时间，最终 {} ms 后，执行延时任务", value);
 *                     // Assert.assertTrue(value > 1490);
 *                 })
 *                 .plusTime(Duration.ofSeconds(1)) // 1 秒后执行延时任务
 *                 .task(); // 启动任务
 *
 *         delayTask.plusTimeMillis(500); // 增加 0.5 秒的延时
 *
 *         // 最终 1.5 秒后执行延时任务
 *     }
 *
 *     @Test
 *     public void minusDelayTime() {
 *         // ---------------演示 - 减少延时时间---------------
 *         long timeMillis = System.currentTimeMillis();
 *
 *         // 1 秒后执行延时任务
 *         DelayTask delayTask = DelayTaskKit.of(() -> {
 *                     long value = System.currentTimeMillis() - timeMillis;
 *                     log.info("减少延时时间，最终 {} ms 后，执行延时任务", value);
 *                     // Assert.assertTrue(value < 510);
 *                 })
 *                 .plusTime(Duration.ofSeconds(1)) // 1 秒后执行延时任务
 *                 .task(); // 启动任务
 *
 *         delayTask.plusTimeMillis(-400); // 减少 0.4 秒的延时时间
 *         delayTask.minusTimeMillis(100); // 减少 0.1 秒的延时时间
 *
 *         // 最终 0.5 秒后执行延时任务
 *     }
 *
 *     @Test
 *     public void coverDelayTask() throws InterruptedException {
 *         // ---------------演示 - 覆盖延时任务---------------
 *
 *         String taskId = "1";
 *
 *         DelayTaskKit.of(taskId, () -> log.info("执行任务 - 1"))
 *                 .plusTime(Duration.ofSeconds(2)) // 2 秒后执行延时任务
 *                 .task(); // 启动任务
 *
 *         TimeUnit.MILLISECONDS.sleep(500);
 *
 *         long timeMillis = System.currentTimeMillis();
 *
 *         // 因为 taskId 相同，所以会覆盖之前的延时任务
 *         DelayTask delayTask = DelayTaskKit.of(taskId, () -> {
 *                     long value = System.currentTimeMillis() - timeMillis;
 *                     log.info("执行任务 - 2，最终 {} ms 后，执行延时任务", value);
 *                     // Assert.assertTrue(value > 990);
 *                 })
 *                 .plusTime(Duration.ofSeconds(1)) // 1 秒后执行延时任务
 *                 .task(); // 启动任务
 *     }
 *
 *     @Test
 *     public void cancelDelayTask() throws InterruptedException {
 *         // ---------------演示 - 取消延时任务，通过 DelayTask 来取消---------------
 *
 *         DelayTask delayTask = DelayTaskKit.of(() -> {
 *                     log.info("取消 - 延时任务");
 *                 })
 *                 .plusTime(Duration.ofSeconds(2)) // 2 秒后执行延时任务
 *                 .task(); // 启动任务
 *
 *         log.info("0.5 秒后, 因为满足某个业务条件, 不想执行定时任务了");
 *         TimeUnit.MILLISECONDS.sleep(500);
 *
 *         delayTask.isActive(); // true
 *         delayTask.cancel(); // 取消任务
 *         delayTask.isActive(); // false
 *
 *         // -----------演示 - 取消延时任务，通过 taskId 来取消-----------
 *
 *         String taskId = "1";
 *         // 在创建延时任务时，设置 taskId
 *         DelayTaskKit.of(taskId, () -> log.info("通过 taskId 取消 - 延时任务"))
 *                 .plusTime(Duration.ofSeconds(1)) // 1 秒后执行延时任务
 *                 .task(); // 启动任务
 *
 *         log.info("0.5 秒后, 因为满足某个业务条件, 不想执行定时任务了");
 *         TimeUnit.MILLISECONDS.sleep(500);
 *         DelayTaskKit.cancelDelayTask(taskId); // 通过 taskId 取消任务
 *     }
 *
 *     @Test
 *     public void optionalDelayTask() {
 *         // ---------------演示 - 查找延时任务---------------
 *
 *         String newTaskId = "1";
 *         DelayTaskKit.of(newTaskId, () -> log.info("hello DelayTask"))
 *                 // 2.5 秒后执行延时任务。（这里演示添加延时时间的两个方法）
 *                 .plusTime(Duration.ofSeconds(1)) // 1 秒后执行延时任务
 *                 .plusTime(Duration.ofMillis(1000)) // 1 秒后执行延时任务
 *                 .plusTimeMillis(500) // 追加延时时间 0.5 秒
 *                 .task(); // 启动任务
 *
 *         // 在后续的业务中，可以通过 taskId 查找该延时任务
 *         Optional<DelayTask> optionalDelayTask = DelayTaskKit.optionalDelayTask(newTaskId);
 *         if (optionalDelayTask.isPresent()) {
 *             DelayTask delayTask = optionalDelayTask.get();
 *             log.info("{}", delayTask);
 *         }
 *
 *         // 通过 taskId 查找延时任务，存在则执行给定逻辑
 *         DelayTaskKit.ifPresentDelayTask(newTaskId, delayTask -> {
 *             delayTask.plusTimeMillis(500); // 增加 0.5 秒的延时时间
 *         });
 *     }
 *
 *     @Test
 *     public void customTaskListener() {
 *         // ---------------演示 - 增强 TaskListener ---------------
 *
 *         DelayTaskKit.of(new TaskListener() {
 *                     @Override
 *                     public void onUpdate() {
 *                         log.info("1.7 秒后执行的延时任务");
 *                     }
 *
 *                     @Override
 *                     public boolean triggerUpdate() {
 *                         // 是否触发 onUpdate 监听回调方法
 *                         return TaskListener.super.triggerUpdate();
 *                     }
 *
 *                     @Override
 *                     public Executor getExecutor() {
 *                         // 指定一个执行器来消费当前 onUpdate
 *                         Executors yourExecutors = ...;
 *                         return yourExecutors;
 *                     }
 *
 *                     @Override
 *                     public void onException(Throwable e) {
 *                         // 异常回调
 *                         TaskListener.super.onException(e);
 *                     }
 *                 })
 *                 .plusTime(Duration.ofMillis(1700))
 *                 .task();
 *     }
 * }
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2024-09-01
 * @since 21.16
 */
package com.iohao.game.common.kit.concurrent.timer.delay;