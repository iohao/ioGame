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
package com.iohao.game.common.kit.concurrent.executor;

/**
 * 执行器管理域，管理着 {@link ThreadExecutorRegion} （线程执行器管理域）的实现类
 * <pre>
 *     管理着 {@link UserThreadExecutorRegion}、{@link UserVirtualThreadExecutorRegion}、{@link SimpleThreadExecutorRegion}。
 *     即使同进程启动了多个逻辑服，也会共享线程相关资源，从而避免创建过多的线程。
 *
 *     {@link UserThreadExecutorRegion} - 用户线程执行器管理域
 *     该执行器主要用于消费 action 业务，或者说消费玩家相关的业务。
 *     通过 userId 来得到对应的 ThreadExecutor 执行业务，从而避免并发问题。
 *
 *     {@link UserVirtualThreadExecutorRegion} - 用户虚拟线程执行器
 *     该执行器主要用于消费 io 的相关业务（如 DB 入库）。
 *
 *     {@link SimpleThreadExecutorRegion} - 简单的线程执行器管理域
 *     该执行器与 {@link UserThreadExecutorRegion} 类似。
 *     可通过 index 来得到对应的 ThreadExecutor 执行业务，从而避免并发问题。
 *     如果业务是计算密集型的，又不想占用 {@link UserThreadExecutorRegion} 线程资源时，可使用该执行器。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2024-01-11
 * @see UserThreadExecutorRegion 用户线程执行器管理域
 * @see UserVirtualThreadExecutorRegion 用户虚拟线程执行器
 * @see SimpleThreadExecutorRegion 简单的线程执行器管理域
 */
public interface ExecutorRegion {
    /**
     * user 线程执行器管理域
     *
     * @return user 线程执行器管理域
     */
    ThreadExecutorRegion getUserThreadExecutorRegion();

    /**
     * 用户虚拟线程执行器
     *
     * @return 用户虚拟线程执行器
     */
    default ThreadExecutorRegion getUserVirtualThreadExecutorRegion() {
        return UserVirtualThreadExecutorRegion.me();
    }

    /**
     * 简单的线程执行器管理域
     *
     * @return 简单的线程执行器管理域
     */
    ThreadExecutorRegion getSimpleThreadExecutorRegion();

    /**
     * user 线程执行器管理域
     *
     * @param index index
     * @return user 线程执行器管理域
     */
    default ThreadExecutor getUserThreadExecutor(long index) {
        return this.getUserThreadExecutorRegion().getThreadExecutor(index);
    }

    /**
     * 用户虚拟线程执行器
     *
     * @param index index
     * @return 用户虚拟线程执行器
     */
    default ThreadExecutor getUserVirtualThreadExecutor(long index) {
        return this.getUserVirtualThreadExecutorRegion().getThreadExecutor(index);
    }

    /**
     * 简单的线程执行器管理域
     *
     * @param index index
     * @return 简单的线程执行器管理域
     */
    default ThreadExecutor getSimpleThreadExecutor(long index) {
        return this.getSimpleThreadExecutorRegion().getThreadExecutor(index);
    }
}
