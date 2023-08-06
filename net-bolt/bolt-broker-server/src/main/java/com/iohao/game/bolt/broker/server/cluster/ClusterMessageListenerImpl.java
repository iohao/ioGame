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
        balancedManager
                .getLogicBalanced()
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
