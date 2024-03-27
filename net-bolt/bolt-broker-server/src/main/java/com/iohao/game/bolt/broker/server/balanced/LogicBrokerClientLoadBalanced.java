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
package com.iohao.game.bolt.broker.server.balanced;

import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientProxy;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientRegion;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientRegionFactory;
import com.iohao.game.common.kit.MoreKit;
import lombok.Setter;
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
public final class LogicBrokerClientLoadBalanced implements BrokerClientLoadBalanced {

    /**
     * 路由与逻辑服域的关联
     * <pre>
     *     key : cmdMerge
     *     value : BrokerClientRegion
     * </pre>
     */
    final Map<Integer, BrokerClientRegion> cmdClientRegionMap = new NonBlockingHashMap<>();

    /**
     * 逻辑服tag 与逻辑服域的关联
     * <pre>
     *     key : tag
     *     value : BrokerClientRegion
     * </pre>
     */
    final Map<String, BrokerClientRegion> tagClientRegionMap = new NonBlockingHashMap<>();

    final Map<Integer, BrokerClientProxy> serverIdClientProxyMap = new NonBlockingHashMap<>();

    @Setter
    BrokerClientRegionFactory brokerClientRegionFactory;

    @Override
    public void register(BrokerClientProxy brokerClientProxy) {

        // 相同业务模块（逻辑服）的信息域
        String tag = brokerClientProxy.getTag();

        BrokerClientRegion brokerClientRegion = getBrokerClientRegionByTag(tag);
        brokerClientRegion.add(brokerClientProxy);

        // 路由与逻辑服域的关联
        var cmdMergeList = brokerClientProxy.getCmdMergeList();
        for (Integer cmdMerge : cmdMergeList) {
            this.cmdClientRegionMap.put(cmdMerge, brokerClientRegion);
        }

        this.serverIdClientProxyMap.put(brokerClientProxy.getIdHash(), brokerClientProxy);
    }

    @Override
    public void remove(BrokerClientProxy brokerClientProxy) {

        int id = brokerClientProxy.getIdHash();

        // 相同业务模块（逻辑服）的信息域
        String tag = brokerClientProxy.getTag();
        BrokerClientRegion brokerClientRegion = getBrokerClientRegionByTag(tag);
        brokerClientRegion.remove(id);

        this.serverIdClientProxyMap.remove(brokerClientProxy.getIdHash());
    }

    /**
     * get BrokerClientRegion
     *
     * @param cmdMerge cmdMerge
     * @return BrokerClientRegion
     */
    public BrokerClientRegion getBrokerClientRegion(int cmdMerge) {
        // 通过 路由信息 得到对应的逻辑服列表（域）
        BrokerClientRegion region = this.cmdClientRegionMap.get(cmdMerge);

        if (Objects.isNull(region)) {
            return null;
        }

        return region;
    }

    public Collection<BrokerClientRegion> listBrokerClientRegion() {
        return this.tagClientRegionMap.values();
    }

    public BrokerClientProxy getBrokerClientProxyByIdHash(int idHash) {
        return this.serverIdClientProxyMap.get(idHash);
    }

    private BrokerClientRegion getBrokerClientRegionByTag(String tag) {
        BrokerClientRegion brokerClientRegion = this.tagClientRegionMap.get(tag);

        // 无锁化
        if (Objects.isNull(brokerClientRegion)) {
            BrokerClientRegion newValue = this.brokerClientRegionFactory.createBrokerClientRegion(tag);
            return MoreKit.putIfAbsent(tagClientRegionMap, tag, newValue);
        }

        return brokerClientRegion;
    }

}
