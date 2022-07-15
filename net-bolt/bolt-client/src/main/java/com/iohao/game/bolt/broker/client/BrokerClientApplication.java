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
package com.iohao.game.bolt.broker.client;

import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.client.BrokerClientBuilder;
import lombok.experimental.UtilityClass;

/**
 * BoltBrokerClient 构建与启动
 *
 * @author 渔民小镇
 * @date 2022-05-14
 */
@UtilityClass
public class BrokerClientApplication {

    /**
     * 构建并启动 BoltBrokerClient
     *
     * @param brokerClientStartup brokerClientStartup
     * @return BoltBrokerClient
     */
    public BrokerClient start(AbstractBrokerClientStartup brokerClientStartup) {

        BrokerClientBuilder brokerClientBuilder = brokerClientStartup.initConfig();

        BrokerClient brokerClient = start(brokerClientBuilder);

        brokerClientStartup.startupSuccess(brokerClient);

        return brokerClient;
    }

    public BrokerClient start(BrokerClientBuilder builder) {
        BrokerClient brokerClient = builder.build();
        brokerClient.init();
        return brokerClient;
    }

    public BrokerClientBuilder initConfig(AbstractBrokerClientStartup brokerClientStartup) {
        return brokerClientStartup.initConfig();
    }
}
