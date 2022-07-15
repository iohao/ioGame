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
import com.iohao.game.bolt.broker.core.client.BrokerClientItem;
import com.iohao.game.bolt.broker.core.aware.BrokerClientItemAware;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author 渔民小镇
 * @date 2022-05-14
 */
@Slf4j
public class ConnectEventClientProcessor implements ConnectionEventProcessor, BrokerClientItemAware {

    private final AtomicBoolean connected = new AtomicBoolean();
    private final AtomicInteger connectTimes = new AtomicInteger();
    private Connection connection;
    private String remoteAddress;
    private final CountDownLatch latch = new CountDownLatch(1);
    static final LongAdder count = new LongAdder();
    @Setter
    BrokerClientItem brokerClientItem;

    @Override
    public void onEvent(String remoteAddress, Connection conn) {
        Assert.assertNotNull(remoteAddress);
        doCheckConnection(conn);
        this.remoteAddress = remoteAddress;
        this.connection = conn;
        connected.set(true);
        connectTimes.incrementAndGet();
        latch.countDown();

        // 设置连接
        brokerClientItem.setConnection(conn);

        log.debug("~~~~~~~~~~~ client connect: {}", latch);
        count.increment();
        log.debug("~~~~~~~~~~~ client count: {}", count);

    }

    /**
     * do check connection
     *
     * @param conn
     */
    private void doCheckConnection(Connection conn) {
        Assert.assertNotNull(conn);
        Assert.assertNotNull(conn.getPoolKeys());
        Assert.assertTrue(conn.getPoolKeys().size() > 0);
        Assert.assertNotNull(conn.getChannel());
        Assert.assertNotNull(conn.getUrl());
        Assert.assertNotNull(conn.getChannel().attr(Connection.CONNECTION).get());
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
