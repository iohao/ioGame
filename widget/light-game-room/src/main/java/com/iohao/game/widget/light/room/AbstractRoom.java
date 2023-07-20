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
package com.iohao.game.widget.light.room;

import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.flow.ActionMethodResultWrap;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingHashMap;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;

/**
 * 抽象房间
 *
 * @author 渔民小镇
 * @date 2022-03-31
 */
@Getter
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AbstractRoom implements Serializable {
    @Serial
    private static final long serialVersionUID = -6937915481102847959L;

    /**
     * 玩家
     * <pre>
     *     key is userId
     *     value is player
     * </pre>
     */
    final Map<Long, AbstractPlayer> playerMap = new NonBlockingHashMap<>();

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
    /** 房间号 */
    int roomNo;

    /** 创建房间信息 */
    CreateRoomInfo createRoomInfo;
    /** 房间空间大小: 4 就是4个人上限 (根据规则设置) */
    int spaceSize;

    /** 房间状态 */
    RoomStatusEnum roomStatusEnum = RoomStatusEnum.wait;

    /**
     * 创建推送对象
     *
     * @param flowContext flowContext
     * @param <T>         t
     * @return AbstractFlowContextSend
     */
    protected abstract <T extends AbstractFlowContextSend> T createSend(FlowContext flowContext);

    /**
     * 玩家列表: 所有玩家信息
     *
     * @param <T> 玩家
     * @return 所有玩家信息 (包括退出房间的玩家信息)
     */
    @SuppressWarnings("unchecked")
    public <T extends AbstractPlayer> Collection<T> listPlayer() {
        return (Collection<T>) this.playerMap.values();
    }

    public List<AbstractPlayer> listPlayer(Predicate<AbstractPlayer> predicate) {
        return listPlayer().stream()
                .filter(predicate)
                .toList();
    }

    public Collection<Long> listPlayerId(long excludePlayerId) {
        return listPlayerId().stream()
                .filter(playerId -> playerId != excludePlayerId)
                .toList();
    }

    public Collection<Long> listPlayerId() {
        return this.playerMap.keySet();
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractPlayer> T getPlayerById(long userId) {
        return (T) this.playerMap.get(userId);
    }

    public boolean existUser(long userId) {
        return this.playerMap.get(userId) != null;
    }

    /**
     * 添加玩家到房间内
     *
     * @param player 玩家
     */
    public void addPlayer(AbstractPlayer player) {
        long userId = player.getId();
        this.playerMap.put(userId, player);
        this.playerSeatMap.put(player.getSeat(), userId);
    }

    /**
     * 移出玩家
     *
     * @param player 玩家
     */
    public void removePlayer(AbstractPlayer player){
        long userId = player.getId();
        this.playerMap.remove(userId);
        this.playerSeatMap.remove(player.getSeat());
    }

    public boolean isStatus(RoomStatusEnum roomStatusEnum) {
        return this.roomStatusEnum == roomStatusEnum;
    }

    /**
     * 广播业务数据给房间内的所有玩家
     *
     * @param flowContext  flow 上下文
     * @param methodResult 广播的业务数据
     */
    public void broadcast(FlowContext flowContext, Object methodResult) {
        this.broadcast(flowContext, methodResult, this.listPlayerId());
    }

    /**
     * 广播业务数据给用户列表
     *
     * @param flowContext  flow 上下文
     * @param methodResult 广播的业务数据
     * @param userIdList   用户列表
     */
    public void broadcast(FlowContext flowContext, Object methodResult, Collection<Long> userIdList) {
        this.broadcast(flowContext, methodResult, userIdList, 0);
    }

    /**
     * 广播业务数据给房间内的所有玩家， 排除指定用户
     *
     * @param flowContext   flow 上下文
     * @param methodResult  广播的业务数据
     * @param excludeUserId 排除的用户
     */
    public void broadcast(FlowContext flowContext, Object methodResult, long excludeUserId) {
        this.broadcast(flowContext, methodResult, this.listPlayerId(), excludeUserId);
    }

    /**
     * 广播业务数据给用户列表, 并排除一个用户
     *
     * @param flowContext   flow 上下文
     * @param methodResult  广播的业务数据
     * @param userIdList    用户列表
     * @param excludeUserId 排除的用户
     */
    public void broadcast(FlowContext flowContext, Object methodResult, Collection<Long> userIdList, long excludeUserId) {
        // 设置业务数据到 flowContext 中
        flowContext.setMethodResult(methodResult);

        // 把刚才设置的业务数据包装到响应对象中
        BarSkeleton barSkeleton = flowContext.getBarSkeleton();
        // 4 ---- wrap result 结果包装器
        ActionMethodResultWrap actionMethodResultWrap = barSkeleton.getActionMethodResultWrap();
        // 结果包装器
        actionMethodResultWrap.wrap(flowContext);

        AbstractFlowContextSend send = this.createSend(flowContext)
                .addUserId(userIdList, excludeUserId);

        send.send();
    }

    /**
     * 广播业务数据给指定的用户
     *
     * @param flowContext  flow 上下文
     * @param methodResult 广播的业务数据
     * @param userId       指定的用户
     */
    public void broadcastUser(FlowContext flowContext, Object methodResult, long userId) {
        flowContext.setMethodResult(methodResult);

        AbstractFlowContextSend send = this.createSend(flowContext)
                .addUserId(userId);

        send.send();
    }
}
