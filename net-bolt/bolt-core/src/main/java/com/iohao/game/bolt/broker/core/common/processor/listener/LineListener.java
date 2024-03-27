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
package com.iohao.game.bolt.broker.core.common.processor.listener;

import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.message.BrokerClientModuleMessage;

/**
 * 钩子方法，逻辑服上线、下线监听回调
 * <pre>
 *     逻辑服指是的游戏对外服和游戏逻辑服
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-12-14
 */
interface LineListener {
    /**
     * 其他游戏对外服上线监听
     * <pre>
     *     已经在线上的，或者有新上线的游戏对外服都会触发此方法
     * </pre>
     *
     * @param otherModuleMessage 游戏对外服
     * @param client             当前逻辑服
     */
    default void onlineExternal(BrokerClientModuleMessage otherModuleMessage, BrokerClient client) {
    }

    /**
     * 其他游戏对外服下线监听
     *
     * @param otherModuleMessage 下线的游戏对外服
     * @param client             当前逻辑服
     */
    default void offlineExternal(BrokerClientModuleMessage otherModuleMessage, BrokerClient client) {
    }

    /**
     * 其他游戏逻辑服在线监听
     * <pre>
     *     已经在线上的，或者有新上线的游戏逻辑服都会触发此方法
     * </pre>
     *
     * @param otherModuleMessage 在线的其他游戏逻辑服
     * @param client             当前逻辑服
     */
    default void onlineLogic(BrokerClientModuleMessage otherModuleMessage, BrokerClient client) {
    }

    /**
     * 其他游戏逻辑服下线监听
     *
     * @param otherModuleMessage 下线的游戏逻辑服
     * @param client             当前逻辑服
     */
    default void offlineLogic(BrokerClientModuleMessage otherModuleMessage, BrokerClient client) {
    }
}
