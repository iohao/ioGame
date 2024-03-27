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
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 对外服的管理器
 *
 * @author 渔民小镇
 * @date 2022-05-14
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExternalBrokerClientLoadBalanced implements BrokerClientLoadBalanced {
    /**
     * 对外服的关联 map
     * <pre>
     *     key : external id
     *     value : external info
     * </pre>
     */
    final Map<Integer, BrokerClientProxy> map = new NonBlockingHashMap<>();
    /** 对外服 list */
    List<BrokerClientProxy> list = Collections.emptyList();

    @Override
    public void register(BrokerClientProxy brokerClientProxy) {

        int externalId = brokerClientProxy.getIdHash();
        this.map.put(externalId, brokerClientProxy);

        this.resetSelector();
    }

    @Override
    public void remove(BrokerClientProxy brokerClientProxy) {

        int externalId = brokerClientProxy.getIdHash();

        this.map.remove(externalId);

        this.resetSelector();
    }

    public BrokerClientProxy get(int externalId) {
        return this.map.get(externalId);
    }

    public boolean contains(int externalId) {
        return this.map.containsKey(externalId);
    }

    /**
     * 所有的对外服
     *
     * @return 对外服列表
     */
    public List<BrokerClientProxy> listBrokerClientProxy() {
        return this.list;
    }

    public int count() {
        return this.map.size();
    }

    private void resetSelector() {
        this.list = new CopyOnWriteArrayList<>(map.values());
    }
}
