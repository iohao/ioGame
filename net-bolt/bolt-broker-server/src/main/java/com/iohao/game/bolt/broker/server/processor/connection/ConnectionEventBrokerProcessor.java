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
import com.alipay.remoting.exception.RemotingException;
import com.iohao.game.bolt.broker.core.message.RequestBrokerClientModuleMessage;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.aware.BrokerServerAware;
import com.iohao.game.common.kit.log.IoGameLoggerFactory;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * broker 连接 event
 * <pre>
 *     see RequestBrokerClientModuleMessageClientProcessor.java
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-14
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConnectionEventBrokerProcessor implements ConnectionEventProcessor, BrokerServerAware {
    static final Logger log = IoGameLoggerFactory.getLoggerConnection();

    final AtomicInteger connectTimes = new AtomicInteger();
    final AtomicBoolean connected = new AtomicBoolean();
    Connection connection;
    String remoteAddress;
    final CountDownLatch latch = new CountDownLatch(1);

    @Setter
    BrokerServer brokerServer;

    static final RequestBrokerClientModuleMessage requestBrokerClientModuleMessage = new RequestBrokerClientModuleMessage();

    @Override
    public void onEvent(String remoteAddress, Connection conn) {

        Objects.requireNonNull(remoteAddress);
        doCheckConnection(conn);
        this.remoteAddress = remoteAddress;
        this.connection = conn;
        connected.set(true);
        connectTimes.incrementAndGet();
        latch.countDown();

        //  通知客户端发送模块信息
        try {
            brokerServer.getRpcServer().oneway(conn, requestBrokerClientModuleMessage);
        } catch (RemotingException e) {
            log.error(e.getMessage(), e);
        }

    }

    /**
     * do check connection
     *
     * @param conn
     */
    private void doCheckConnection(Connection conn) {
        Objects.requireNonNull(conn);
        Objects.requireNonNull(conn.getPoolKeys());
        Objects.requireNonNull(conn.getChannel());
        Objects.requireNonNull(conn.getUrl());
        Objects.requireNonNull(conn.getChannel().attr(Connection.CONNECTION).get());
    }

    public boolean isConnected() throws InterruptedException {
        latch.await();
        return this.connected.get();
    }

    public int getConnectTimes() throws InterruptedException {
        latch.await();
        return this.connectTimes.get();
    }

    public Connection getConnection() throws InterruptedException {
        latch.await();
        return this.connection;
    }

    public String getRemoteAddress() throws InterruptedException {
        latch.await();
        return this.remoteAddress;
    }

    public void reset() {
        this.connectTimes.set(0);
        this.connected.set(false);
        this.connection = null;
    }
}
