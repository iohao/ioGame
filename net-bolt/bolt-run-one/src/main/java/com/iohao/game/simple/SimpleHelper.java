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

import com.iohao.game.bolt.broker.client.AbstractBrokerClientStartup;
import com.iohao.game.bolt.broker.client.external.ExternalServer;
import com.iohao.game.bolt.broker.client.external.ExternalServerBuilder;
import com.iohao.game.bolt.broker.client.external.bootstrap.ExternalJoinEnum;
import com.iohao.game.bolt.broker.core.client.BrokerAddress;
import com.iohao.game.bolt.broker.core.common.BrokerGlobalConfig;
import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * 简单的快速启动工具： 对外服、网关服
 * <pre>
 *     注意：
 *          这个工具只适合单机的开发或本地一体化的开发, 对于分步式不适合。
 *
 *          当然如果打算开发单体应用，这种方式是很合适的。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-02-24
 */
@UtilityClass
public class SimpleHelper {

    /**
     * 简单的快速启动
     * <pre>
     *     快速启动:
     *          对外服 websocket 方式连接
     *          网关服 默认端口 10200
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

    /**
     * 简单的快速启动
     * <pre>
     *     快速启动:
     *          对外服 tcp 方式连接
     *          网关服 默认端口 10200
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
     * 对外服 示例
     * <pre>
     *     用于演示
     * </pre>
     *
     * @param externalJoinEnum 连接方式
     * @param externalPort     游戏对外服端口
     * @return 对外服
     */
    public ExternalServer createExternalServer(ExternalJoinEnum externalJoinEnum, int externalPort) {

        // 游戏对外服 - 构建器
        ExternalServerBuilder builder = ExternalServer.newBuilder(externalPort)
                // 连接方式
                .externalJoinEnum(externalJoinEnum)
                // Broker （游戏网关）的连接地址
                .brokerAddress(new BrokerAddress("127.0.0.1", BrokerGlobalConfig.brokerPort));

        return builder.build();
    }

    private void runInternal(int externalPort, List<AbstractBrokerClientStartup> logicList, ExternalJoinEnum externalJoinEnum) {
        // 对外服 tcp 方法的连接
        ExternalServer externalServer = createExternalServer(externalJoinEnum, externalPort);

        // 简单的启动器
        new SimpleRunOne()
                // 游戏对外服
                .setExternalServer(externalServer)
                // 游戏逻辑服列表
                .setLogicServerList(logicList)
                // 启动 游戏对外服、游戏网关服、游戏逻辑服
                .startup();
    }
}
