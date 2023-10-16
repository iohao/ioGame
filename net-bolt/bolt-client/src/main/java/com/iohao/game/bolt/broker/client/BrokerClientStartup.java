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
package com.iohao.game.bolt.broker.client;

import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.bolt.broker.core.client.*;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.common.kit.NetworkKit;

/**
 * BoltBrokerClient 的配置
 *
 * @author 渔民小镇
 * @date 2022-04-29
 */
public sealed interface BrokerClientStartup permits AbstractBrokerClientStartup {
    /**
     * 初始化 业务框架
     * <pre>
     *     如果不需要业务框架的逻辑服，使用下面的示例代码
     *     {@code return BarSkeleton.newBuilder().build();}
     * </pre>
     *
     * @return 业务框架
     */
    BarSkeleton createBarSkeleton();

    /**
     * BoltBrokerClient 构建器
     *
     * <pre>
     *     see {@link BrokerClient#newBuilder()}
     *
     *     see {@link AbstractBrokerClientStartup#setBrokerClientBuilder(BrokerClientBuilder)}
     * </pre>
     *
     * @return 构建器
     */
    BrokerClientBuilder createBrokerClientBuilder();

    /**
     * 初始化 远程连接地址 （连接到游戏网关的地址）
     * <pre>
     *     地址格式:  ip:port
     *     如: 127.0.0.1:10200
     *
     *     默认方法中提供了本地连接 broker（游戏网关） 的地址
     *     如果不能满足业务的，可以重写此方法
     * </pre>
     *
     * @return 远程连接地址
     */
    default BrokerAddress createBrokerAddress() {
        // 类似 127.0.0.1 ，但这里是本机的 ip
        String localIp = NetworkKit.LOCAL_IP;
        // broker （游戏网关）默认端口
        int brokerPort = IoGameGlobalConfig.brokerPort;
        return new BrokerAddress(localIp, brokerPort);
    }

    /**
     * 添加连接处理器
     * <pre>
     *     see:
     *     {@link com.alipay.remoting.ConnectionEventType#CLOSE}
     *     {@link com.alipay.remoting.ConnectionEventType#CONNECT}
     *
     *     默认方法中提供了一些比较通用的连接处理器，如果不能满足业务的，可以重写此方法
     * </pre>
     *
     * @param brokerClientBuilder boltBrokerClientBuilder
     */
    void connectionEventProcessor(BrokerClientBuilder brokerClientBuilder);

    /**
     * 注册用户处理器
     * <pre>
     *     默认方法中提供了一些比较通用的用户处理器，如果不能满足业务的，可以重写此方法
     * </pre>
     *
     * @param brokerClientBuilder boltBrokerClientBuilder
     */
    void registerUserProcessor(BrokerClientBuilder brokerClientBuilder);

    /**
     * BrokerClient 启动后的钩子方法
     * <pre>
     *     如果有需要，可以在这里 保存一下 BrokerClient 的引用
     *
     *     框架会在逻辑服启动时，在 {@link BrokerClientHelper} 中保存了一份 BrokerClient 的引用
     * </pre>
     *
     * @param brokerClient BrokerClient
     */
    default void startupSuccess(BrokerClient brokerClient) {
        // 对于 brokerClient 的引用使用，建议用 BrokerClientHolder
        BrokerClients.put(this.getClass(), brokerClient);
    }
}
