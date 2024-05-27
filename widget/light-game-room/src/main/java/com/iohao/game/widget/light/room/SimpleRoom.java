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
import com.iohao.game.bolt.broker.core.client.BrokerClientHelper;
import com.iohao.game.widget.light.room.flow.RoomCreateContext;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingHashMap;

import java.io.Serial;
import java.util.Map;
import java.util.TreeMap;

/**
 * 房间（内置实现）
 *
 * @author 渔民小镇
 * @date 2022-03-31
 * @since 21.8
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SimpleRoom implements Room {
    @Serial
    private static final long serialVersionUID = -6937915481102847959L;
    /**
     * 玩家
     * <pre>
     *     key is userId
     *     value is player
     * </pre>
     */
    final Map<Long, Player> playerMap = new NonBlockingHashMap<>();
    /**
     * 玩家位置
     * <pre>
     *     key is seat
     *     value is userId
     * </pre>
     */
    final Map<Integer, Long> playerSeatMap = new TreeMap<>();
    /** 房间唯一 id */
    long roomId;
    /** 创建房间信息 */
    RoomCreateContext roomCreateContext;
    /** 房间空间大小: 4 就是4个人上限 (根据规则设置) */
    int spaceSize;
    /** 房间状态 */
    RoomStatusEnum roomStatusEnum = RoomStatusEnum.wait;
    CommunicationAggregationContext aggregationContext;

    public SimpleRoom() {
        // 为房间设置通讯接口
        aggregationContext = BrokerClientHelper.getBrokerClient().getCommunicationAggregationContext();
    }
}