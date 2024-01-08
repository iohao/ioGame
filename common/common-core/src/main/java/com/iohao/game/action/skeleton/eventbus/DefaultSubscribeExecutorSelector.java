/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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
package com.iohao.game.action.skeleton.eventbus;

import com.iohao.game.common.kit.concurrent.executor.SimpleThreadExecutorRegion;
import com.iohao.game.common.kit.concurrent.executor.ThreadExecutor;
import com.iohao.game.common.kit.concurrent.executor.UserThreadExecutorRegion;
import com.iohao.game.common.kit.concurrent.executor.UserVirtualExecutorRegion;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author 渔民小镇
 * @date 2023-12-24
 */
final class DefaultSubscribeExecutorSelector implements SubscribeExecutorSelector {
    static final LongAdder threadIndexAdder = new LongAdder();

    @Override
    public ThreadExecutor select(Subscriber subscriber, EventBusMessage eventBusMessage) {

        EventSubscribe.ExecutorSelector executorSelect = subscriber.getExecutorSelect();

        // 虚拟线程中执行
        if (executorSelect == EventSubscribe.ExecutorSelector.userVirtualExecutor) {
            long threadIndex = getThreadIndex(eventBusMessage);
            return UserVirtualExecutorRegion.me().getThreadExecutor(threadIndex);
        }

        // [线程安全] 用户线程中执行
        if (executorSelect == EventSubscribe.ExecutorSelector.userExecutor) {
            long threadIndex = getThreadIndex(eventBusMessage);
            return UserThreadExecutorRegion.me().getThreadExecutor(threadIndex);
        }

        // [线程安全] 相同的订阅者使用同一个线程执行器
        if (executorSelect == EventSubscribe.ExecutorSelector.methodExecutor) {
            long threadIndex = subscriber.id;
            return SimpleThreadExecutorRegion.me().getThreadExecutor(threadIndex);
        }

        long threadIndex = getThreadIndex(eventBusMessage);
        return SimpleThreadExecutorRegion.me().getThreadExecutor(threadIndex);
    }

    long getThreadIndex(EventBusMessage eventBusMessage) {
        long userId = eventBusMessage.getThreadIndex();

        if (userId != 0) {
            return userId;
        }

        threadIndexAdder.increment();
        return threadIndexAdder.longValue();
    }

    static DefaultSubscribeExecutorSelector me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final DefaultSubscribeExecutorSelector ME = new DefaultSubscribeExecutorSelector();
    }
}
