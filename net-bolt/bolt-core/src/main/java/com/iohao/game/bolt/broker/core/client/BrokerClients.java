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
package com.iohao.game.bolt.broker.core.client;

import com.iohao.game.action.skeleton.core.commumication.BrokerClientContext;
import lombok.experimental.UtilityClass;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Map;
import java.util.Objects;

/**
 * 管理 BrokerClient
 * <pre>
 *     管理逻辑服，默认情况使用 BrokerClient.id 与 BrokerClient 关联
 *
 *     具体阅读 BrokerClientStartup#startupSuccess 源码
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-15
 */
@UtilityClass
public class BrokerClients {
    final Map<String, BrokerClient> brokerClientMap = new NonBlockingHashMap<>();

    public void put(String id, BrokerClient brokerClient) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(brokerClient);

        brokerClientMap.put(id, brokerClient);
    }

    public BrokerClientContext getBrokerClient(String id) {
        return brokerClientMap.get(id);
    }
}
