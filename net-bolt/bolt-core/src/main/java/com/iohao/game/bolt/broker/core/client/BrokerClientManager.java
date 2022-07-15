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
package com.iohao.game.bolt.broker.core.client;

import com.alipay.remoting.ConnectionEventProcessor;
import com.alipay.remoting.ConnectionEventType;
import com.alipay.remoting.rpc.protocol.UserProcessor;
import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.bolt.broker.core.common.BrokerGlobalConfig;
import com.iohao.game.bolt.broker.core.loadbalance.ElementSelector;
import com.iohao.game.bolt.broker.core.loadbalance.RandomElementSelector;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;

import java.util.*;
import java.util.function.Supplier;

/**
 * 管理 bolt client ， 接收新的集群信息，并增减相关 bolt client
 *
 * <pre>
 *     BrokerClientItem 与游戏网关是 1:1 的关系，
 *     如果启动了 N 个网关，那么 BrokerClientManager 下的 BrokerClientItem 就会有 N 个。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-14
 */
@Slf4j
@Getter
@Setter
@Accessors(chain = true)
public final class BrokerClientManager {

    /**
     * <pre>
     *     key : address ，broker 的地址，格式：ip:port
     *     value : 与 broker 建立连接的 bolt client
     * </pre>
     */
    final Map<String, BrokerClientItem> boltClientMap = new NonBlockingHashMap<>();

    /** 连接 broker （游戏网关） 的地址 */
    BrokerAddress brokerAddress;
    /** 连接事件 */
    Map<ConnectionEventType, Supplier<ConnectionEventProcessor>> connectionEventProcessorMap;
    /** 用户处理器 */
    List<Supplier<UserProcessor<?>>> processorList;
    /** 业务框架 */
    BarSkeleton barSkeleton;
    ElementSelector<BrokerClientItem> randomElementSelector = new RandomElementSelector<>(Collections.emptyList());
    /** 消息发送超时时间 */
    int timeoutMillis;
    BrokerClient brokerClient;

    public void init() {
        this.register(this.brokerAddress.getAddress());
    }

    public boolean contains(String address) {
        return this.boltClientMap.containsKey(address);
    }

    public void register(String address) {
        BrokerClientItem brokerClientItem = new BrokerClientItem(address)
                .setTimeoutMillis(this.timeoutMillis)
                .setBarSkeleton(this.barSkeleton)
                .setBrokerClient(this.brokerClient);

        // 添加连接处理器
        connectionEventProcessorMap.forEach((type, valueSupplier) -> {
            var processor = valueSupplier.get();
            brokerClientItem.addConnectionEventProcessor(type, processor);
        });

        // 注册用户处理器
        processorList.stream()
                .map(Supplier::get)
                .forEach(brokerClientItem::registerUserProcessor);

        // 初始化
        brokerClientItem.startup();

        // 添加映射关系
        boltClientMap.put(address, brokerClientItem);

        // 生成负载对象
        this.resetSelector();
    }

    public Set<String> keySet() {
        return new HashSet<>(this.boltClientMap.keySet());
    }

    public void remove(String address) {
        if (BrokerGlobalConfig.openLog) {
            log.info("broker （游戏网关）的机器减少了 address : {}", address);
        }

        // 移除
        this.boltClientMap.remove(address);

        // 生成负载对象
        this.resetSelector();

        if (BrokerGlobalConfig.openLog) {
            log.info("当前网关数量 : {}", this.boltClientMap.size());
        }

        // TODO: 2022/5/13 这里重连需要注意集群与单机的情况

    }

    public void remove(BrokerClientItem brokerClientItem) {
        brokerClientItem.setStatus(BrokerClientItem.Status.DISCONNECT);
        this.resetSelector();
    }

    void resetSelector() {
        // 生成负载对象
        List<BrokerClientItem> list = boltClientMap.values()
                .stream()
                .filter(brokerClientItem -> brokerClientItem.getStatus() == BrokerClientItem.Status.ACTIVE)
                .toList();

        List<BrokerClientItem> brokerClientItems = new ArrayList<>(list);
        randomElementSelector = new RandomElementSelector<>(brokerClientItems);
    }

    public int countActiveItem() {
        return boltClientMap.values()
                .stream()
                .filter(brokerClientItem -> brokerClientItem.getStatus() == BrokerClientItem.Status.ACTIVE)
                .toList()
                .size()
                ;
    }

    public BrokerClientItem next() {
        return randomElementSelector.next();
    }

    public List<BrokerClientItem> listBrokerClientItem() {
        return new ArrayList<>(this.boltClientMap.values());
    }
}
