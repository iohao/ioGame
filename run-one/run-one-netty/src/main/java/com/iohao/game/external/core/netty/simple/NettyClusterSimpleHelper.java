/*
 * ioGame
 * Copyright (C) 2021 - present  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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
package com.iohao.game.external.core.netty.simple;

import com.iohao.game.bolt.broker.client.AbstractBrokerClientStartup;
import com.iohao.game.bolt.broker.cluster.BrokerCluster;
import com.iohao.game.bolt.broker.cluster.BrokerClusterManagerBuilder;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.BrokerServerBuilder;
import com.iohao.game.external.core.ExternalServer;
import com.iohao.game.external.core.config.ExternalJoinEnum;
import com.iohao.game.external.core.netty.kit.ExternalServerCreateKit;
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
 * @date 2023-04-28
 */
@UtilityClass
public class NettyClusterSimpleHelper {


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
        ExternalServer externalServer = ExternalServerCreateKit.createExternalServer(externalPort, externalJoinEnum);

        // 集群简单的启动器
        new NettyClusterSimpleRunOne()
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
