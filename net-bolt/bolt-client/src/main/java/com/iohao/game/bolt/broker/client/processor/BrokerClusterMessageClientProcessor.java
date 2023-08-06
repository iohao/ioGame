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
package com.iohao.game.bolt.broker.client.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.iohao.game.bolt.broker.core.aware.BrokerClientAware;
import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.client.BrokerClientManager;
import com.iohao.game.bolt.broker.core.message.BrokerClusterMessage;
import com.iohao.game.bolt.broker.core.message.BrokerMessage;
import com.iohao.game.common.consts.IoGameLogName;
import com.iohao.game.common.kit.ExecutorKit;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

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
@Slf4j(topic = IoGameLogName.ClusterTopic)
public class BrokerClusterMessageClientProcessor extends AsyncUserProcessor<BrokerClusterMessage>
        implements BrokerClientAware {
    final ExecutorService executorService = ExecutorKit.newSingleThreadExecutor("BrokerClusterMessageClientProcessor");

    @Setter
    BrokerClient brokerClient;

    @Override
    public Executor getExecutor() {
        return executorService;
    }

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

            // 如果 BrokerClientManager 有这个集群的地址，说明机器已经存在了
            if (brokerClientManager.contains(address)) {
                continue;
            }

            log.debug("集群有新的机器 address : {}", address);
            brokerClientManager.register(address);
        }

        // 多出来的，要移除；通常是 broker （游戏网关）的机器减少了
        for (String address : keySet) {

            if (address.contains("127.0.0.1")) {
                continue;
            }

            brokerClientManager.remove(address);
        }
    }

    @Override
    public String interest() {
        return BrokerClusterMessage.class.getName();
    }
}
