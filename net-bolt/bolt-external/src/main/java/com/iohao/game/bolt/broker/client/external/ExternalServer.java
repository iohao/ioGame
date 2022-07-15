/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License..
 */
package com.iohao.game.bolt.broker.client.external;

import com.iohao.game.bolt.broker.client.BrokerClientApplication;
import com.iohao.game.bolt.broker.client.external.simple.ExternalBrokerClientStartup;
import com.iohao.game.bolt.broker.core.client.BrokerAddress;
import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.common.BrokerGlobalConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * 对外的服务器
 *
 * @author 渔民小镇
 * @date 2022-01-09
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class ExternalServer {
    /** netty 服务器，与真实用户对接 */
    final ServerBootstrap bootstrap;
    /** ip */
    final String ip;
    /** 对外服端口 */
    final int port;
    final ExternalBrokerClientStartup externalBoltBrokerClientStartup;
    /** 设置 broker （游戏网关）连接地址 */
    BrokerAddress brokerAddress;

    ExternalServer(ExternalServerBuilder builder) {
        this.port = builder.port;
        this.ip = builder.ip;
        this.bootstrap = builder.bootstrap;
        this.externalBoltBrokerClientStartup = builder.externalBoltBrokerClientStartup;
        this.brokerAddress = builder.brokerAddress;
    }

    /**
     * 启动对外服
     *
     * @throws InterruptedException e
     */
    private void doStart() throws InterruptedException {
        // channelFuture
        ChannelFuture channelFuture = bootstrap.bind(new InetSocketAddress(ip, port)).sync();

        if (BrokerGlobalConfig.openLog) {
            log.info("external 启动游戏对外服 ! port: {}", port);
        }

        channelFuture.channel().closeFuture().sync();
    }

    private void startupExternalBoltBrokerClient() {

        // 保存与 broker 通信的 client
        var brokerClientBuilder = BrokerClientApplication.initConfig(this.externalBoltBrokerClientStartup);

        // 重新设置 broker 的连接地址，以对外服的为准
        if (Objects.nonNull(this.brokerAddress)) {
            brokerClientBuilder.brokerAddress(this.brokerAddress);
        }

        BrokerClient brokerClient = BrokerClientApplication.start(brokerClientBuilder);
        ExternalHelper.me().brokerClient = brokerClient;

        this.externalBoltBrokerClientStartup.startupSuccess(brokerClient);
    }

    /**
     * 启动对外服
     */
    public void startup() {
        // 启动内部逻辑服, 用于连接 broker （游戏网关）服务器
        this.startupExternalBoltBrokerClient();

        try {
            // 启动对外服
            this.doStart();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void shutdown() {

    }

    public static ExternalServerBuilder newBuilder(int port) {
        return new ExternalServerBuilder(port);
    }

    public static void main(String[] args) {
        int port = 22022;

        ExternalServerBuilder builder = ExternalServer.newBuilder(port);

        ExternalServer externalServer = builder.build();

        externalServer.startup();
        System.out.println("OK!");

    }
}
