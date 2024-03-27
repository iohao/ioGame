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
 * 连接到 Broker（游戏网关）前的监听
 *
 * @author 渔民小镇
 * @date 2023-12-14
 */
interface ConnectionBeforeListener {
    /**
     * 将当前逻辑服注册到 Broker（游戏网关）之前的回调
     * <pre>
     *     如果有特殊业务的，开发者可以在此方法中给 BrokerClient、BrokerClientModuleMessage 增加一些其他的附加信息
     *
     *     this.barSkeleton.getRunners().onStart();
     *     this.registerBefore()
     *     ...
     *     registerToBroker()
     * </pre>
     *
     * @param moduleMessage 当前逻辑服信息
     * @param client        当前逻辑服
     * @see BrokerClient#init()
     */
    default void registerBefore(BrokerClientModuleMessage moduleMessage, BrokerClient client) {

    }
}
