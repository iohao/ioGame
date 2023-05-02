/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
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
