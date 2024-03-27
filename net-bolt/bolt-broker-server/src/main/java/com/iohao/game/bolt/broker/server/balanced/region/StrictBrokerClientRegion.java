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

import com.iohao.game.action.skeleton.core.exception.ActionErrorEnum;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Map;
import java.util.Objects;

/**
 * 负载均衡，相同业务模块（逻辑服）的信息域
 * <pre>
 *     即同一个业务模块起了N个服务（来负载）
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-06-06
 */
public final class StrictBrokerClientRegion implements BrokerClientRegion {
    /**
     * 模块信息代理，这里的模块指的是逻辑服信息
     * <pre>
     *     key : 逻辑服 id
     *     value : 逻辑服代理
     * </pre>
     */
    final Map<Integer, BrokerClientProxy> brokerClientProxyMap = new NonBlockingHashMap<>();
    final String tag;
    WithElementSelector<BrokerClientProxy> withElementSelector;

    public StrictBrokerClientRegion(String tag) {
        this.tag = tag;
    }

    @Override
    public BrokerClientProxy getBrokerClientProxy(HeadMetadata headMetadata) {
        // 绑定的逻辑服 id
        int endPointClientId = headMetadata.getEndPointClientId();
        if (endPointClientId != 0) {
            // 通过绑定的逻辑服 id 来查找对应的游戏逻辑服
            BrokerClientProxy brokerClientProxy = this.brokerClientProxyMap.get(endPointClientId);

            if (Objects.isNull(brokerClientProxy)) {
                headMetadata.setOther(ActionErrorEnum.findBindingLogicServerNotExist);
            }

            return brokerClientProxy;
        }

        if (Objects.isNull(this.withElementSelector)) {
            return null;
        }

        return this.withElementSelector.next(headMetadata.getWithNo());
    }

    @Override
    public void add(BrokerClientProxy brokerClientProxy) {
        int id = brokerClientProxy.getIdHash();
        this.brokerClientProxyMap.put(id, brokerClientProxy);
        this.resetSelector();
    }

    @Override
    public void remove(int id) {
        this.brokerClientProxyMap.remove(id);
        this.resetSelector();
    }

    @Override
    public String getTag() {
        return this.tag;
    }

    @Override
    public int count() {
        return this.brokerClientProxyMap.size();
    }

    @Override
    public Map<Integer, BrokerClientProxy> getBrokerClientProxyMap() {
        return this.brokerClientProxyMap;
    }

    private void resetSelector() {
        this.withElementSelector = new DefaultWithElementSelector(this.brokerClientProxyMap);
    }
}