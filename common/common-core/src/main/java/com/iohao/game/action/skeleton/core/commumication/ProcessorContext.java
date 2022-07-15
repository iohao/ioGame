/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
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
package com.iohao.game.action.skeleton.core.commumication;

import com.alipay.remoting.Url;
import com.alipay.remoting.rpc.RpcClient;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.iohao.game.action.skeleton.protocol.processor.ExtRequestMessage;

/**
 * 通讯方式之一 用于各服务器之前的 processor 通信
 * <pre>
 *     各服务器指的是
 *          对外服、游戏网关、游戏逻辑服
 *
 *      用于 bolt 扩展 processor，这个种扩展方式是自由度是最大的
 *      但需要开发者自己编写 {@link AsyncUserProcessor} 来处理这种消息
 *
 *      实际内部是调用的是 {@link RpcClient#oneway(Url, Object)} 方法，所以这个接口是用来区分通讯类型
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-28
 */
public interface ProcessorContext {
    /**
     * oneway 异步调用
     *
     * @param message message
     */
    void invokeOneway(Object message);

    /**
     * oneway 异步调用
     *
     * @param message message
     */
    void invokeOneway(ExtRequestMessage message);


}
