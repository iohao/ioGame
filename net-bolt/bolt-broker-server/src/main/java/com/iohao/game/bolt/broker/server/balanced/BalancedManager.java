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

import com.alipay.remoting.rpc.RpcServer;
import com.iohao.game.bolt.broker.core.client.BrokerClientType;
import com.iohao.game.bolt.broker.core.message.BrokerClientModuleMessage;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientProxy;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientRegion;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 负载管理器
 * <pre>
 *     对外服和游戏逻辑服的负载相关管理
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-12
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BalancedManager {
    /** 逻辑服 负载均衡 */
    @Getter
    final LogicBrokerClientLoadBalanced logicBalanced = new LogicBrokerClientLoadBalanced();
    /** 对外服 负载均衡 */
    @Getter
    final ExternalBrokerClientLoadBalanced externalLoadBalanced = new ExternalBrokerClientLoadBalanced();

    final Map<String, BrokerClientProxy> refMap = new NonBlockingHashMap<>();

    final BrokerServer brokerServer;

    public BalancedManager(BrokerServer brokerServer) {
        this.brokerServer = brokerServer;
    }

    /**
     * 根据 brokerClient 类型，得到对应的负载器
     *
     * @param brokerClientType brokerClientType
     * @return 负载器
     */
    public BrokerClientLoadBalanced getRegionLoadBalanced(BrokerClientType brokerClientType) {
        if (brokerClientType == BrokerClientType.EXTERNAL) {
            return this.externalLoadBalanced;
        }

        return this.logicBalanced;
    }

    public void register(BrokerClientModuleMessage brokerClientModuleMessage) {

        // 得到游戏逻辑服或对外服的负载器
        var brokerClientType = brokerClientModuleMessage.getBrokerClientType();
        var loadBalanced = this.getRegionLoadBalanced(brokerClientType);

        String address = brokerClientModuleMessage.getAddress();

        // broker client
        RpcServer rpcServer = brokerServer.getRpcServer();
        BrokerClientProxy brokerClientProxy = new BrokerClientProxy(brokerClientModuleMessage, rpcServer);

        loadBalanced.register(brokerClientProxy);

        brokerClientProxy.setCmdMergeList(null);
        brokerClientModuleMessage.setCmdMergeList(null);

        refMap.put(address, brokerClientProxy);
    }

    public BrokerClientProxy remove(String address) {
        BrokerClientProxy brokerClientProxy = this.refMap.get(address);

        BrokerClientType brokerClientType = brokerClientProxy.getBrokerClientType();
        var loadBalanced = this.getRegionLoadBalanced(brokerClientType);

        // 根据 address 来移除逻辑服（对外服或游戏逻辑服）
        loadBalanced.remove(brokerClientProxy);

        return brokerClientProxy;
    }

    /**
     * 得到逻辑服和对外服的列表
     *
     * @return client list
     */
    public List<BrokerClientProxy> listBrokerClientProxy() {

        // 当前网关的所有逻辑服
        List<BrokerClientProxy> list = new ArrayList<>(16);

        // 游戏对外服
        var externalProxyList = this.externalLoadBalanced.listBrokerClientProxy();
        list.addAll(externalProxyList);

        // 游戏逻辑服
        Collection<BrokerClientRegion> brokerClientRegions = this.logicBalanced.listBrokerClientRegion();
        for (BrokerClientRegion brokerClientRegion : brokerClientRegions) {
            var logicProxyList = brokerClientRegion.listBrokerClientProxy();
            list.addAll(logicProxyList);
        }

        return list;
    }

}
