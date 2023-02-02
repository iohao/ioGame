/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iohao.game.bolt.broker.core.common;

import com.iohao.game.bolt.broker.core.aware.UserProcessorExecutorAware;

import java.util.concurrent.Executor;

/**
 * 主要用于给 UserProcessor 构建 Executor 的策略
 * <pre>
 *     框架会在启动时，
 *     如果检测到 UserProcessor 实现了 UserProcessorExecutorAware 接口，就会触发一次
 *
 *     通过该接口，开发者可以给 UserProcessor 配置 Executor；
 *
 *     开发者可以根据自身业务来做定制
 *     see {@link IoGameGlobalConfig#userProcessorExecutorStrategy}
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-11-11
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
