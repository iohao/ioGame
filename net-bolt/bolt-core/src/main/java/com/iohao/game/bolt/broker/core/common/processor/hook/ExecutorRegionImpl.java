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
package com.iohao.game.bolt.broker.core.common.processor.hook;

import com.iohao.game.action.skeleton.kit.ExecutorRegion;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 业务框架 action 线程 region
 *
 * @author 渔民小镇
 * @date 2023-11-22
 */
record ExecutorRegionImpl(Map<String, Executor> map) implements ExecutorRegion {
    void put(String threadName, Executor executor) {
        this.map.put(threadName, executor);
    }

    public Optional<ThreadPoolExecutor> getThreadPoolExecutorOptional(String threadName) {
        Executor executor = this.map.get(threadName);

        if (executor instanceof ThreadPoolExecutor threadPoolExecutor) {
            return Optional.of(threadPoolExecutor);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Executor> getExecutorOptional(String threadName) {
        return Optional.ofNullable(this.map.get(threadName));
    }

    public int sizeRemaining(String threadName) {
        return getThreadPoolExecutorOptional(threadName)
                .map(threadPoolExecutor -> threadPoolExecutor.getQueue().size())
                .orElse(0);
    }
}
