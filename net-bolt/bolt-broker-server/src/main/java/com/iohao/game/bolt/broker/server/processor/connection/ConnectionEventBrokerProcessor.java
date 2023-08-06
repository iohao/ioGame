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
package com.iohao.game.bolt.broker.server.processor.connection;

import com.alipay.remoting.Connection;
import com.alipay.remoting.ConnectionEventProcessor;
import com.alipay.remoting.ConnectionEventType;
import com.alipay.remoting.exception.RemotingException;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.bolt.broker.core.message.RequestBrokerClientModuleMessage;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.aware.BrokerServerAware;
import com.iohao.game.common.consts.IoGameLogName;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j(topic = IoGameLogName.ConnectionTopic)
public class ConnectionEventBrokerProcessor implements ConnectionEventProcessor, BrokerServerAware {
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
        extractedPrint(remoteAddress, conn);
        Objects.requireNonNull(remoteAddress);
        doCheckConnection(conn);
        this.remoteAddress = remoteAddress;
        this.connection = conn;
        connected.set(true);
        connectTimes.incrementAndGet();
        latch.countDown();

        int withNo = brokerServer.getWithNo();
        requestBrokerClientModuleMessage.setWithNo(withNo);

        //  通知客户端发送模块信息
        try {
            brokerServer.getRpcServer().oneway(conn, requestBrokerClientModuleMessage);
        } catch (RemotingException e) {
            log.error(e.getMessage(), e);
        }
    }

    private static void extractedPrint(String remoteAddress, Connection conn) {
        if (IoGameGlobalConfig.openLog) {
            log.info("Broker ConnectionEventType:【{}】 remoteAddress:【{}】，Connection:【{}】",
                    ConnectionEventType.CONNECT, remoteAddress, conn
            );
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
