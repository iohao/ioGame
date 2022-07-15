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
package com.iohao.game.bolt.broker.server.processor;

import com.alipay.remoting.exception.RemotingException;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.balanced.BalancedManager;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientProxy;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 把逻辑服的广播转发到对外服
 *
 * @author 渔民小镇
 * @date 2022-05-28
 */
@Slf4j
@UtilityClass
public class BrokerExternalKit {
    public void sendMessageToExternals(BrokerServer brokerServer, Object message) {
        BalancedManager balancedManager = brokerServer.getBalancedManager();
        var externalLoadBalanced = balancedManager.getExternalLoadBalanced();

        try {
            for (BrokerClientProxy brokerClientProxy : externalLoadBalanced.listBrokerClientProxy()) {
                //  转发到对外服务器
                brokerClientProxy.oneway(message);
            }
        } catch (RemotingException | InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
}
