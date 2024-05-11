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

import com.alipay.remoting.rpc.RpcServer;
import com.iohao.game.bolt.broker.core.client.BrokerClientType;
import com.iohao.game.bolt.broker.core.message.BrokerClientModuleMessage;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientProxy;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientRegion;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingHashMap;

import java.util.*;

/**
 * 负载管理器
 * <pre>
 *     对外服和游戏逻辑服的负载相关管理
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-12
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BalancedManager {
    /** 逻辑服 负载均衡 */
    @Getter
    final LogicBrokerClientLoadBalanced logicBalanced = new LogicBrokerClientLoadBalanced();
    /** 对外服 负载均衡 */
    @Getter
    final ExternalBrokerClientLoadBalanced externalLoadBalanced = new ExternalBrokerClientLoadBalanced();
    /** key:address value:proxy */
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
        RpcServer rpcServer = this.brokerServer.getRpcServer();
        BrokerClientProxy brokerClientProxy = new BrokerClientProxy(brokerClientModuleMessage, rpcServer);

        loadBalanced.register(brokerClientProxy);

        brokerClientProxy.setCmdMergeList(null);

        this.refMap.put(address, brokerClientProxy);
    }

    public BrokerClientProxy remove(String address) {
        BrokerClientProxy brokerClientProxy = this.refMap.get(address);

        if (Objects.isNull(brokerClientProxy)) {
            return null;
        }

        BrokerClientType brokerClientType = brokerClientProxy.getBrokerClientType();
        var loadBalanced = this.getRegionLoadBalanced(brokerClientType);

        // 根据 address 来移除逻辑服（对外服或游戏逻辑服）
        loadBalanced.remove(brokerClientProxy);

        return brokerClientProxy;
    }

    /**
     * 得到游戏逻辑服和游戏对外服的列表
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
