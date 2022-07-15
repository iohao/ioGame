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
package com.iohao.game.bolt.broker.client.external.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.iohao.game.bolt.broker.client.external.bootstrap.ExternalKit;
import com.iohao.game.bolt.broker.core.common.BrokerGlobalConfig;
import com.iohao.game.bolt.broker.core.message.BroadcastMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * 接收并处理 来自网关的广播消息
 *
 * @author 渔民小镇
 * @date 2022-01-16
 */
@Slf4j
public class BroadcastMessageExternalProcessor extends AsyncUserProcessor<BroadcastMessage> {
    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, BroadcastMessage message) {
        if (BrokerGlobalConfig.isExternalLog()) {
            log.info("对外服接收网关的数据：{}", message);
        }

        ExternalKit.broadcast(message);
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
        return BroadcastMessage.class.getName();
    }
}
