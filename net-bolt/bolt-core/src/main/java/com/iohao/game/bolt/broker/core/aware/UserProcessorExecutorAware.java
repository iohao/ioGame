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
package com.iohao.game.bolt.broker.core.aware;

import java.util.concurrent.Executor;

/**
 * UserProcessorExecutorAware
 * <pre>
 *
 *     只要 UserProcessor 实现了该接口，框架会调用 setProcessorExecutor 方法并赋值
 *
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-11-10
 */
public interface UserProcessorExecutorAware {
    /**
     * set UserProcessor Executor
     *
     * @param executor Executor
     */
    void setUserProcessorExecutor(Executor executor);
}
