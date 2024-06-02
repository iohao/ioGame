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
package com.iohao.game.widget.light.room;

import com.iohao.game.action.skeleton.core.commumication.CommunicationAggregationContext;
import com.iohao.game.action.skeleton.kit.RangeBroadcaster;

import java.util.Collection;

/**
 * 房间内的广播增强
 *
 * @author 渔民小镇
 * @date 2024-04-23
 * @since 21.8
 */
interface RoomBroadcastEnhance {
    /**
     * 房间内的所有玩家 userIds
     *
     * @return players，userIds
     */
    Collection<Long> listPlayerId();

    /**
     * 设置通讯上下文
     * <pre>{@code
     * // 方式一：通过 flowContext 得到通讯上下文
     * CommunicationAggregationContext aggregationContext = flowContext.option(FlowAttr.aggregationContext);
     * // 方式二：通过 BrokerClient 得到通讯上下文
     * CommunicationAggregationContext aggregationContext = BrokerClientHelper.getBrokerClient().getCommunicationAggregationContext();
     * }
     * </pre>
     *
     * @param aggregationContext 通讯上下文
     */
    void setAggregationContext(CommunicationAggregationContext aggregationContext);

    /**
     * get 通讯上下文
     *
     * @return 通讯上下文
     */
    CommunicationAggregationContext getAggregationContext();

    /**
     * 通过 CommunicationAggregationContext 创建一个 RangeBroadcast，默认会添加上当前房间内的所有玩家
     *
     * @param aggregationContext aggregationContext
     * @return RangeBroadcast 范围内的广播
     */
    default RangeBroadcaster ofRangeBroadcast(CommunicationAggregationContext aggregationContext) {
        return this.ofEmptyRangeBroadcast(aggregationContext)
                // 添加上房间内的所有玩家
                .addUserId(this.listPlayerId());
    }

    /**
     * 通过 CommunicationAggregationContext 创建一个 RangeBroadcast
     *
     * @param aggregationContext aggregationContext
     * @return RangeBroadcast 范围内的广播
     */
    default RangeBroadcaster ofEmptyRangeBroadcast(CommunicationAggregationContext aggregationContext) {
        return RangeBroadcaster.of(aggregationContext);
    }

    /**
     * 创建一个 RangeBroadcast，默认会添加上当前房间内的所有玩家
     *
     * @return RangeBroadcast 范围内的广播
     */
    default RangeBroadcaster ofRangeBroadcast() {
        return this.ofRangeBroadcast(this.getAggregationContext());
    }

    /**
     * 创建一个 RangeBroadcast
     *
     * @return RangeBroadcast 范围内的广播
     */
    default RangeBroadcaster ofEmptyRangeBroadcast() {
        return this.ofEmptyRangeBroadcast(this.getAggregationContext());
    }
}