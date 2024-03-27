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
    boolean inNettyThread;

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

    @Override
    public void setInNettyThread(boolean inNettyThread) {
        this.inNettyThread = inNettyThread;
    }

    @Override
    public boolean inNettyThreadExecute() {
        return this.inNettyThread;
    }

    @Override
    public boolean processInIOThread() {
        return inNettyThread;
    }
}
