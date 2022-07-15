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
package com.iohao.game.bolt.broker.server.cluster;

import com.alipay.remoting.exception.RemotingException;
import com.iohao.game.bolt.broker.cluster.ClusterMessageListener;
import com.iohao.game.bolt.broker.core.message.BrokerClusterMessage;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.balanced.BalancedManager;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientProxy;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientRegion;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 集群消息通知
 * <pre>
 *     发送集群信息给客户端（这里指的是逻辑服：对外服和游戏逻辑服）
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-15
 */
@Slf4j
@Setter
public class ClusterMessageListenerImpl implements ClusterMessageListener {

    BrokerServer brokerServer;

    @Override
    public void inform(BrokerClusterMessage brokerClusterMessage) {
        // 把集群消息发送给所有 游戏逻辑服
        extractedLogic(brokerClusterMessage);

        // 把集群消息发送给所有 游戏对外服
        extractedExternal(brokerClusterMessage);
    }

    private void extractedLogic(BrokerClusterMessage brokerClusterMessage) {
        BalancedManager balancedManager = brokerServer.getBalancedManager();
        // 得到逻辑服负载器
        balancedManager.getLogicBalanced()
                // 得到逻辑服域 BrokerClientRegion
                .listBrokerClientRegion()
                .stream()
                // 得到 BrokerClientRegion 下的所有逻辑服
                .flatMap((Function<BrokerClientRegion, Stream<BrokerClientProxy>>) brokerClientRegion -> brokerClientRegion.listBrokerClientProxy().stream())
                // 给游戏逻辑服发送集群消息
                .forEach(brokerClientProxy -> {
                    try {
                        brokerClientProxy.oneway(brokerClusterMessage);
                    } catch (RemotingException | InterruptedException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void extractedExternal(BrokerClusterMessage brokerClusterMessage) {
        BalancedManager balancedManager = brokerServer.getBalancedManager();
        // 对外服列表
        balancedManager
                .getExternalLoadBalanced()
                .listBrokerClientProxy()
                .forEach(brokerClientProxy -> {
                    try {
                        brokerClientProxy.oneway(brokerClusterMessage);
                    } catch (RemotingException | InterruptedException e) {
                        log.error(e.getMessage(), e);
                    }
                });
    }
}
