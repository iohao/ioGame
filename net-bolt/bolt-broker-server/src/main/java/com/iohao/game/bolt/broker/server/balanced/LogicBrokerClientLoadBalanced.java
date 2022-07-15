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
package com.iohao.game.bolt.broker.server.balanced;

import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientProxy;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientRegion;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientRegionFactory;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * 逻辑服域的负载均衡
 *
 * @author 渔民小镇
 * @date 2022-05-14
 */
@Slf4j
public class LogicBrokerClientLoadBalanced implements BrokerClientLoadBalanced {

    /**
     * 路由与逻辑服域的关联
     * <pre>
     *     key : cmdMerge
     *     value : BoltClientRegion
     * </pre>
     */
    final Map<Integer, BrokerClientRegion> cmdBoltClientRegionMap = new NonBlockingHashMap<>();

    /**
     * 逻辑服tag 与逻辑服域的关联
     * <pre>
     *     key : tag
     *     value : BoltClientRegion
     * </pre>
     */
    final Map<String, BrokerClientRegion> tagBoltClientRegionMap = new NonBlockingHashMap<>();

    final Map<Integer, BrokerClientProxy> serverIdBoltClientProxyMap = new NonBlockingHashMap<>();

    @Setter
    BrokerClientRegionFactory brokerClientRegionFactory;

    @Override
    public void register(BrokerClientProxy brokerClientProxy) {

        // 相同业务模块（逻辑服）的信息域
        String tag = brokerClientProxy.getTag();

        BrokerClientRegion brokerClientRegion = getBoltClientRegionByTag(tag);
        brokerClientRegion.add(brokerClientProxy);

        // 路由与逻辑服域的关联
        var cmdMergeList = brokerClientProxy.getCmdMergeList();
        for (Integer cmdMerge : cmdMergeList) {
            this.cmdBoltClientRegionMap.put(cmdMerge, brokerClientRegion);
        }

        this.serverIdBoltClientProxyMap.put(brokerClientProxy.getIdHash(), brokerClientProxy);
    }

    @Override
    public void remove(BrokerClientProxy brokerClientProxy) {

        int id = brokerClientProxy.getIdHash();

        // 相同业务模块（逻辑服）的信息域
        String tag = brokerClientProxy.getTag();
        BrokerClientRegion brokerClientRegion = getBoltClientRegionByTag(tag);
        brokerClientRegion.remove(id);

        this.serverIdBoltClientProxyMap.remove(brokerClientProxy.getIdHash());
    }

    public BrokerClientRegion getBoltClientRegion(int cmdMerge) {
        // 通过 路由信息 得到对应的逻辑服列表（域）
        BrokerClientRegion region = this.cmdBoltClientRegionMap.get(cmdMerge);

        if (Objects.isNull(region)) {
            return null;
        }

        return region;
    }

    public Collection<BrokerClientRegion> listBrokerClientRegion() {
        return this.tagBoltClientRegionMap.values();
    }

    public BrokerClientProxy getBrokerClientProxyByIdHash(int idHash) {
        return this.serverIdBoltClientProxyMap.get(idHash);
    }

    private BrokerClientRegion getBoltClientRegionByTag(String tag) {
        BrokerClientRegion brokerClientRegion = tagBoltClientRegionMap.get(tag);

        // 无锁化
        if (Objects.isNull(brokerClientRegion)) {
            brokerClientRegion = brokerClientRegionFactory.createBrokerClientRegion(tag);
            brokerClientRegion = tagBoltClientRegionMap.putIfAbsent(tag, brokerClientRegion);
            if (Objects.isNull(brokerClientRegion)) {
                brokerClientRegion = tagBoltClientRegionMap.get(tag);
            }
        }

        return brokerClientRegion;
    }

}
