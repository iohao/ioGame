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
package com.iohao.game.bolt.broker.core.client;

import com.iohao.game.action.skeleton.core.commumication.*;

/**
 * 游戏逻辑服 BrokerClient 的引用持有
 * <pre>
 *     会在 {@link BrokerClientBuilder#build()} 时赋值
 *
 *     对于多个 BrokerClient 的引用管理，可以参考 {@link BrokerClients}
 *
 *
 *     这个类的主要作用是为了更好的区分框架提供的通讯方式
 *     让开发者在使用时更加的清晰
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-15
 */
public class BrokerClientHelper {
    static BrokerClient brokerClient;

    public static BrokerClientContext getBrokerClient() {
        return brokerClient;
    }

    public static ProcessorContext getProcessorContext() {
        return brokerClient.getCommunicationAggregationContext();
    }

    /**
     * 广播通讯上下文
     *
     * @return BroadcastContext
     */
    public static BroadcastContext getBroadcastContext() {
        return brokerClient.getCommunicationAggregationContext();
    }

    /**
     * 广播通讯上下文 - 严格顺序的
     *
     * @return BroadcastOrderContext
     */
    public static BroadcastOrderContext getBroadcastOrderContext() {
        return brokerClient.getCommunicationAggregationContext();
    }

    /**
     * 游戏逻辑服与游戏逻辑服之间的通讯上下文
     *
     * @return InvokeModuleContext
     */
    public static InvokeModuleContext getInvokeModuleContext() {
        return brokerClient.getCommunicationAggregationContext();
    }

    /**
     * 游戏逻辑服与游戏对外服的通讯上下文
     *
     * @return InvokeExternalModuleContext
     */
    public static InvokeExternalModuleContext getInvokeExternalModuleContext() {
        return brokerClient.getCommunicationAggregationContext();
    }

    @Deprecated
    private BrokerClientHelper() {

    }

    /**
     * 请使用静态方法
     * <pre>
     *     将 BrokerClientHelper.xxx() 改为 BrokerClientHelper.xxx()
     *
     *     将在下个大版本中移除
     * </pre>
     *
     * @return me
     */
    @Deprecated
    public static BrokerClientHelper me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    @Deprecated
    private static class Holder {
        static final BrokerClientHelper ME = new BrokerClientHelper();
    }
}
