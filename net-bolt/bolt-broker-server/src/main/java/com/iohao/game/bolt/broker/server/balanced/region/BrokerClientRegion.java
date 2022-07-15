/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
    int count();

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
    BrokerClientProxy getBoltClientProxy(HeadMetadata headMetadata);

    /**
     * BrokerClientProxy map
     *
     * @return map
     */
    Map<Integer, BrokerClientProxy> getBoltClientProxyMap();

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
        return getBoltClientProxyMap().values();
    }

    /**
     * 根据 id 删除逻辑服
     *
     * @param id id
     */
    void remove(int id);
}
