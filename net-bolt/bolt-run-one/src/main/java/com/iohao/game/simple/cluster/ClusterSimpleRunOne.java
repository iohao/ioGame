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

import com.iohao.game.action.skeleton.core.doc.BarSkeletonDoc;
import com.iohao.game.bolt.broker.client.AbstractBrokerClientStartup;
import com.iohao.game.bolt.broker.client.BrokerClientApplication;
import com.iohao.game.bolt.broker.client.external.ExternalServer;
import com.iohao.game.bolt.broker.core.common.BrokerGlobalConfig;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.common.kit.ExecutorKit;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * （集群相关的）集群简单的启动器： 对外服、游戏网关（3个节点）、逻辑服
 * 谐音:拳皇98中的 round one ready go!
 * <pre>
 *     注意：
 *          这个工具只适合单机的开发或本地一体化的开发, 对于分步式不适合。
 *
 * </pre>
 * 集群介绍
 * <pre>
 *     格式： ip:port
 *
 *     -- 生产环境的建议 --
 *     注意，在生产上建议一台物理机配置一个 broker （游戏网关）
 *     一个 broker 就是一个节点
 *     比如配置三台机器，端口可以使用同样的端口，假设三台机器的 ip 分别是:
 *     192.168.1.10:30056
 *     192.168.1.11:30056
 *     192.168.1.12:30056
 *
 *     -- 为了方便演示 --
 *     这里配置写死是方便在一台机器上启动集群
 *     但是同一台机器启动多个 broker 来实现集群就要使用不同的端口，因为《端口被占用，不能相同》
 *     所以这里的配置是：
 *     127.0.0.1:30056
 *     127.0.0.1:30057
 *     127.0.0.1:30058
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-15
 */
@Slf4j
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClusterSimpleRunOne {
    final ExecutorService executorService = ExecutorKit.newCacheThreadPool(ClusterSimpleRunOne.class.toString());

    /** 对外服 */
    ExternalServer externalServer;
    /** 逻辑服 */
    List<AbstractBrokerClientStartup> logicServerList;
    /** true 在本地启动 broker （游戏网关）集群 */
    boolean runBrokerServerCluster = true;

    /**
     * 简单的快速启动
     * <pre>
     *     快速启动:
     *          对外服
     *          游戏网关集群
     *          逻辑服
     *
     *      注意1：
     *          方法会启动 3 个游戏网关来演示集群，端口分别是：30056、30057、30058
     *
     *      注意2：
     *          因为 broker （游戏网关） 集群是无中心节点的，所以逻辑服可以选择与任意一台网关建立连接，
     *          逻辑服内部会自动的与集群其他节点建立连接
     * </pre>
     */
    public void startup() {

        // 启动网关集群（3个节点）
        if (this.runBrokerServerCluster) {
            this.clusterBrokerServer();
        }

        try {
            // 暂停 0.5 秒，让本地网关集群先启动完成
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }

        // 启动逻辑服、对外服
        startupLogic();
    }

    /**
     * 禁用 broker （游戏网关）集群
     * <pre>
     *     本地不启动游戏网关集群
     *     如果公司团队开发中，可以把 broker （游戏网关）集群，部署在其他机器上
     *     而本机启动的逻辑服连接到这些游戏网关集群上，这样就可以共用游戏网关集群，不用每次在本机启动集群
     *     这样调试起来也方便
     * </pre>
     *
     * @return this
     */
    public ClusterSimpleRunOne disableBrokerServerCluster() {
        this.runBrokerServerCluster = false;
        return this;
    }

    private void startupLogic() {
        executorService.execute(() -> {
            // 启动逻辑服
            if (Objects.nonNull(this.logicServerList)) {
                logicServerList.forEach(BrokerClientApplication::start);
                log.info("启动逻辑服 : {}", this.logicServerList);
            }

            // 启动游戏对外服
            if (Objects.nonNull(this.externalServer)) {
                externalServer.startup();
            }
        });

        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }

        // 生成游戏文档
        executorService.execute(BarSkeletonDoc.me()::buildDoc);
    }

    private void clusterBrokerServer() {

        /*
         * 种子节点地址
         * <pre>
         *     格式： ip:port
         *
         *     -- 生产环境的建议 --
         *     注意，在生产上建议一台物理机配置一个 broker （游戏网关）
         *     一个 broker 就是一个节点
         *     比如配置三台机器，端口可以使用同样的端口，假设三台机器的 ip 分别是:
         *     192.168.1.10:30056
         *     192.168.1.11:30056
         *     192.168.1.12:30056
         *
         *     -- 为了方便演示 --
         *     这里配置写死是方便在一台机器上启动集群
         *     但是同一台机器启动多个 broker 来实现集群就要使用不同的端口，因为《端口被占用，不能相同》
         *     所以这里的配置是：
         *     127.0.0.1:30056
         *     127.0.0.1:30057
         *     127.0.0.1:30058
         * </pre>
         */
        List<String> seedAddress = List.of(
                "127.0.0.1:30056",
                "127.0.0.1:30057",
                "127.0.0.1:30058"
        );

        // Gossip listen port 监听端口
        int gossipListenPort = BrokerGlobalConfig.gossipListenPort;
        // broker 端口（游戏网关端口）
        int port = BrokerGlobalConfig.brokerPort;
        // ---- 第1台 broker ----
        this.createBrokerServer(seedAddress, gossipListenPort, port);

        // Gossip listen port 监听端口
        gossipListenPort = 30057;
        // broker 端口（游戏网关端口）
        port = 10201;
        //  ---- 第2台 broker ----
        this.createBrokerServer(seedAddress, gossipListenPort, port);

        // Gossip listen port 监听端口
        gossipListenPort = 30058;
        // broker 端口（游戏网关端口）
        port = 10202;
        //  ---- 第3台 broker ----
        this.createBrokerServer(seedAddress, gossipListenPort, port);
    }

    private void createBrokerServer(List<String> seedAddress, int gossipListenPort, int port) {
        BrokerServer brokerServer = ClusterSimpleHelper.createBrokerServer(seedAddress, gossipListenPort, port);

        // 启动游戏网关
        executorService.execute(brokerServer::startup);
    }
}
