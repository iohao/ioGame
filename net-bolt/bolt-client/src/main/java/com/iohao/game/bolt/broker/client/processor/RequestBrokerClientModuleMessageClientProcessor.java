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
package com.iohao.game.bolt.broker.client.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.iohao.game.bolt.broker.core.aware.BrokerClientItemAware;
import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.client.BrokerClientItem;
import com.iohao.game.bolt.broker.core.client.BrokerClientManager;
import com.iohao.game.bolt.broker.core.common.BrokerGlobalConfig;
import com.iohao.game.bolt.broker.core.message.RequestBrokerClientModuleMessage;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 收到网关请求模块信息
 * <pre>
 *     see ConnectionEventBrokerProcessor.java
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-16
 */
@Slf4j
public class RequestBrokerClientModuleMessageClientProcessor extends AsyncUserProcessor<RequestBrokerClientModuleMessage> implements BrokerClientItemAware {
    @Setter
    BrokerClientItem brokerClientItem;

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, RequestBrokerClientModuleMessage request) {
        if (BrokerGlobalConfig.requestResponseLog) {
            log.info("bizCtx.getRemoteAddress() : {}", bizCtx.getRemoteAddress());
        }

        // 客户端服务器注册到游戏网关服
        brokerClientItem.registerToBroker();

        if (BrokerGlobalConfig.requestResponseLog) {
            BrokerClient brokerClient = brokerClientItem.getBrokerClient();
            BrokerClientManager brokerClientManager = brokerClient.getBrokerClientManager();
            log.info("brokerClientItems : {}", brokerClientManager.countActiveItem());
        }
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
        return RequestBrokerClientModuleMessage.class.getName();
    }
}
