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
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        map.put(externalId, brokerClientProxy);

        this.resetSelector();
    }

    @Override
    public void remove(BrokerClientProxy brokerClientProxy) {

        int externalId = brokerClientProxy.getIdHash();

        map.remove(externalId);

        this.resetSelector();
    }


    public BrokerClientProxy get(int externalId) {
        return this.map.get(externalId);
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
        list = new CopyOnWriteArrayList<>(map.values());
    }
}
