/*
 * ioGame
 * Copyright (C) 2021 - present  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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

import com.alipay.remoting.ConnectionEventType;
import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.toy.IoGameBanner;
import com.iohao.game.bolt.broker.client.processor.*;
import com.iohao.game.bolt.broker.client.processor.connection.CloseConnectEventClientProcessor;
import com.iohao.game.bolt.broker.client.processor.connection.ConnectEventClientProcessor;
import com.iohao.game.bolt.broker.client.processor.connection.ConnectFailedEventClientProcessor;
import com.iohao.game.bolt.broker.client.processor.connection.ExceptionConnectEventClientProcessor;
import com.iohao.game.bolt.broker.core.GroupWith;
import com.iohao.game.bolt.broker.core.client.BrokerAddress;
import com.iohao.game.bolt.broker.core.client.BrokerClientBuilder;
import com.iohao.game.bolt.broker.core.common.processor.pulse.PulseSignalRequestUserProcessor;
import com.iohao.game.bolt.broker.core.common.processor.pulse.PulseSignalResponseUserProcessor;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

/**
 * 逻辑服抽象类
 *
 * @author 渔民小镇
 * @date 2022-05-14
 */
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract non-sealed class AbstractBrokerClientStartup implements BrokerClientStartup, GroupWith {
    /** 连接 broker （游戏网关） 的地址 */
    BrokerAddress brokerAddress;
    /** 业务框架 */
    BarSkeleton barSkeleton;
    /**
     * BoltBrokerClient 构建器
     * <pre>
     *     如果字段赋值了
     *          就不会使用 {@link BrokerClientStartup#createBrokerClientBuilder()} 接口的值
     *
     *     如果字段没有赋值
     *          就会使用 {@link BrokerClientStartup#createBrokerClientBuilder()} 接口的值
     * </pre>
     */
    BrokerClientBuilder brokerClientBuilder;
    int withNo;

    @Override
    public void connectionEventProcessor(BrokerClientBuilder brokerClientBuilder) {
        brokerClientBuilder
                .addConnectionEventProcessor(ConnectionEventType.CONNECT, ConnectEventClientProcessor::new)
                .addConnectionEventProcessor(ConnectionEventType.CLOSE, CloseConnectEventClientProcessor::new)
                .addConnectionEventProcessor(ConnectionEventType.CONNECT_FAILED, ConnectFailedEventClientProcessor::new)
                .addConnectionEventProcessor(ConnectionEventType.EXCEPTION, ExceptionConnectEventClientProcessor::new);
    }

    @Override
    public void registerUserProcessor(BrokerClientBuilder brokerClientBuilder) {
        brokerClientBuilder
                // 收到网关请求模块信息
                .registerUserProcessor(RequestBrokerClientModuleMessageClientProcessor::new)
                // broker （游戏网关）集群处理
                .registerUserProcessor(BrokerClusterMessageClientProcessor::new)
                // 业务请求处理器
                .registerUserProcessor(RequestMessageClientProcessor::new)
                // 脉冲信号请求接收
                .registerUserProcessor(PulseSignalRequestUserProcessor::new)
                // 脉冲信号响应接收
                .registerUserProcessor(PulseSignalResponseUserProcessor::new)
                // 分布式事件总线接收
                .registerUserProcessor(EventBusMessageClientProcessor::new)
                // 其他逻辑服的上线、下线消息接收
                .registerUserProcessor(BrokerClientOfflineMessageLogicProcessor::new)
                .registerUserProcessor(BrokerClientOnlineMessageLogicProcessor::new)
        ;
    }

    /**
     * 初始化一些配置到构建器中
     * <pre>
     *     这个方法的目的在于，先设置一些配置到 builder 中，后续有需要修改的部分配置在单独到 builder 中设置
     * </pre>
     *
     * @return BoltBrokerClientBuilder
     */
    BrokerClientBuilder initConfig() {
        IoGameBanner.me().init();
        // 业务框架
        this.barSkeleton = this.createBarSkeleton();
        // 连接到游戏网关的地址
        this.brokerAddress = this.createBrokerAddress();
        // 构建器
        if (Objects.isNull(this.brokerClientBuilder)) {
            this.brokerClientBuilder = this.createBrokerClientBuilder();
        }

        Objects.requireNonNull(this.brokerClientBuilder, "brokerClient 构建器必须要有");

        // 设置 config 配置信息到 BoltBrokerClientBuilder 中
        this.brokerClientBuilder
                .withNo(this.withNo)
                .barSkeleton(this.barSkeleton)
                .brokerAddress(this.brokerAddress);

        // 添加连接处理器
        this.connectionEventProcessor(this.brokerClientBuilder);
        // 注册用户处理器
        this.registerUserProcessor(this.brokerClientBuilder);

        // 实验性功能
        experiment();

        return this.brokerClientBuilder;
    }

    /**
     * 实验性功能，将来可能移除的。
     */
    private void experiment() {
        //        ExtRegions.me().add(new MonitorExtRegion());
    }

    @Override
    public void setWithNo(int withNo) {
        this.withNo = withNo;
    }
}
