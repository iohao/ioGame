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
package com.iohao.game.bolt.broker.client.external.simple;

import com.alipay.remoting.rpc.protocol.UserProcessor;
import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.bolt.broker.client.AbstractBrokerClientStartup;
import com.iohao.game.bolt.broker.client.external.processor.*;
import com.iohao.game.bolt.broker.client.processor.BrokerClusterMessageClientProcessor;
import com.iohao.game.bolt.broker.client.processor.ExtRequestMessageClientProcessor;
import com.iohao.game.bolt.broker.client.processor.RequestBrokerClientModuleMessageClientProcessor;
import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.client.BrokerClientBuilder;
import com.iohao.game.bolt.broker.core.client.BrokerClientType;

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
        return BarSkeleton
                .newBuilder()
                .build();
    }

    @Override
    public void registerUserProcessor(BrokerClientBuilder builder) {
        // 收到网关请求模块信息
        Supplier<UserProcessor<?>> requestBrokerClientModuleSupplier = RequestBrokerClientModuleMessageClientProcessor::new;
        // broker （游戏网关）集群处理
        Supplier<UserProcessor<?>> brokerClusterMessageProcessorSupplier = BrokerClusterMessageClientProcessor::new;
        // 注册 接收扩展逻辑服的消息
        Supplier<UserProcessor<?>> extRequestMessageProcessorSupplier = ExtRequestMessageClientProcessor::new;

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
                .registerUserProcessor(extRequestMessageProcessorSupplier)
                .registerUserProcessor(broadcastMessageProcessorSupplier)
                .registerUserProcessor(broadcastOrderMessageProcessorSupplier)
                .registerUserProcessor(settingUserIdMessageProcessorSupplier)
                .registerUserProcessor(responseMessageProcessorSupplier)
                .registerUserProcessor(endPointLogicServerMessageProcessorSupplier)
                .registerUserProcessor(requestCollectExternalMessageExternalProcessorSupplier)
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
