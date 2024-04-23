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
import com.iohao.game.action.skeleton.core.flow.FlowContext;

import java.util.Collection;

/**
 * 房间广播，兼容旧的设计
 * <pre>
 *     该系列方法已经废弃，请使用下面的来代替，在语义上会更清晰
 *     1 {@link RoomBroadcastEnhance#ofRangeBroadcast(CommunicationAggregationContext)}
 *     2 {@link RoomBroadcastEnhance#ofRangeBroadcast(FlowContext)}
 * </pre>
 *
 * @author 渔民小镇
 * @date 2024-04-23
 */
@Deprecated
public interface RoomBroadcastFlowContext extends RoomBroadcastEnhance {

    /**
     * 广播业务数据给房间内的所有玩家
     * <pre>
     *     该方法已经废弃，请使用下面的来代替，在语义上会更清晰
     *     1 {@link RoomBroadcastEnhance#ofRangeBroadcast(CommunicationAggregationContext)}
     *     2 {@link RoomBroadcastEnhance#ofRangeBroadcast(FlowContext)}
     * </pre>
     *
     * @param flowContext flow 上下文
     * @param bizData     广播的业务数据
     */
    @Deprecated
    default void broadcast(FlowContext flowContext, Object bizData) {
        this.broadcast(flowContext, bizData, this.listPlayerId());
    }

    /**
     * 广播业务数据给用户列表
     * <pre>
     *     该方法已经废弃，请使用下面的来代替，在语义上会更清晰
     *     1 {@link RoomBroadcastEnhance#ofRangeBroadcast(CommunicationAggregationContext)}
     *     2 {@link RoomBroadcastEnhance#ofRangeBroadcast(FlowContext)}
     * </pre>
     *
     * @param flowContext flow 上下文
     * @param bizData     广播的业务数据
     * @param userIdList  用户列表
     */
    @Deprecated
    default void broadcast(FlowContext flowContext, Object bizData, Collection<Long> userIdList) {
        this.broadcast(flowContext, bizData, userIdList, 0);
    }

    /**
     * 广播业务数据给房间内的所有玩家， 排除指定用户
     * <pre>
     *     该方法已经废弃，请使用下面的来代替，在语义上会更清晰
     *     1 {@link RoomBroadcastEnhance#ofRangeBroadcast(CommunicationAggregationContext)}
     *     2 {@link RoomBroadcastEnhance#ofRangeBroadcast(FlowContext)}
     * </pre>
     *
     * @param flowContext   flow 上下文
     * @param bizData       广播的业务数据
     * @param excludeUserId 排除的用户
     */
    @Deprecated
    default void broadcast(FlowContext flowContext, Object bizData, long excludeUserId) {
        this.broadcast(flowContext, bizData, this.listPlayerId(), excludeUserId);
    }

    /**
     * 广播业务数据给用户列表, 并排除一个用户
     * <pre>
     *     该方法已经废弃，请使用下面的来代替，在语义上会更清晰
     *     1 {@link RoomBroadcastEnhance#ofRangeBroadcast(CommunicationAggregationContext)}
     *     2 {@link RoomBroadcastEnhance#ofRangeBroadcast(FlowContext)}
     * </pre>
     *
     * @param flowContext   flow 上下文
     * @param bizData       广播的业务数据
     * @param userIdList    用户列表
     * @param excludeUserId 排除的用户
     */
    @Deprecated
    default void broadcast(FlowContext flowContext, Object bizData, Collection<Long> userIdList, long excludeUserId) {

        this.ofRangeBroadcast(flowContext)
                // 响应的数据
                .setResponseMessage(flowContext.getCmdInfo(), bizData)
                .addUserId(userIdList)
                .removeUserId(excludeUserId)
                .execute();
    }

    /**
     * 广播业务数据给指定的用户
     * <pre>
     *     该方法已经废弃，请使用下面的来代替，在语义上会更清晰
     *     1 {@link RoomBroadcastEnhance#ofRangeBroadcast(CommunicationAggregationContext)}
     *     2 {@link RoomBroadcastEnhance#ofRangeBroadcast(FlowContext)}
     * </pre>
     *
     * @param flowContext flow 上下文
     * @param bizData     广播的业务数据
     * @param userId      指定的用户
     */
    @Deprecated
    default void broadcastUser(FlowContext flowContext, Object bizData, long userId) {
        this.ofRangeBroadcast(flowContext)
                // 响应的数据
                .setResponseMessage(flowContext.getCmdInfo(), bizData)
                .addUserId(userId)
                .execute();
    }
}