/*
 * ioGame
 * Copyright (C) 2021 - 2024  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

/**
 * ExecutorRegion 工具类，起到类似代理的作用。
 * <pre>
 *     即使同进程启动了多个逻辑服，也会共享线程相关资源，从而避免创建过多的线程。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2024-01-11
 * @see UserThreadExecutorRegion 用户线程执行器管理域
 * @see UserVirtualThreadExecutorRegion 用户虚拟线程执行器
 * @see SimpleThreadExecutorRegion 简单的线程执行器管理域
 * @see ExecutorRegion 线程执行器 region
 */
@UtilityClass
public class ExecutorRegionKit {
    @Setter
    @Getter
    ExecutorRegion executorRegion = new ExecutorRegion() {
    };

    /**
     * user 线程执行器管理域
     *
     * @param index index
     * @return user 线程执行器管理域
     */
    public ThreadExecutor getUserThreadExecutor(long index) {
        return executorRegion.getUserThreadExecutor(index);
    }

    /**
     * 用户虚拟线程执行器
     *
     * @param index index
     * @return 用户虚拟线程执行器
     */
    public ThreadExecutor getUserVirtualThreadExecutor(long index) {
        return executorRegion.getUserVirtualThreadExecutor(index);
    }

    /**
     * 简单的线程执行器管理域
     *
     * @param index index
     * @return 简单的线程执行器管理域
     */
    public ThreadExecutor getSimpleThreadExecutor(long index) {
        return executorRegion.getSimpleThreadExecutor(index);
    }
}
