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

import com.iohao.game.common.kit.PresentKit;
import com.iohao.game.widget.light.room.flow.RoomCreateContext;
import com.iohao.game.widget.light.room.kit.RoomKit;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * 房间
 *
 * @author 渔民小镇
 * @date 2022-03-31
 * @since 21.8
 */
@SuppressWarnings("unchecked")
public interface Room extends Serializable, RoomBroadcastEnhance {
    /**
     * 玩家
     * <pre>
     *     key is userId
     *     value is player
     * </pre>
     *
     * @return 玩家
     */
    Map<Long, Player> getPlayerMap();

    /**
     * 玩家位置
     * <pre>
     *     key is seat
     *     value is userId
     * </pre>
     *
     * @return 玩家位置
     */
    Map<Integer, Long> getPlayerSeatMap();

    /**
     * @return 房间唯一 id
     */
    long getRoomId();

    /**
     * @param roomId 房间唯一 id
     */
    void setRoomId(long roomId);

    /**
     * @return 房间号
     */
    int getRoomNo();

    /**
     * @param roomNo 房间号
     */
    void setRoomNo(int roomNo);

    /**
     * @return 房间空间大小。如 4 就是 4 个人上限 (可以根据规则设置)
     */
    int getSpaceSize();

    /**
     * @param spaceSize 房间空间大小。如 4 就是 4 个人上限 (可以根据规则设置)
     */
    void setSpaceSize(int spaceSize);

    /**
     * @return 房间状态
     */
    RoomStatusEnum getRoomStatusEnum();

    /**
     * @param roomStatusEnum 房间状态
     */
    void setRoomStatusEnum(RoomStatusEnum roomStatusEnum);

    /**
     * @return 创建房间信息（玩法规则）
     */
    RoomCreateContext getRoomCreateContext();

    /**
     * 房间创建者的 userId
     *
     * @return userId
     */
    default long getCreatorUserId() {
        return this.getRoomCreateContext().getCreatorUserId();
    }

    /**
     * @param roomCreateContext 创建房间信息（玩法规则）
     */
    void setRoomCreateContext(RoomCreateContext roomCreateContext);

    /**
     * 玩家列表: 所有玩家信息
     *
     * @param <T> 玩家
     * @return 所有玩家信息 (包括退出房间的玩家信息)
     */
    default <T extends Player> Collection<T> listPlayer() {
        return (Collection<T>) this.getPlayerMap().values();
    }

    default Stream<Player> streamPlayer() {
        return this.listPlayer().stream();
    }

    default List<Player> listPlayer(Predicate<Player> predicate) {
        return listPlayer().stream()
                .filter(predicate)
                .toList();
    }

    default Collection<Long> listPlayerId(long excludePlayerId) {
        return listPlayerId().stream()
                .filter(playerId -> playerId != excludePlayerId)
                .toList();
    }

    default Collection<Long> listPlayerId() {
        return this.getPlayerMap().keySet();
    }

    default <T extends Player> T getPlayerById(long userId) {
        return (T) this.getPlayerMap().get(userId);
    }

    default boolean existUser(long userId) {
        return this.getPlayerMap().get(userId) != null;
    }

    /**
     * 添加玩家到房间内
     *
     * @param player 玩家
     */
    default void addPlayer(Player player) {
        player.setRoomId(this.getRoomId());
        long userId = player.getUserId();
        this.getPlayerMap().put(userId, player);
        this.getPlayerSeatMap().put(player.getSeat(), userId);
    }

    /**
     * 移除玩家
     *
     * @param player 玩家
     */
    default void removePlayer(Player player) {
        long userId = player.getUserId();
        this.getPlayerMap().remove(userId);
        this.getPlayerSeatMap().remove(player.getSeat());
    }

    /**
     * 当前房间是否是所指定的房间状态
     *
     * @param roomStatusEnum 房间状态
     * @return true 是所指定的房间状态
     */
    default boolean isStatus(RoomStatusEnum roomStatusEnum) {
        return this.getRoomStatusEnum() == roomStatusEnum;
    }

    /**
     * 如果玩家在房间内，就执行给定的操作，否则不执行任何操作。
     *
     * @param userId userId
     * @param action 给定操作
     * @param <T>    t
     */
    default <T extends Player> void ifPlayerExist(long userId, Consumer<T> action) {
        T player = this.getPlayerById(userId);
        Optional.ofNullable(player).ifPresent(action);
    }

    /**
     * 如果玩家不在房间内，就执行给定的操作，否则不执行任何操作。
     *
     * @param userId   userId
     * @param runnable 给定操作
     */
    default void ifPlayerNotExist(long userId, Runnable runnable) {
        var player = this.getPlayerById(userId);
        PresentKit.ifNull(player, runnable);
    }

    default int countPlayer() {
        return this.getPlayerMap().size();
    }

    default boolean isEmptyPlayer() {
        return this.getPlayerMap().isEmpty();
    }

    /**
     * 得到一个空位置
     *
     * @return >=0 表示有位置
     */
    default int getEmptySeatNo() {
        return RoomKit.getEmptySeatNo(this);
    }

    /**
     * 玩家是否都准备了
     *
     * @return true 所有玩家都准备了
     */
    default boolean isReadyPlayers() {
        // 是否都准备了。玩家中，如果有任意一个没点准备的，notReady 为 true
        boolean notReady = this.streamPlayer().anyMatch(player -> !player.isReady());
        return !notReady;
    }

    default void forEach(BiConsumer<Long, Player> action) {
        this.getPlayerMap().forEach(action);
    }
}