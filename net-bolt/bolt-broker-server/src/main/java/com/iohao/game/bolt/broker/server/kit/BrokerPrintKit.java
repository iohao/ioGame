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
package com.iohao.game.bolt.broker.server.kit;

import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.balanced.BalancedManager;
import com.iohao.game.common.consts.IoGameLogName;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

/**
 * @author 渔民小镇
 * @date 2022-05-12
 */
@UtilityClass
@Slf4j(topic = IoGameLogName.CommonStdout)
public class BrokerPrintKit {
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
        BrokerClientNodeInfo externalNodeInfo = new BrokerClientNodeInfo("游戏对外服", externalCount);
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
