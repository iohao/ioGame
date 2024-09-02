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
package com.iohao.game.common.kit.concurrent.timer.delay;

import com.iohao.game.common.kit.concurrent.IntervalTaskListener;
import com.iohao.game.common.kit.concurrent.TaskKit;
import com.iohao.game.common.kit.concurrent.TaskListener;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;

interface DelayTaskExecutor extends DelayTask {
    /**
     * 是否触发 onUpdate 监听回调方法
     *
     * @return true 执行 onUpdate 方法
     */
    boolean triggerUpdate();

    /**
     * Timer 监听回调
     */
    void onUpdate();

    /**
     * 异常回调，当 triggerUpdate 或 onUpdate 方法抛出异常时，将会传递到这里
     *
     * @param e e
     */
    default void onException(Throwable e) {
        System.err.println(e.getMessage());
    }

    /**
     * 执行 onUpdate 的执行器
     *
     * @return 当返回值为 null 时，将使用当前线程（默认 HashedWheelTimer）执行，否则使用该执行器来执行
     */
    Executor getExecutor();
}

interface DelayTaskRegionEnhance extends DelayTaskRegion {
    void stop();

    void forEach(Consumer<DelayTaskExecutor> consumer);

    void runDelayTask(DelayTaskExecutor delayTaskExecutor);
}

@Getter
final class DelayIntervalTaskListener implements IntervalTaskListener {
    final DelayTaskRegionEnhance delayTaskRegion;
    boolean active = true;

    DelayIntervalTaskListener(DelayTaskRegionEnhance delayTaskRegion) {
        this.delayTaskRegion = delayTaskRegion;
    }

    @Override
    public void onUpdate() {
        delayTaskRegion.forEach(task -> {
            Executor executor = task.getExecutor();

            if (Objects.nonNull(executor)) {
                executor.execute(() -> extractedFlowTaskListener(task));
            } else {
                this.extractedFlowTaskListener(task);
            }
        });
    }

    private void extractedFlowTaskListener(DelayTaskExecutor task) {
        try {
            // 移除不活跃的延时任务
            if (!task.isActive()) {
                delayTaskRegion.cancelDelayTask(task.getTaskId());
                return;
            }

            if (task.triggerUpdate()) {
                task.onUpdate();
            }
        } catch (Throwable e) {
            task.onException(e);
        }
    }
}

@Getter
class SimpleDelayTask implements DelayTaskExecutor {
    static final AtomicLong taskIdCounter = new AtomicLong();

    final String taskId;
    final TaskListener taskListener;
    final DelayTaskRegionEnhance delayTaskRegion;
    final LongAdder timeMillis = new LongAdder();
    final AtomicBoolean active = new AtomicBoolean(true);

    SimpleDelayTask(TaskListener taskListener, DelayTaskRegionEnhance delayTaskRegion) {
        this(String.valueOf(taskIdCounter.incrementAndGet()), taskListener, delayTaskRegion);
    }

    SimpleDelayTask(String taskId, TaskListener taskListener, DelayTaskRegionEnhance delayTaskRegion) {
        this.taskId = taskId;
        this.taskListener = taskListener;
        this.delayTaskRegion = delayTaskRegion;
    }

    @Override
    public boolean isActive() {
        return this.active.get();
    }

    @Override
    public void cancel() {
        if (isActive()) {
            this.active.set(false);
            this.delayTaskRegion.cancelDelayTask(taskId);
        }
    }

    @Override
    public long getMillis() {
        long sum = this.timeMillis.sum();
        return sum < 0 ? 0 : sum;
    }

    @Override
    public DelayTask plusTimeMillis(long millis) {
        if (this.isActive()) {
            this.timeMillis.add(millis);
        }

        return this;
    }

    @Override
    public void onUpdate() {

        this.cancel();

        if (this.taskListener.triggerUpdate()) {
            this.taskListener.onUpdate();
        }
    }

    static final long INTERVAL_MILLIS_CONSUMER = -SimpleDelayTaskRegion.INTERVAL_MILLIS * 2;

    @Override
    public boolean triggerUpdate() {
        // 时间 <= 0 时，就可以执行任务了
        this.plusTimeMillis(INTERVAL_MILLIS_CONSUMER);

        return this.isActive() && this.getMillis() <= 0;
    }

    @Override
    public void onException(Throwable e) {
        this.taskListener.onException(e);
    }

    @Override
    public Executor getExecutor() {
        return this.taskListener.getExecutor();
    }

    @Override
    public DelayTask task() {
        if (this.isActive()) {
            delayTaskRegion.runDelayTask(this);
        }

        return this;
    }

    @Override
    public String toString() {
        return "SimpleDelayTask{" +
                "taskId='" + taskId + '\'' +
                ", active=" + active +
                ", timeMillis=" + getMillis() +
                '}';
    }
}

class SimpleDelayTaskRegion implements DelayTaskRegion, DelayTaskRegionEnhance {
    static final long INTERVAL_MILLIS = 50;

    final Map<String, DelayTaskExecutor> taskMap = new NonBlockingHashMap<>();
    final DelayIntervalTaskListener taskListener;

    SimpleDelayTaskRegion() {
        this.taskListener = new DelayIntervalTaskListener(this);
        TaskKit.runInterval(this.taskListener, INTERVAL_MILLIS, TimeUnit.MILLISECONDS);
    }

    @Override
    public void forEach(Consumer<DelayTaskExecutor> consumer) {
        taskMap.values().forEach(consumer);
    }

    @Override
    public void runDelayTask(DelayTaskExecutor delayTaskExecutor) {
        this.taskMap.put(delayTaskExecutor.getTaskId(), delayTaskExecutor);
    }

    @Override
    public Optional<DelayTask> optionalDelayTask(String taskId) {
        return Optional.ofNullable(this.taskMap.get(taskId));
    }

    @Override
    public void cancelDelayTask(String taskId) {
        var task = taskMap.remove(taskId);
        if (Objects.nonNull(task) && task.isActive()) {
            task.cancel();
        }
    }

    @Override
    public int countDelayTask() {
        return this.taskMap.size();
    }

    @Override
    public void stop() {
        this.taskListener.active = false;
    }

    @Override
    public DelayTask of(TaskListener taskListener) {
        return new SimpleDelayTask(taskListener, this);
    }

    @Override
    public DelayTask of(String taskId, TaskListener taskListener) {
        return new SimpleDelayTask(taskId, taskListener, this);
    }
}

@Slf4j
final class DebugDelayTask extends SimpleDelayTask {
    final LongAdder sumMillis = new LongAdder();

    DebugDelayTask(String taskId, TaskListener taskListener, DelayTaskRegionEnhance delayTaskRegion) {
        super(taskId, taskListener, delayTaskRegion);
    }

    DebugDelayTask(TaskListener taskListener, DelayTaskRegionEnhance delayTaskRegion) {
        super(taskListener, delayTaskRegion);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        log.info("剩余任务数量 {}，{}", this.delayTaskRegion.countDelayTask(), this);
    }

    @Override
    public boolean triggerUpdate() {
        // 总耗时计算
        this.sumMillis.add(Math.abs(INTERVAL_MILLIS_CONSUMER));

        return super.triggerUpdate();
    }

    @Override
    public String toString() {
        return "DebugDelayTask{" +
                "taskId='" + taskId + '\'' +
                ", active=" + active +
                ", timeMillis=" + timeMillis +
                ", sumMillis=" + sumMillis +
                "} ";
    }
}

final class DebugDelayTaskRegion extends SimpleDelayTaskRegion {
    @Override
    public DelayTask of(TaskListener taskListener) {
        return new DebugDelayTask(taskListener, this);
    }

    @Override
    public DelayTask of(String taskId, TaskListener taskListener) {
        return new DebugDelayTask(taskId, taskListener, this);
    }
}