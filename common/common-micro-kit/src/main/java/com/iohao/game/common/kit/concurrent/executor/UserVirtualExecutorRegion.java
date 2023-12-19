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
package com.iohao.game.common.kit.concurrent.executor;

import com.iohao.game.common.kit.ExecutorKit;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

/**
 * 用户虚拟线程执行器
 *
 * @author 渔民小镇
 * @date 2023-12-19
 */
@Slf4j
public final class UserVirtualExecutorRegion extends AbstractThreadExecutorRegion {

    public UserVirtualExecutorRegion(String threadName) {
        super(threadName, UserThreadExecutorRegion.availableProcessors2n());
    }

    /**
     * 根据 userId 获取对应的 Executor
     *
     * @param userId userId
     * @return Executor 任务执行器
     */
    @Override
    public ThreadExecutor getThreadExecutor(long userId) {
        int index = (int) (userId & (this.executorLength - 1));
        return this.threadExecutors[index];
    }

    /**
     * 没有实现，不要使用
     *
     * @param runnable 任务
     */
    @Override
    public void execute(Runnable runnable) {
        throw new RuntimeException("不支持");
    }

    @Override
    protected ExecutorService createExecutorService(String name) {
        return ExecutorKit.newVirtualExecutor(name);
    }

    public static UserVirtualExecutorRegion me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final UserVirtualExecutorRegion ME = new UserVirtualExecutorRegion("UserVirtual");
    }
}
