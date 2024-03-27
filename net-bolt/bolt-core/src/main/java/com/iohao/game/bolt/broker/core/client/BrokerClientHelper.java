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
package com.iohao.game.bolt.broker.core.client;

import com.iohao.game.action.skeleton.core.commumication.*;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import lombok.experimental.UtilityClass;

/**
 * 游戏逻辑服 BrokerClient 的引用持有
 * <pre>
 *     会在 {@link BrokerClientBuilder#build()} 时赋值
 *
 *     对于多个 BrokerClient 的引用管理，可以参考 {@link BrokerClients}
 *
 *     这个类的主要作用是为了更好的区分框架提供的通讯方式
 *     让开发者在使用时更加的清晰
 * </pre>
 * 注意
 * <pre>
 *     如果一个进程中启动了多个逻辑服，会随机选一个作为 brokerClient 的引用。
 *
 *     如果想获取当前 action 所关联的游戏逻辑服，可以通过 FlowContext 获取
 *     参考 {@link FlowContext#getBroadcastContext()}
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-15
 */
@UtilityClass
public class BrokerClientHelper {
    BrokerClient brokerClient;

    public BrokerClientContext getBrokerClient() {
        return brokerClient;
    }

    public ProcessorContext getProcessorContext() {
        return brokerClient.getCommunicationAggregationContext();
    }

    /**
     * 广播通讯上下文
     *
     * @return BroadcastContext
     */
    public BroadcastContext getBroadcastContext() {
        return brokerClient.getCommunicationAggregationContext();
    }

    /**
     * 广播通讯上下文 - 严格顺序的
     *
     * @return BroadcastOrderContext
     */
    public BroadcastOrderContext getBroadcastOrderContext() {
        return brokerClient.getCommunicationAggregationContext();
    }

    /**
     * 游戏逻辑服与游戏逻辑服之间的通讯上下文
     *
     * @return InvokeModuleContext
     */
    public InvokeModuleContext getInvokeModuleContext() {
        return brokerClient.getCommunicationAggregationContext();
    }

    /**
     * 游戏逻辑服与游戏对外服的通讯上下文
     *
     * @return InvokeExternalModuleContext
     */
    public InvokeExternalModuleContext getInvokeExternalModuleContext() {
        return brokerClient.getCommunicationAggregationContext();
    }
}
