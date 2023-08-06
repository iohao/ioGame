/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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
package com.iohao.game.bolt.broker.core.client;

import com.alipay.remoting.ConnectionEventProcessor;
import com.alipay.remoting.ConnectionEventType;
import com.alipay.remoting.rpc.protocol.UserProcessor;
import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.bolt.broker.core.loadbalance.ElementSelector;
import com.iohao.game.bolt.broker.core.loadbalance.ElementSelectorFactory;
import com.iohao.game.bolt.broker.core.loadbalance.RandomElementSelector;
import com.iohao.game.common.kit.ExecutorKit;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
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
    final Map<String, BrokerClientItem> brokerClientItemMap = new NonBlockingHashMap<>();
    /** 连接 broker （游戏网关） 的地址 */
    BrokerAddress brokerAddress;
    /** 连接事件 */
    Map<ConnectionEventType, Supplier<ConnectionEventProcessor>> connectionEventProcessorMap;
    /** 用户处理器 */
    List<Supplier<UserProcessor<?>>> processorList;
    /** 业务框架 */
    BarSkeleton barSkeleton;
    /** 元素选择器生产工厂 */
    ElementSelectorFactory<BrokerClientItem> elementSelectorFactory = RandomElementSelector::new;
    /** BrokerClientItem 元素选择器 */
    ElementSelector<BrokerClientItem> elementSelector;
    /** 消息发送超时时间 */
    int timeoutMillis;
    BrokerClient brokerClient;

    BrokerClientItem brokerClientItemWith;

    public void init() {
        this.elementSelector = elementSelectorFactory.createElementSelector(Collections.emptyList());

        this.register(this.brokerAddress.getAddress());
    }

    public void register(String address) {
        BrokerClientItem brokerClientItem = new BrokerClientItem(address)
                .setTimeoutMillis(this.timeoutMillis)
                .setBarSkeleton(this.barSkeleton)
                .setBrokerClient(this.brokerClient)
                .setAwareInject(this.brokerClient.getAwareInject());

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
        this.brokerClientItemMap.put(address, brokerClientItem);
        // 生成负载对象
        this.resetSelector();
    }

    public void remove(String address) {
        if (IoGameGlobalConfig.openLog) {
            log.info("broker （游戏网关）的机器减少了 address : {}", address);
        }

        // 移除
        this.brokerClientItemMap.remove(address);

        // 生成负载对象
        this.resetSelector();

        if (IoGameGlobalConfig.openLog) {
            Set<String> keySet = brokerClientItemMap.keySet();
            log.info("当前网关数量 : {} {}", this.brokerClientItemMap.size(), keySet);
        }
    }

    private void a() {
        AtomicBoolean flag = new AtomicBoolean();
        if (flag.compareAndSet(false, true)) {
            ExecutorKit.newSingleScheduled("aa").scheduleAtFixedRate(() -> {
                Set<String> keySet = brokerClientItemMap.keySet();
                log.info("当前网关数量 : {} {}", this.brokerClientItemMap.size(), keySet);
            }, 1, 5, TimeUnit.SECONDS);
        }
    }

    public void remove(BrokerClientItem brokerClientItem) {
        brokerClientItem.setStatus(BrokerClientItem.Status.DISCONNECT);
        this.resetSelector();
    }

    void resetSelector() {
        // 生成负载对象；注意，这个 List 是不支持序列化的
        List<BrokerClientItem> brokerClientItems = brokerClientItemMap.values()
                .stream()
                .filter(brokerClientItem -> brokerClientItem.getStatus() == BrokerClientItem.Status.ACTIVE)
                .toList();

        // 重置负载对象
        this.elementSelector = this.elementSelectorFactory.createElementSelector(brokerClientItems);
    }

    public int countActiveItem() {
        return (int) brokerClientItemMap.values()
                .stream()
                .filter(brokerClientItem -> brokerClientItem.getStatus() == BrokerClientItem.Status.ACTIVE)
                .count();
    }

    public BrokerClientItem next() {
        if (Objects.nonNull(this.brokerClientItemWith)) {
            return this.brokerClientItemWith;
        }

        return elementSelector.next();
    }

    public List<BrokerClientItem> listBrokerClientItem() {
        return new ArrayList<>(brokerClientItemMap.values());
    }

    public boolean contains(String address) {
        return this.brokerClientItemMap.containsKey(address);
    }

    public Set<String> keySet() {
        return new HashSet<>(this.brokerClientItemMap.keySet());
    }

    public void forEach(Consumer<BrokerClientItem> consumer) {
        this.brokerClientItemMap.values()
                .stream()
                .filter(brokerClientItem -> brokerClientItem.getStatus() == BrokerClientItem.Status.ACTIVE)
                .forEach(consumer);
    }
}
