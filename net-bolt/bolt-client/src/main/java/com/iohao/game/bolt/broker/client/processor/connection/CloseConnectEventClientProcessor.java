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
package com.iohao.game.bolt.broker.client.processor.connection;

import com.alipay.remoting.Connection;
import com.alipay.remoting.ConnectionEventProcessor;
import com.iohao.game.bolt.broker.core.aware.BrokerClientItemAware;
import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.client.BrokerClientItem;
import com.iohao.game.bolt.broker.core.client.BrokerClientManager;
import com.iohao.game.bolt.broker.core.common.BrokerGlobalConfig;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 渔民小镇
 * @date 2022-05-14
 */
@Slf4j
public class CloseConnectEventClientProcessor implements ConnectionEventProcessor, BrokerClientItemAware {

    private final AtomicBoolean dicConnected = new AtomicBoolean();
    private final AtomicInteger disConnectTimes = new AtomicInteger();
    @Setter
    BrokerClientItem brokerClientItem;

    @Override
    public void onEvent(String remoteAddress, Connection conn) {
        Assert.assertNotNull(conn);
        dicConnected.set(true);
        disConnectTimes.incrementAndGet();

        if (BrokerGlobalConfig.openLog) {
            log.info("网关断开 remoteAddress : {}", remoteAddress);
        }

        //  这里要断开与 broker （游戏网关）的连接
        BrokerClient brokerClient = brokerClientItem.getBrokerClient();
        BrokerClientManager brokerClientManager = brokerClient.getBrokerClientManager();

        if (BrokerGlobalConfig.openLog) {
            log.info("brokerClientItems : {}", brokerClientManager.countActiveItem());
        }

        //  在集群时，需要移除与当前 broker （游戏网关）连接的 brokerClientItem
        brokerClientManager.remove(brokerClientItem);

        if (BrokerGlobalConfig.openLog) {
            log.info("brokerClientItems : {}", brokerClientManager.countActiveItem());
        }
    }

    public boolean isDisConnected() {
        return this.dicConnected.get();
    }

    public int getDisConnectTimes() {
        return this.disConnectTimes.get();
    }

    public void reset() {
        this.disConnectTimes.set(0);
        this.dicConnected.set(false);
    }

}
