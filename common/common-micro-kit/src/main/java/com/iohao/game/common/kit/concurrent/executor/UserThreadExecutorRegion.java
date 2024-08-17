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

import com.iohao.game.common.kit.RuntimeKit;

/**
 * 用户线程执行器管理域
 * <pre>
 *     执行器具体数量是不大于 Runtime.getRuntime().availableProcessors() 的 2 次幂。
 *     当 availableProcessors 的值分别为 4、8、12、16、32 时，对应的数量则是 4、8、8、16、32。
 *
 *     4、8、12、16、32 （availableProcessors 的值）
 *     4、8、 8、16、32 （对应的数量）
 * </pre>
 * <pre>
 *     UserThreadExecutorRegion - 用户线程执行器管理域
 *     该执行器主要用于消费 action 业务，或者说消费玩家相关的业务。
 *     通过 userId 来得到对应的 ThreadExecutor 执行业务，从而避免并发问题。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-12-01
 */
final class UserThreadExecutorRegion extends AbstractThreadExecutorRegion {
    final int executorLength;

    UserThreadExecutorRegion() {
        super("User", RuntimeKit.availableProcessors2n);
        this.executorLength = RuntimeKit.availableProcessors2n - 1;
    }

    /**
     * 根据 userId 获取对应的 Executor
     *
     * @param userId userId
     * @return Executor 任务执行器
     */
    @Override
    public ThreadExecutor getThreadExecutor(long userId) {
        int index = (int) (userId & this.executorLength);
        return this.threadExecutors[index];
    }
}
