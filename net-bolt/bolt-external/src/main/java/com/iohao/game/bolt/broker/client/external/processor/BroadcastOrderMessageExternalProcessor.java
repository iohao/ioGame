/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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
package com.iohao.game.bolt.broker.client.external.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.iohao.game.bolt.broker.client.external.bootstrap.ExternalKit;
import com.iohao.game.bolt.broker.core.message.BroadcastOrderMessage;
import com.iohao.game.common.kit.ExecutorKit;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * 接收并处理 来自网关的广播消息 - 顺序的
 *
 * @author 渔民小镇
 * @date 2022-07-14
 */
public class BroadcastOrderMessageExternalProcessor extends AsyncUserProcessor<BroadcastOrderMessage> {
    final ExecutorService executorService = ExecutorKit.newSingleThreadExecutor("BroadcastOrderExternal");

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, BroadcastOrderMessage message) {
        ExternalKit.broadcast(message);
    }

    @Override
    public Executor getExecutor() {
        return executorService;
    }

    /**
     * 指定感兴趣的请求数据类型，该 UserProcessor 只对感兴趣的请求类型的数据进行处理；
     * 假设 除了需要处理 MyRequest 类型的数据，还要处理 java.lang.String 类型，有两种方式：
     * 1、再提供一个 UserProcessor 实现类，其 interest() 返回 java.lang.String.class.getName()
     * 2、使用 MultiInterestUserProcessor 实现类，可以为一个 UserProcessor 指定 List<String> multiInterest()
     *
     * @return 自定义处理器
     */
    @Override
    public String interest() {
        return BroadcastOrderMessage.class.getName();
    }
}
