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
package com.iohao.game.bolt.broker.client.external.simple;

import com.alipay.remoting.rpc.protocol.UserProcessor;
import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.BarSkeletonBuilder;
import com.iohao.game.bolt.broker.client.AbstractBrokerClientStartup;
import com.iohao.game.bolt.broker.client.external.processor.*;
import com.iohao.game.bolt.broker.client.processor.BrokerClusterMessageClientProcessor;
import com.iohao.game.bolt.broker.client.processor.RequestBrokerClientModuleMessageClientProcessor;
import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.client.BrokerClientBuilder;
import com.iohao.game.bolt.broker.core.client.BrokerClientType;
import com.iohao.game.bolt.broker.core.common.processor.pulse.PulseSignalRequestUserProcessor;
import com.iohao.game.bolt.broker.core.common.processor.pulse.PulseSignalResponseUserProcessor;

import java.util.function.Supplier;

/**
 * 对外服的 BoltBrokerClient
 * <pre>
 *     负责与 broker (游戏网关通信)
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-04-29
 */
public class ExternalBrokerClientStartup extends AbstractBrokerClientStartup {

    @Override
    public BarSkeleton createBarSkeleton() {
        // 对外服不需要业务框架，这里给个空的
        BarSkeletonBuilder builder = BarSkeleton.newBuilder();
        builder.getSetting().setPrint(false);
        return builder.build();
    }

    @Override
    public void registerUserProcessor(BrokerClientBuilder builder) {
        // 收到网关请求模块信息
        Supplier<UserProcessor<?>> requestBrokerClientModuleSupplier = RequestBrokerClientModuleMessageClientProcessor::new;
        // broker （游戏网关）集群处理
        Supplier<UserProcessor<?>> brokerClusterMessageProcessorSupplier = BrokerClusterMessageClientProcessor::new;

        // 注册 广播处理器
        Supplier<UserProcessor<?>> broadcastMessageProcessorSupplier = BroadcastMessageExternalProcessor::new;
        // 注册 顺序广播处理器
        Supplier<UserProcessor<?>> broadcastOrderMessageProcessorSupplier = BroadcastOrderMessageExternalProcessor::new;
        // 注册 用户id变更处理
        Supplier<UserProcessor<?>> settingUserIdMessageProcessorSupplier = SettingUserIdMessageExternalProcessor::new;
        // 注册 接收网关消息处理
        Supplier<UserProcessor<?>> responseMessageProcessorSupplier = ResponseMessageExternalProcessor::new;
        // 注册 用户绑定逻辑服
        Supplier<UserProcessor<?>> endPointLogicServerMessageProcessorSupplier = EndPointLogicServerMessageExternalProcessor::new;
        // 注册 处理来自游戏逻辑服的请求，并响应结果给请求方
        Supplier<UserProcessor<?>> requestCollectExternalMessageExternalProcessorSupplier = RequestCollectExternalMessageExternalProcessor::new;

        builder
                .registerUserProcessor(requestBrokerClientModuleSupplier)
                .registerUserProcessor(brokerClusterMessageProcessorSupplier)
                .registerUserProcessor(broadcastMessageProcessorSupplier)
                .registerUserProcessor(broadcastOrderMessageProcessorSupplier)
                .registerUserProcessor(settingUserIdMessageProcessorSupplier)
                .registerUserProcessor(responseMessageProcessorSupplier)
                .registerUserProcessor(endPointLogicServerMessageProcessorSupplier)
                .registerUserProcessor(requestCollectExternalMessageExternalProcessorSupplier)
                // 脉冲信号请求接收
                .registerUserProcessor(PulseSignalRequestUserProcessor::new)
                // 脉冲信号响应接收
                .registerUserProcessor(PulseSignalResponseUserProcessor::new)
                .registerUserProcessor(DeprecatedBrokerClientModuleMessageOfflineExternalProcessor::new)
        ;
    }

    @Override
    public BrokerClientBuilder createBrokerClientBuilder() {
        BrokerClientBuilder builder = BrokerClient.newBuilder();
        builder.appName("游戏对外服")
                // 逻辑服标签 （tag 相当于归类）
                .tag("external")
                // 逻辑服设置为对外服类型
                .brokerClientType(BrokerClientType.EXTERNAL)
        ;
        return builder;
    }
}
