/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License..
 */
package com.iohao.game.action.skeleton.core.commumication;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.exception.RemotingException;

/**
 * 当前服务器上下文
 * <pre>
 *     see BrokerClientHelper
 * </pre>
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
public interface BrokerClientContext extends AsyncContext {

    /**
     * 发送消息到游戏网关
     *
     * @param request 消息
     * @throws RemotingException e
     */
    void oneway(final Object request) throws RemotingException;

    /**
     * 推送通讯相关 - 得到广播通讯上下文
     *
     * @return 广播通讯上下文
     */
    BroadcastContext getBroadcastContext();

    /**
     * 推送通讯相关 - 得到顺序的 - 广播通讯上下文
     *
     * @return 顺序的 - 广播通讯上下文
     */
    BroadcastOrderContext getBroadcastOrderContext();

    /**
     * 得到 processor 上下文
     *
     * @return processor 上下文
     */
    ProcessorContext getProcessorContext();

    /**
     * 逻辑服间的相互通信相关 - 得到内部模块通讯上下文
     *
     * @return 内部模块通讯上下文
     */
    InvokeModuleContext getInvokeModuleContext();
}
