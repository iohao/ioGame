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

import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.iohao.game.bolt.broker.core.aware.UserProcessorExecutorAware;

import java.util.concurrent.Executor;

/**
 * AsyncUserProcessor 父类
 *
 * @author 渔民小镇
 * @date 2022-11-10
 */
public abstract class AbstractAsyncUserProcessor<T> extends AsyncUserProcessor<T>
        implements UserProcessorExecutorAware {

    Executor userProcessorExecutor;

    @Override
    public Executor getExecutor() {
        return this.userProcessorExecutor;
    }

    @Override
    public Executor getUserProcessorExecutor() {
        return this.userProcessorExecutor;
    }

    @Override
    public void setUserProcessorExecutor(Executor executor) {
        this.userProcessorExecutor = executor;
    }
}
