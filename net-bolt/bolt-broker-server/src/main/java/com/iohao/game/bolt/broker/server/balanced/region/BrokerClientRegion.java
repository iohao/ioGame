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
package com.iohao.game.bolt.broker.server.balanced.region;

import com.iohao.game.action.skeleton.protocol.HeadMetadata;

import java.util.Collection;
import java.util.Map;

/**
 * 负载均衡，相同业务模块（逻辑服）的信息域
 * <pre>
 *     即同一个业务模块起了N个服务（来负载）
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-16
 */
public interface BrokerClientRegion {
    /**
     * tag
     *
     * @return tag
     */
    String getTag();

    /**
     * 当前逻辑服域有多少个逻辑服
     *
     * @return 统计
     */
    default int count() {
        return this.getBrokerClientProxyMap().size();
    }

    /**
     * 添加逻辑服
     *
     * @param brokerClientProxy 逻辑服信息
     */
    void add(BrokerClientProxy brokerClientProxy);

    /**
     * 根据请求元信息得到一个 逻辑服
     *
     * @param headMetadata 请求元信息
     * @return 逻辑服信息
     */
    BrokerClientProxy getBrokerClientProxy(HeadMetadata headMetadata);

    /**
     * BrokerClientProxy map
     *
     * @return map
     */
    Map<Integer, BrokerClientProxy> getBrokerClientProxyMap();

    /**
     * 域下的所有 逻辑服
     *
     * @return 逻辑服列表
     */
    default Collection<BrokerClientProxy> listBrokerClientProxy() {
        /*
         * NonBlockingHashMap 迭代器是一个“弱一致性”迭代器，
         * 它永远不会抛出 ConcurrentModificationException
         */
        return getBrokerClientProxyMap().values();
    }

    /**
     * 根据 id 删除逻辑服
     *
     * @param id id
     */
    void remove(int id);

    /**
     * doAnything
     *
     * @param value value
     */
    default void doAnything(Object value) {
    }
}
