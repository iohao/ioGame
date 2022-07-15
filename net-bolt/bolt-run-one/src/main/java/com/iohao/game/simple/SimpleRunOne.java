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
package com.iohao.game.simple;

import com.iohao.game.action.skeleton.core.doc.BarSkeletonDoc;
import com.iohao.game.bolt.broker.client.AbstractBrokerClientStartup;
import com.iohao.game.bolt.broker.client.BrokerClientApplication;
import com.iohao.game.bolt.broker.client.external.ExternalServer;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.BrokerServerBuilder;
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
 * 简单的启动器： 对外服、网关服、逻辑服
 * 谐音:拳皇98中的 round one ready go!
 * <pre>
 *     注意：
 *          这个工具只适合单机的开发或本地一体化的开发, 对于分步式不适合。
 *
 *          当然如果打算开发单体应用，这种方式是很合适的。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-02-28
 */
@Slf4j
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SimpleRunOne {
    final ExecutorService executorService = ExecutorKit.newCacheThreadPool(SimpleRunOne.class.toString());

    /** 对外服 */
    ExternalServer externalServer;
    /** 逻辑服 */
    List<AbstractBrokerClientStartup> logicServerList;

    /** broker 游戏网关 */
    BrokerServer brokerServer;
    /** broker 游戏网关 构建器 */
    BrokerServerBuilder brokerServerBuilder = BrokerServer.newBuilder();
    /** true 在本地启动 broker （游戏网关） */
    boolean runBrokerServer = true;

    /**
     * 简单的快速启动
     * <pre>
     *     快速启动:
     *          对外服
     *          网关服
     *          逻辑服
     * </pre>
     */
    public void startup() {
        // 启动网关
        if (this.runBrokerServer) {
            this.brokerServer = brokerServerBuilder.build();
            this.executorService.execute(this.brokerServer::startup);
        }

        // 启动逻辑服、对外服
        this.startupLogic();
    }

    /**
     * 禁用 broker （游戏网关）
     * <pre>
     *     本地不启动游戏网关
     *     团队开发中可以把 broker （游戏网关）单独部署在一台机器上
     *     而本机启动的逻辑服连接到这台单独部署的游戏网关上，这样就可以共用 broker （游戏网关），
     *     不用每次在本机启动，这样调试起来也方便
     * </pre>
     *
     * @return this
     */
    public SimpleRunOne disableBrokerServer() {
        this.runBrokerServer = false;
        return this;
    }

    private void startupLogic() {
        this.executorService.execute(() -> {
            // 启动逻辑服
            if (Objects.nonNull(this.logicServerList)) {
                this.logicServerList.forEach(BrokerClientApplication::start);
            }

            // 启动游戏对外服
            if (Objects.nonNull(this.externalServer)) {
                this.externalServer.startup();
            }
        });

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }

        // 生成游戏文档
        this.executorService.execute(BarSkeletonDoc.me()::buildDoc);
    }

}
