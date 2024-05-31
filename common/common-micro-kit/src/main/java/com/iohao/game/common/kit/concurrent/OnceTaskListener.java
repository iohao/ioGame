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
package com.iohao.game.common.kit.concurrent;

import io.netty.util.Timeout;
import io.netty.util.TimerTask;

import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * Timer 监听回调，只执行 1 次。
 * <pre>
 *     当 {@code triggerUpdate } 返回 true 时，才会执行 {@code onUpdate} 方法。
 *
 *     默认情况下 triggerUpdate 返回值为 true，开发者可以通过控制 triggerUpdate 方法的返回值来决定是否执行 onUpdate 方法；
 * </pre>
 * example
 * <pre>{@code
 *     // 只执行一次，500、800 milliseconds 后
 *     TaskKit.runOnce(() -> log.info("500 delayMilliseconds"), 500);
 *     TaskKit.runOnce(() -> log.info("800 delayMilliseconds"), 800);
 *
 *     // 只执行一次，10 秒后执行
 *     TaskKit.runOnce(new YourOnceTaskListener(), 10, TimeUnit.SECONDS);
 *
 *     // 只执行一次，1500 Milliseconds 后执行，当 theTriggerUpdate 为 true 时，才执行 onUpdate
 *     boolean theTriggerUpdate = RandomKit.randomBoolean();
 *     TaskKit.runOnce(new OnceTaskListener() {
 *         @Override
 *         public void onUpdate() {
 *             log.info("1500 delayMilliseconds");
 *         }
 *
 *         @Override
 *         public boolean triggerUpdate() {
 *             return theTriggerUpdate;
 *         }
 *
 *     }, 1500, TimeUnit.MILLISECONDS);
 * }
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-12-06
 * @see TaskKit
 * @see IntervalTaskListener
 * @since 21
 */
public interface OnceTaskListener extends TimerTask, TaskListener {
    @Override
    default void run(Timeout timeout) throws Exception {

        Executor executor = this.getExecutor();

        if (Objects.nonNull(executor)) {
            executor.execute(this::executeFlow);
        } else {
            this.executeFlow();
        }
    }

    private void executeFlow() {
        try {
            if (this.triggerUpdate()) {
                this.onUpdate();
            }
        } catch (Throwable e) {
            this.onException(e);
        }
    }
}
