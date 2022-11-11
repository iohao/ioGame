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
import com.iohao.game.bolt.broker.core.loadbalance.ElementSelector;
import com.iohao.game.bolt.broker.core.loadbalance.RandomElementSelector;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;

import java.util.*;

/**
 * 负载均衡，相同业务模块（逻辑服）的信息域
 * <pre>
 *     即同一个业务模块起了N个服务（来负载）
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-16
 */
@Slf4j
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
    final Map<Integer, BrokerClientProxy> boltClientProxyMap = new NonBlockingHashMap<>();
    final String tag;

    ElementSelector<BrokerClientProxy> elementSelector;

    public DefaultBrokerClientRegion(String tag) {
        this.tag = tag;
    }

    @Override
    public BrokerClientProxy getBoltClientProxy(HeadMetadata headMetadata) {
        int endPointClientId = headMetadata.getEndPointClientId();

        // 得到指定的逻辑服
        if (endPointClientId != 0 && boltClientProxyMap.containsKey(endPointClientId)) {

            /*
             * 查看当前 endPointClientId 是否属于当前 Region
             *
             * 理论上需要保存一下所有"注册过"的游戏逻辑服id，
             * 因为动态绑定逻辑服时，玩家绑定的逻辑服有可能关闭了或下线了，
             * 这种情况应该返回 null ，这样可以通知对外服， 路由不存在，
             * 但目前先不做这样的判断。
             */

            // 如果找到了就返回，没找到则使用继续往下找
            BrokerClientProxy brokerClientProxy = this.boltClientProxyMap.get(endPointClientId);
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
        this.boltClientProxyMap.put(id, brokerClientProxy);
        this.resetSelector();
    }

    @Override
    public void remove(int id) {
        this.boltClientProxyMap.remove(id);
        this.resetSelector();
    }

    @Override
    public String getTag() {
        return this.tag;
    }

    @Override
    public int count() {
        return this.boltClientProxyMap.size();
    }

    private void resetSelector() {
        // 随机选择器
        List<BrokerClientProxy> list = new ArrayList<>(boltClientProxyMap.values());
        elementSelector = new RandomElementSelector<>(list);
    }
}
