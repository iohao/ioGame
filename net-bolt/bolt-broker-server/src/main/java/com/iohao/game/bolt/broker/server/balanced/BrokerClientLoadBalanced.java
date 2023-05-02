/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.iohao.game.bolt.broker.server.balanced;

import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientProxy;

/**
 * 逻辑服管理器
 * <pre>
 *     注意：
 *     对外服中与 broker 建立连接的的 boltBrokerClient ，所以对外服也属于是逻辑服的一种
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-14
 */
public interface BrokerClientLoadBalanced {
    /**
     * 注册逻辑服
     *
     * @param brokerClientProxy brokerClientProxy
     */
    void register(BrokerClientProxy brokerClientProxy);

    /**
     * 删除逻辑服
     *
     * @param brokerClientProxy brokerClientProxy
     */
    void remove(BrokerClientProxy brokerClientProxy);
}
