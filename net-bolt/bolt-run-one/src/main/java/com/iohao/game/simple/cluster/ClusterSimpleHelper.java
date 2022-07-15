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
package com.iohao.game.simple.cluster;

import com.iohao.game.bolt.broker.client.AbstractBrokerClientStartup;
import com.iohao.game.bolt.broker.client.external.ExternalServer;
import com.iohao.game.bolt.broker.client.external.bootstrap.ExternalJoinEnum;
import com.iohao.game.bolt.broker.cluster.BrokerCluster;
import com.iohao.game.bolt.broker.cluster.BrokerClusterManagerBuilder;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.BrokerServerBuilder;
import com.iohao.game.simple.SimpleHelper;
import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * 简单的快速启动工具： 对外服、游戏网关集群(3个节点)、游戏逻辑服
 * <pre>
 *     注意：
 *          这个工具只适合单机的开发或本地一体化的开发，对于生产不适合。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-15
 */
@UtilityClass
public class ClusterSimpleHelper {


    /**
     * 简单的快速启动
     * <pre>
     *     快速启动:
     *          对外服 tcp 方式连接
     *          游戏网关集群
     *          逻辑服
     *
     *      包括游戏业务文档的生成
     * </pre>
     *
     * @param externalPort 游戏对外服端口
     * @param logicList    逻辑服列表
     */
    public void runTcp(int externalPort, List<AbstractBrokerClientStartup> logicList) {
        runInternal(externalPort, logicList, ExternalJoinEnum.TCP);
    }

    /**
     * 简单的快速启动
     * <pre>
     *     快速启动:
     *          对外服 websocket 方式连接
     *          游戏网关集群
     *          逻辑服
     *
     *      包括游戏业务文档的生成
     * </pre>
     *
     * @param externalPort 游戏对外服端口
     * @param logicList    逻辑服列表
     */
    public void run(int externalPort, List<AbstractBrokerClientStartup> logicList) {
        runInternal(externalPort, logicList, ExternalJoinEnum.WEBSOCKET);
    }

    private void runInternal(int externalPort, List<AbstractBrokerClientStartup> logicList, ExternalJoinEnum externalJoinEnum) {
        // 对外服
        ExternalServer externalServer = SimpleHelper.createExternalServer(externalJoinEnum, externalPort);

        // 集群简单的启动器
        new ClusterSimpleRunOne()
                // 对外服
                .setExternalServer(externalServer)
                // 逻辑服列表
                .setLogicServerList(logicList)
                // 启动 对外服、网关、逻辑服
                .startup();
    }

    public BrokerServer createBrokerServer(List<String> seedAddress, int gossipListenPort, int port) {
        // 集群的管理 构建器
        BrokerClusterManagerBuilder brokerClusterManagerBuilder = BrokerCluster.newBrokerClusterManagerBuilder()
                // Gossip listen port 监听端口
                .gossipListenPort(gossipListenPort)
                // 种子节点地址
                .seedAddress(seedAddress);


        // Bolt Broker Server 构建器
        BrokerServerBuilder brokerServerBuilder = BrokerServer.newBuilder()
                // broker 端口（游戏网关端口）
                .port(port)
                // 集群的管理构建器，如果不设置，表示不需要集群
                .brokerClusterManagerBuilder(brokerClusterManagerBuilder);

        // Bolt Broker Server （游戏网关）
        return brokerServerBuilder.build();
    }
}
