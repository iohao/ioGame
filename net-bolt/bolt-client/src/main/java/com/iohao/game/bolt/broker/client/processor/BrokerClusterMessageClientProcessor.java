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
import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.aware.BrokerClientAware;
import com.iohao.game.bolt.broker.core.client.BrokerClientManager;
import com.iohao.game.bolt.broker.core.message.BrokerClusterMessage;
import com.iohao.game.bolt.broker.core.message.BrokerMessage;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

/**
 * 集群消息请求处理器
 * <pre>
 *     如果有新的 broker （游戏网关）加入集群，逻辑服会通过这个请求处理器来接收消息
 *     逻辑服（对外服和游戏逻辑服）接收到这个消息后，可根据传入的集群消息来建立连接
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-15
 */
@Slf4j
public class BrokerClusterMessageClientProcessor extends AsyncUserProcessor<BrokerClusterMessage> implements BrokerClientAware {

    @Setter
    BrokerClient brokerClient;

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, BrokerClusterMessage message) {
        if (log.isDebugEnabled()) {
            log.debug("==接收来自网关的集群消息 {} {}", message.getBrokerMessageList().size(), message);
        }

        BrokerClientManager brokerClientManager = brokerClient.getBrokerClientManager();
        Set<String> keySet = brokerClientManager.keySet();

        List<BrokerMessage> brokerMessageList = message.getBrokerMessageList();

        // 新增网关
        for (BrokerMessage brokerMessage : brokerMessageList) {
            String address = brokerMessage.getAddress();

            keySet.remove(address);

            boolean contains = brokerClientManager.contains(address);

            // 如果 BrokerClientManager 没有这个集群的地址，说明是新增的机器
            if (!contains) {
                log.debug("集群有新的机器 address : {}", address);
                // 需要新增连接
                brokerClientManager.register(address);
            }
        }

        // 多出来的，要移除；通常是 broker （游戏网关）的机器减少了
        for (String address : keySet) {

            if (address.contains("127.0.0.1")) {
                continue;
            }

            brokerClientManager.remove(address);
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
        return BrokerClusterMessage.class.getName();
    }
}
