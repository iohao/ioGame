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
package com.iohao.game.bolt.broker.core.aware;

import com.alipay.remoting.RemotingContext;
import com.alipay.remoting.rpc.RpcHandler;
import com.alipay.remoting.rpc.protocol.*;
import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.kit.ExecutorSelectKit;

import java.util.concurrent.ExecutorService;

/**
 * 在 netty 线程中执行任务 aware
 * <pre>
 *     默认会在执行 netty handler 的线程中执行任务，而不使用 bolt 的线程执行器；
 *     因为最终会将任务将由 ioGame 默认的执行器策略中，可减少上下文的切换消耗。
 *
 *     相关源码可阅读 DefaultRequestMessageClientProcessorHook
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-12-20
 * @see ExecutorSelectKit#processLogic(BarSkeleton, FlowContext)
 */
public interface UserProcessorInNettyThreadAware {
    /**
     * 消费业务请求的线程
     * <pre>
     *     默认使用执行 netty handler 的线程中执行业务
     *
     *     相关源码
     *     {@link RpcHandler}
     *     {@link RpcRequestProcessor#process(RemotingContext, RpcRequestCommand, ExecutorService)}
     *
     * </pre>
     *
     * @param inNettyThread true 表示在 netty 中执行业务
     */
    void setInNettyThread(boolean inNettyThread);

    /**
     * 是否在执行 netty handler 的线程中执行业务
     *
     * @return true 表示在执行 netty handler 的线程中执行业务
     */
    boolean inNettyThreadExecute();
}
