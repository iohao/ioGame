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
package com.iohao.game.bolt.broker.server.processor.connection;

import com.alipay.remoting.Connection;
import com.alipay.remoting.ConnectionEventProcessor;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.aware.BrokerServerAware;
import com.iohao.game.bolt.broker.server.balanced.BalancedManager;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientProxy;
import com.iohao.game.bolt.broker.server.kit.BrokerPrintKit;
import com.iohao.game.common.kit.log.IoGameLoggerFactory;
import lombok.Setter;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 渔民小镇
 * @date 2022-05-14
 */
public class CloseConnectionEventBrokerProcessor implements ConnectionEventProcessor, BrokerServerAware {
    static final Logger log = IoGameLoggerFactory.getLoggerConnection();

    private final AtomicInteger disConnectTimes = new AtomicInteger();
    private final AtomicBoolean dicConnected = new AtomicBoolean();
    @Setter
    BrokerServer brokerServer;

    @Override
    public void onEvent(String remoteAddress, Connection conn) {

        Objects.requireNonNull(conn);
        dicConnected.set(true);
        disConnectTimes.incrementAndGet();

        BalancedManager balancedManager = this.brokerServer.getBalancedManager();
        BrokerClientProxy brokerClientProxy = balancedManager.remove(remoteAddress);
        BrokerPrintKit.print(this.brokerServer);

        if (IoGameGlobalConfig.openLog) {
            log.info("连接关闭 remoteAddress 【{}】 brokerClientProxy : 【{}】", remoteAddress, brokerClientProxy);
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
