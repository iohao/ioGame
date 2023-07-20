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
package com.iohao.game.bolt.broker.client.external;

import com.iohao.game.bolt.broker.client.external.bootstrap.ExternalChannelInitializerCallback;
import com.iohao.game.bolt.broker.client.external.bootstrap.ExternalJoinEnum;
import com.iohao.game.bolt.broker.client.external.bootstrap.ServerBootstrapEventLoopGroupOption;
import com.iohao.game.bolt.broker.client.external.bootstrap.heart.IdleProcessSetting;
import com.iohao.game.bolt.broker.client.external.bootstrap.initializer.*;
import com.iohao.game.bolt.broker.client.external.bootstrap.option.ServerBootstrapEventLoopGroupOptionForLinux;
import com.iohao.game.bolt.broker.client.external.bootstrap.option.ServerBootstrapEventLoopGroupOptionForMac;
import com.iohao.game.bolt.broker.client.external.bootstrap.option.ServerBootstrapEventLoopGroupOptionForOther;
import com.iohao.game.bolt.broker.client.external.config.ExternalGlobalConfig;
import com.iohao.game.bolt.broker.client.external.simple.ExternalBrokerClientStartup;
import com.iohao.game.bolt.broker.core.client.BrokerAddress;
import com.iohao.game.common.kit.system.OsInfo;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import lombok.Setter;
import lombok.experimental.Accessors;

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
@Setter
@Accessors(fluent = true)
public final class ExternalServerBuilder {

    /** 服务器 */
    final ServerBootstrap bootstrap = new ServerBootstrap();
    /**
     * 自定义 - 编排业务
     * <pre>
     *     将在下个大版本中移除
     *     请使用 {@link ExternalServerBuilder#channelPipelineHook(ChannelPipelineHook)} 代替
     * </pre>
     */
    @Deprecated
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
    /** 自定义 - 编排业务钩子方法 */
    ChannelPipelineHook channelPipelineHook;

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
     * <pre>
     *     将在下个大版本中移除
     *     请使用 {@link ExternalServerBuilder#channelPipelineHook(ChannelPipelineHook)} 代替
     * </pre>
     *
     * @param name      name
     * @param processor handler
     * @return me
     */
    @Deprecated
    public ExternalServerBuilder registerChannelHandler(String name, ChannelHandler processor) {
        this.channelHandlerProcessors.put(name, processor);
        return this;
    }

    /**
     * true 表示请求业务方法需要先登录
     *
     * @return me
     */
    public ExternalServerBuilder enableVerifyIdentity(boolean verifyIdentity) {
        ExternalGlobalConfig.accessAuthenticationHook.setVerifyIdentity(verifyIdentity);
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
        /*
         * 这两句代码都是对 netty 的业务编排，channelHandlerProcessors 已经标记为过期，这里只是做个兼容。
         * 两者只能选其一，建议使用 channelPipelineHook 来做编排，因为 hook 在使用上灵活性相对高一些。
         */
        this.option.setChannelHandlerProcessors(this.channelHandlerProcessors);
        this.option.setChannelPipelineHook(this.channelPipelineHook);

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
        OsInfo osInfo = OsInfo.me();

        // 根据系统内核来优化
        if (osInfo.isLinux()) {
            // linux
            return new ServerBootstrapEventLoopGroupOptionForLinux();
        } else if (osInfo.isMac()) {
            // mac
            return new ServerBootstrapEventLoopGroupOptionForMac();
        }

        // other system
        return new ServerBootstrapEventLoopGroupOptionForOther();
    }

    private void defaultSetting() {
        /*
         * 如果没有 ChannelHandler，使用默认的钩子接口。
         *
         * 注意 this.channelHandlerProcessors 成员将在下个大版本中移除，这里是做个兼容。
         * 通过 ChannelPipelineHook 钩子接口可以使得代码相对清晰，同时也更具灵活性。
         */
        if (this.channelHandlerProcessors.isEmpty() && Objects.isNull(this.channelPipelineHook)) {
            this.channelPipelineHook = new DefaultChannelPipelineHook();
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

        if (Objects.nonNull(this.channelPipelineHook) && !channelHandlerProcessors.isEmpty()) {
            String msg = """
                    channelPipelineHook 与 channelHandlerProcessors 两者只能选其一。
                    建议使用 channelPipelineHook 来做自定义 - 编排业务。
                    因为 channelHandlerProcessors 将在下个大版本中移除。
                    """;

            throw new RuntimeException(msg);
        }
    }

}
