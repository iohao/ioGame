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
import com.iohao.game.bolt.broker.core.loadbalance.ElementSelector;
import com.iohao.game.bolt.broker.core.loadbalance.RandomElementSelector;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 负载均衡，相同业务模块（逻辑服）的信息域
 * <pre>
 *     即同一个业务模块起了N个服务（来负载）
 *
 *     如果绑定的游戏逻辑服的，在没有找到时会取一个其他的游戏逻辑服来处理。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-16
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
public class DefaultBrokerClientRegion implements BrokerClientRegion {
    /**
     * 模块信息代理，这里的模块指的是逻辑服信息
     * <pre>
     *     key : 逻辑服 id
     *     value : 逻辑服代理
     * </pre>
     */
    @Getter
    final Map<Integer, BrokerClientProxy> brokerClientProxyMap = new NonBlockingHashMap<>();
    final String tag;

    ElementSelector<BrokerClientProxy> elementSelector;

    public DefaultBrokerClientRegion(String tag) {
        this.tag = tag;
    }

    @Override
    public BrokerClientProxy getBrokerClientProxy(HeadMetadata headMetadata) {
        int endPointClientId = headMetadata.getEndPointClientId();

        // 得到指定的逻辑服
        if (endPointClientId != 0 && this.brokerClientProxyMap.containsKey(endPointClientId)) {

            /*
             * 查看当前 endPointClientId 是否属于当前 Region
             *
             * 理论上需要保存一下所有"注册过"的游戏逻辑服id，
             * 因为动态绑定逻辑服时，玩家绑定的逻辑服有可能关闭了或下线了，
             * 这种情况应该返回 null ，这样可以通知对外服， 路由不存在，
             * 但目前先不做这样的判断。
             */

            // 如果找到了就返回，没找到则使用继续往下找
            BrokerClientProxy brokerClientProxy = this.brokerClientProxyMap.get(endPointClientId);
            if (Objects.nonNull(brokerClientProxy)) {
                return brokerClientProxy;
            }
        }

        if (Objects.isNull(this.elementSelector)) {
            return null;
        }

        // 随机选一个逻辑服
        return this.elementSelector.get();
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

    private void resetSelector() {
        // 随机选择器
        List<BrokerClientProxy> list = new ArrayList<>(brokerClientProxyMap.values());
        this.elementSelector = new RandomElementSelector<>(list);
    }
}
