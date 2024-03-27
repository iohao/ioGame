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
package com.iohao.game.action.skeleton.core.commumication;

/**
 * 当前服务器上下文
 * <pre>
 *     see BrokerClientHelper
 *
 *     当增加网络通讯聚合概念后，之后的在增加相关的通讯上下文就方便很多了
 *     新增的通讯上下都作为聚合的父类，在使用的分类上也简单了
 * </pre>
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
public interface BrokerClientContext extends ChannelContext, SimpleServer {
    /**
     * 获取逻辑服 id
     *
     * @return id
     */
    String getId();

    /**
     * 发送消息到游戏网关
     *
     * @param request 消息
     * @throws Exception e
     */
    void oneway(final Object request) throws Exception;

    /**
     * 框架网络通讯聚合接口
     *
     * @return 框架网络通信聚合接口
     */
    CommunicationAggregationContext getCommunicationAggregationContext();

    /**
     * 推送通讯相关 - 得到广播通讯上下文
     *
     * @return 广播通讯上下文
     */
    default BroadcastContext getBroadcastContext() {
        return this.getCommunicationAggregationContext();
    }

    /**
     * 推送通讯相关 - 得到顺序的 - 广播通讯上下文
     *
     * @return 顺序的 - 广播通讯上下文
     */
    default BroadcastOrderContext getBroadcastOrderContext() {
        return this.getCommunicationAggregationContext();
    }

    /**
     * 得到 processor 上下文
     *
     * @return processor 上下文
     */
    default ProcessorContext getProcessorContext() {
        return this.getCommunicationAggregationContext();
    }

    /**
     * 逻辑服间的相互通信相关 - 得到内部模块通讯上下文
     *
     * @return 内部模块通讯上下文
     */
    default InvokeModuleContext getInvokeModuleContext() {
        return this.getCommunicationAggregationContext();
    }

    /**
     * 内部模块通讯上下文，内部模块指的是游戏对外服
     *
     * @return 游戏对外服通讯上下文
     */
    default InvokeExternalModuleContext getInvokeExternalModuleContext() {
        return this.getCommunicationAggregationContext();
    }
}
