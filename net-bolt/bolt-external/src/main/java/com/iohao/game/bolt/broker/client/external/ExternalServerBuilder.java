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

import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;
import com.iohao.game.bolt.broker.client.external.bootstrap.ExternalChannelInitializerCallback;
import com.iohao.game.bolt.broker.client.external.bootstrap.ExternalJoinEnum;
import com.iohao.game.bolt.broker.client.external.bootstrap.ServerBootstrapEventLoopGroupOption;
import com.iohao.game.bolt.broker.client.external.bootstrap.handler.ExternalBizHandler;
import com.iohao.game.bolt.broker.client.external.bootstrap.heart.IdleProcessSetting;
import com.iohao.game.bolt.broker.client.external.bootstrap.initializer.ExternalChannelInitializerCallbackFactory;
import com.iohao.game.bolt.broker.client.external.bootstrap.initializer.ExternalChannelInitializerCallbackOption;
import com.iohao.game.bolt.broker.client.external.bootstrap.initializer.ServerBootstrapSetting;
import com.iohao.game.bolt.broker.client.external.bootstrap.option.ServerBootstrapEventLoopGroupOptionForLinux;
import com.iohao.game.bolt.broker.client.external.bootstrap.option.ServerBootstrapEventLoopGroupOptionForMac;
import com.iohao.game.bolt.broker.client.external.bootstrap.option.ServerBootstrapEventLoopGroupOptionForOther;
import com.iohao.game.bolt.broker.client.external.config.ExternalGlobalConfig;
import com.iohao.game.bolt.broker.client.external.simple.ExternalBrokerClientStartup;
import com.iohao.game.bolt.broker.core.client.BrokerAddress;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 对外服务器 - 构建器
 * <p>
 * 如果不配置默认如下
 * <ul>
 *     <li>serverBootstrapSetting : 根据当前操作系统自动创建</li>
 * </ul>
 *
 * @author 渔民小镇
 * @date 2022-01-09
 */
@Slf4j
@Setter
@Accessors(fluent = true)
public final class ExternalServerBuilder {

    /** 服务器 */
    final ServerBootstrap bootstrap = new ServerBootstrap();
    /** 自定义 - 编排业务 */
    final Map<String, ChannelHandler> channelHandlerProcessors = new LinkedHashMap<>(4);
    /** 构建选项 */
    final ExternalChannelInitializerCallbackOption option = new ExternalChannelInitializerCallbackOption();

    /** ip */
    final String ip;
    /** 对外服端口 */
    final int port;

    /** 连接方式 */
    ExternalJoinEnum externalJoinEnum = ExternalJoinEnum.WEBSOCKET;
    /** 内部逻辑服 连接网关服务器，与网关通信 */
    ExternalBrokerClientStartup externalBoltBrokerClientStartup = new ExternalBrokerClientStartup();
    /**
     * 设置 broker （游戏网关）连接地址
     * <pre>
     *     如果不设置，框架会给个默认值 127.0.0.1:10200
     * </pre>
     */
    BrokerAddress brokerAddress;
    /**
     * setting ServerBootstrap option
     * <pre>
     *     如果不设置，框架会给个默认值
     * </pre>
     */
    ServerBootstrapSetting serverBootstrapSetting;

    ExternalServerBuilder(int port) {
        this.port = port;
        this.ip = new InetSocketAddress(port).getAddress().getHostAddress();
    }

    public ExternalServer build() {

        // 检查
        this.check();

        // 默认值设置
        this.defaultSetting();

        // 自定义 - 编排业务 to option
        ExternalChannelInitializerCallback channelInitializerCallback = this.createExternalChannelInitializerCallback();

        // bootstrap 优化项【对不同的操作系统进行优化 linux、mac、otherOs】
        ServerBootstrapEventLoopGroupOption eventLoopGroupOption = this.createServerBootstrapEventLoopGroupOption();

        this.bootstrap
                // netty 核心组件. (1 连接创建线程组, 2 业务处理线程组)
                .group(eventLoopGroupOption.bossGroup(), eventLoopGroupOption.workerGroup())
                .channel(eventLoopGroupOption.channelClass())
                // 自定义 - 编排业务
                .childHandler((ChannelHandler) channelInitializerCallback);

        // 设置一些 bootstrap 相关配置
        // 开发者如果对 build 中设置的不能满足业务的，可以用 ServerBootstrapSetting 接口来重写
        this.serverBootstrapSetting.optionSetting(this.bootstrap);

        return new ExternalServer(this);
    }

    /**
     * 注册业务 handler
     *
     * @param name      name
     * @param processor handler
     * @return me
     */
    public ExternalServerBuilder registerChannelHandler(String name, ChannelHandler processor) {
        this.channelHandlerProcessors.put(name, processor);
        return this;
    }

    /**
     * 表示请求业务方法需要先登录
     *
     * @return me
     */
    public ExternalServerBuilder enableVerifyIdentity(boolean verifyIdentity) {
        ExternalGlobalConfig.verifyIdentity = verifyIdentity;
        return this;
    }

    /**
     * 开启心跳机制
     *
     * @param idleProcessSetting idleBuilder
     * @return me
     */
    public ExternalServerBuilder enableIdle(IdleProcessSetting idleProcessSetting) {
        this.option.setIdleProcessSetting(idleProcessSetting);
        return this;
    }

    /**
     * 开启心跳机制
     *
     * @return me
     */
    public ExternalServerBuilder enableIdle() {
        return enableIdle(new IdleProcessSetting());
    }

    private ExternalChannelInitializerCallback createExternalChannelInitializerCallback() {
        this.option.setChannelHandlerProcessors(this.channelHandlerProcessors);

        // 自定义 - 编排业务，Channel 初始化的业务编排 (自定义业务编排)
        ExternalChannelInitializerCallback channelInitializerCallback = ExternalChannelInitializerCallbackFactory.me()
                .createExternalChannelInitializerCallback(this.externalJoinEnum)
                .setOption(this.option);

        // bootstrap option
        if (Objects.isNull(this.serverBootstrapSetting)) {
            this.serverBootstrapSetting = channelInitializerCallback.createServerBootstrapSetting();
        }

        return channelInitializerCallback;
    }

    private ServerBootstrapEventLoopGroupOption createServerBootstrapEventLoopGroupOption() {
        OsInfo osInfo = SystemUtil.getOsInfo();

        // 根据系统内核来优化
        if (osInfo.isLinux()) {
            // linux
            return new ServerBootstrapEventLoopGroupOptionForLinux();
        } else if (osInfo.isMac() || osInfo.isMacOsX()) {
            // mac
            return new ServerBootstrapEventLoopGroupOptionForMac();
        }

        // other system
        return new ServerBootstrapEventLoopGroupOptionForOther();
    }

    private void defaultSetting() {
        // 如果没有 handler 默认给一个 业务处理器
        if (this.channelHandlerProcessors.isEmpty()) {
            registerChannelHandler("ExternalBizHandler", new ExternalBizHandler());
        }
    }

    private void check() throws RuntimeException {
        if (this.port == 0) {
            throw new RuntimeException("port error , is zero");
        }

        if (Objects.isNull(this.externalJoinEnum)) {
            throw new RuntimeException("externalJoinEnum expected: " + Arrays.toString(ExternalJoinEnum.values()));
        }

        if (Objects.isNull(this.externalBoltBrokerClientStartup)) {
            throw new RuntimeException("必须设置一个内部逻辑服（ExternalBoltBrokerClientConfig），与broker（游戏网关）通信！");
        }
    }

}
