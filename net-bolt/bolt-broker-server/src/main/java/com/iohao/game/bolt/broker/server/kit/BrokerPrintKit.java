/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
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
package com.iohao.game.bolt.broker.server.kit;

import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.balanced.BalancedManager;
import com.iohao.game.common.kit.log.IoGameLoggerFactory;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;

import java.util.stream.Collectors;

/**
 * @author 渔民小镇
 * @date 2022-05-12
 */
@UtilityClass
public class BrokerPrintKit {
    private static final Logger log = IoGameLoggerFactory.getLoggerCommonStdout();

    public void print(BrokerServer brokerServer) {
        if (!IoGameGlobalConfig.openLog) {
            return;
        }

        BalancedManager balancedManager = brokerServer.getBalancedManager();

        // 游戏逻辑服信息
        var collect = balancedManager
                .getLogicBalanced()
                .listBrokerClientRegion()
                .stream()
                .map(brokerClientRegion -> {

                    String tag = brokerClientRegion.getTag();
                    int count = brokerClientRegion.count();

                    return new BrokerClientNodeInfo(tag, count);
                })
                .collect(Collectors.toList());

        // 对外服信息
        int externalCount = balancedManager.getExternalLoadBalanced().count();
        BrokerClientNodeInfo externalNodeInfo = new BrokerClientNodeInfo("对外服", externalCount);
        collect.add(externalNodeInfo);

        String info = collect.stream()
                .map(BrokerClientNodeInfo::toString)
                .collect(Collectors.joining("\n\t", "\n\t", ""));

        int port = brokerServer.getPort();

        log.info("当前网关【{}】与逻辑服相关信息: {}", port, info);
    }

    private record BrokerClientNodeInfo(String tag, int count) {
        @Override
        public String toString() {
            return "{" +
                    "服务器数量:" + count +
                    ", tag:'" + tag + '\'' +
                    '}';
        }

    }
}
