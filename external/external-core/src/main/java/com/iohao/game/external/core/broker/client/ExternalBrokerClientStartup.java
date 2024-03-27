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
package com.iohao.game.external.core.broker.client;

import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.BarSkeletonBuilder;
import com.iohao.game.bolt.broker.client.AbstractBrokerClientStartup;
import com.iohao.game.bolt.broker.client.processor.BrokerClusterMessageClientProcessor;
import com.iohao.game.bolt.broker.client.processor.RequestBrokerClientModuleMessageClientProcessor;
import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.client.BrokerClientBuilder;
import com.iohao.game.bolt.broker.core.client.BrokerClientType;
import com.iohao.game.bolt.broker.core.common.processor.pulse.PulseSignalRequestUserProcessor;
import com.iohao.game.bolt.broker.core.common.processor.pulse.PulseSignalResponseUserProcessor;
import com.iohao.game.external.core.broker.client.enhance.ExternalEnhances;
import com.iohao.game.external.core.broker.client.processor.*;
import com.iohao.game.external.core.broker.client.processor.listener.CmdRegionBrokerClientListener;
import lombok.Setter;

/**
 * 负责内部通信，与 Broker（游戏网关）通信
 *
 * @author 渔民小镇
 * @date 2023-02-21
 */
@Setter
public class ExternalBrokerClientStartup extends AbstractBrokerClientStartup {
    String id;

    protected BarSkeletonBuilder createBarSkeletonBuilder() {
        // 对外服不需要业务框架，这里给个空的
        BarSkeletonBuilder builder = BarSkeleton.newBuilder();
        builder.getSetting().setPrint(false);
        ExternalEnhances.enhance(builder);
        return builder;
    }

    @Override
    public BarSkeleton createBarSkeleton() {
        return createBarSkeletonBuilder().build();
    }

    @Override
    public BrokerClientBuilder createBrokerClientBuilder() {
        return BrokerClient.newBuilder()
                .id(this.id)
                .appName("新游戏对外服")
                // 逻辑服标签 （tag 相当于归类）
                .tag("external")
                // 逻辑服设置为对外服类型
                .brokerClientType(BrokerClientType.EXTERNAL)
                .addListener(CmdRegionBrokerClientListener.me())
                ;
    }

    @Override
    public void registerUserProcessor(BrokerClientBuilder builder) {
        builder
                // 收到网关请求模块信息
                .registerUserProcessor(RequestBrokerClientModuleMessageClientProcessor::new)
                // broker （游戏网关）集群处理
                .registerUserProcessor(BrokerClusterMessageClientProcessor::new)
                // 注册 广播处理器
                .registerUserProcessor(BroadcastMessageExternalProcessor::new)
                // 注册 顺序广播处理器
                .registerUserProcessor(BroadcastOrderMessageExternalProcessor::new)
                // 注册 用户id变更处理
                .registerUserProcessor(SettingUserIdMessageExternalProcessor::new)
                // 注册 接收网关消息处理
                .registerUserProcessor(ResponseMessageExternalProcessor::new)
                // 注册 用户绑定逻辑服
                .registerUserProcessor(EndPointLogicServerMessageExternalProcessor::new)
                // 注册 处理来自游戏逻辑服的请求，并响应结果给请求方
                .registerUserProcessor(RequestCollectExternalMessageExternalProcessor::new)
                // 脉冲信号请求接收
                .registerUserProcessor(PulseSignalRequestUserProcessor::new)
                // 脉冲信号响应接收
                .registerUserProcessor(PulseSignalResponseUserProcessor::new)
                // 其他逻辑服的上线、下线相关通知
                .registerUserProcessor(BrokerClientOnlineMessageExternalProcessor::new)
                .registerUserProcessor(BrokerClientOfflineMessageExternalProcessor::new)
        ;
    }
}
