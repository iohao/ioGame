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

import com.iohao.game.bolt.broker.core.loadbalance.ElementSelector;
import com.iohao.game.common.kit.CollKit;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author 渔民小镇
 * @date 2023-06-10
 */
final class DefaultWithElementSelector implements WithElementSelector<BrokerClientProxy> {

    int size;

    /**
     * <pre>
     *     key : withNo
     *     value : 逻辑服代理
     * </pre>
     */
    Map<Integer, List<BrokerClientProxy>> map;
    ElementSelector<BrokerClientProxy> elementSelector;
    final AtomicLong counter = new AtomicLong();

    public DefaultWithElementSelector(Map<Integer, BrokerClientProxy> proxyMap) {
        List<BrokerClientProxy> list = proxyMap.values().stream().filter(Objects::nonNull).toList();
        if ((size = list.size()) == 0) {
            return;
        }

        elementSelector = ElementSelector.of(list);
        this.map = new HashMap<>();

        for (BrokerClientProxy brokerClientProxy : list) {
            int withNo = brokerClientProxy.getWithNo();

            if (withNo == 0) {
                continue;
            }

            List<BrokerClientProxy> withList = this.map.get(withNo);
            if (Objects.isNull(withList)) {
                withList = new ArrayList<>();
                this.map.put(withNo, withList);
            }

            withList.add(brokerClientProxy);
        }
    }

    @Override
    public BrokerClientProxy next(int withNo) {

        if (size == 0) {
            return null;
        }

        if (withNo == 0) {
            // 随机选一个逻辑服
            return this.elementSelector.get();
        }

        var withList = this.map.get(withNo);
        if (CollKit.isEmpty(withList)) {
            var brokerClientProxy = withList.get((int) (counter.getAndIncrement() % withList.size()));
            if (Objects.nonNull(brokerClientProxy)) {
                return brokerClientProxy;
            }
        }

        // 随机选一个逻辑服
        return this.elementSelector.next();
    }
}
