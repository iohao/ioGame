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
package com.iohao.game.bolt.broker.core.common;

import com.iohao.game.bolt.broker.core.aware.UserProcessorExecutorAware;

import java.util.concurrent.Executor;

/**
 * 主要用于给 UserProcessor 构建 Executor 的策略
 * <pre>
 *     框架会在启动时，如果检测到 UserProcessor 实现了 {@link UserProcessorExecutorAware} 接口，就会触发一次
 *
 *     通过该接口，开发者可以给 UserProcessor 配置 Executor；
 *
 *     开发者可以根据自身业务来做定制
 *     see {@link IoGameGlobalConfig#userProcessorExecutorStrategy}
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-11-11
 * @see DefaultUserProcessorExecutorStrategy
 * @see VirtualThreadUserProcessorExecutorStrategy
 */
public interface UserProcessorExecutorStrategy {
    /**
     * 通过 userProcessorExecutorAware 来得到 Executor
     * <pre>
     *     通常用于给 UserProcessor 配置线程池，
     *     userProcessorExecutorAware 通常是当前的 UserProcessor 实现类
     * </pre>
     *
     * @param userProcessorExecutorAware 通常是 UserProcessor 实现了 UserProcessorExecutorAware 接口
     * @return Executor
     */
    Executor getExecutor(UserProcessorExecutorAware userProcessorExecutorAware);
}
