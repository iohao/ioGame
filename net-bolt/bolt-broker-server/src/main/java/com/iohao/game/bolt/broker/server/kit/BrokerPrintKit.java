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
package com.iohao.game.bolt.broker.server.kit;

import com.alibaba.fastjson2.JSONObject;
import com.iohao.game.bolt.broker.core.common.BrokerGlobalConfig;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.balanced.BalancedManager;
import com.iohao.game.common.kit.JsonKit;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

/**
 * @author 渔民小镇
 * @date 2022-05-12
 */
@Slf4j
@UtilityClass
public class BrokerPrintKit {
    public void print(BrokerServer brokerServer) {
        if (!BrokerGlobalConfig.openLog) {
            return;
        }

        BalancedManager balancedManager = brokerServer.getBalancedManager();

        // 游戏逻辑服信息
        var collect = balancedManager
                .getLogicBalanced()
                .listBrokerClientRegion()
                .stream()
                .map(brokerClientRegion -> {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("tag", brokerClientRegion.getTag());
                    jsonObject.put("服务器数量", brokerClientRegion.count());
                    return JsonKit.toJsonPretty(jsonObject);
                })
                .collect(Collectors.toList());

        // 对外服信息
        int externalCount = balancedManager.getExternalLoadBalanced().count();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("tag", "对外服");
        jsonObject.put("服务器数量", externalCount);
        collect.add(JsonKit.toJsonPretty(jsonObject));

        int port = brokerServer.getPort();

        log.info("当前网关【{}】与逻辑服相关信息: \n{}", port, collect);
    }
}
