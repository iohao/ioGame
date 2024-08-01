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
package com.iohao.game.action.skeleton.core.runner;

import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.common.kit.concurrent.TaskKit;
import com.iohao.game.common.kit.exception.ThrowKit;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Runners 管理器
 *
 * @author 渔民小镇
 * @date 2023-04-23
 * @see Runner
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class Runners {
    final List<Runner> runnerList = new CopyOnWriteArrayList<>();
    final AtomicBoolean onStart = new AtomicBoolean(false);
    final AtomicBoolean onStartAfter = new AtomicBoolean(false);

    @Setter
    BarSkeleton barSkeleton;

    public Runners() {
        // 内置一些 runner
        InternalRunner internalRunner = new InternalRunner();
        this.runnerList.addAll(internalRunner.runnerList);
    }

    public void addRunner(Runner runner) {

        if (this.onStart.get()) {
            ThrowKit.ofRuntimeException("运行中，不能添加 Runner 了");
        }

        Objects.requireNonNull(runner);

        this.runnerList.add(runner);
    }

    /** 启动 runner 机制 onStart 方法 */
    public void onStart() {
        if (this.onStart.get()) {
            return;
        }

        if (this.onStart.compareAndSet(false, true)) {
            this.runnerList.forEach(runner -> runner.onStart(this.barSkeleton));
        }
    }

    /** 启动 runner 机制 onStartAfter 方法 */
    public void onStartAfter() {
        if (this.onStartAfter.get()) {
            return;
        }

        if (this.onStartAfter.compareAndSet(false, true)) {
            TaskKit.runOnceSecond(() -> {
                // 延迟 1 秒执行，防止没连接到服务器， 或者将来增加一个注册回调的 Processor，目前先暂时这样
                this.runnerList.forEach(runner -> runner.onStartAfter(this.barSkeleton));
            });
        }
    }

    /**
     * Runners Name
     *
     * @return Runners Name
     */
    public List<String> listRunnerName() {
        return this.runnerList.stream()
                .map(Runner::name)
                .collect(Collectors.toList());
    }
}
