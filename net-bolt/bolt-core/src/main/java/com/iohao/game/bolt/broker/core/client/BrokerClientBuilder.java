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
package com.iohao.game.bolt.broker.core.client;

import com.alipay.remoting.ConnectionEventProcessor;
import com.alipay.remoting.ConnectionEventType;
import com.alipay.remoting.rpc.protocol.UserProcessor;
import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.protocol.processor.SimpleServerInfo;
import com.iohao.game.bolt.broker.core.aware.AwareInject;
import com.iohao.game.bolt.broker.core.client.config.BrokerClientStatusConfig;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.bolt.broker.core.common.processor.hook.ClientProcessorHooks;
import com.iohao.game.bolt.broker.core.common.processor.listener.BrokerClientListener;
import com.iohao.game.bolt.broker.core.common.processor.listener.BrokerClientListenerRegion;
import com.iohao.game.bolt.broker.core.message.BrokerClientModuleMessage;
import com.iohao.game.common.kit.HashKit;
import com.iohao.game.common.kit.NetworkKit;
import com.iohao.game.common.kit.exception.ThrowKit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jctools.maps.NonBlockingHashMap;
import org.jctools.maps.NonBlockingHashSet;

import java.util.*;
import java.util.function.Supplier;

/**
 * Bolt Broker Client （逻辑服）构建器
 * <pre>
 *     see {@link BrokerClient#newBuilder()} 创建构建器
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-14
 */
@Setter
@Getter
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrokerClientBuilder {
    /** 模拟的同进程 id */
    static final String ioGamePidRandom = UUID.randomUUID().toString();
    /** 连接处理器 */
    final Map<ConnectionEventType, Supplier<ConnectionEventProcessor>> connectionEventProcessorMap = new NonBlockingHashMap<>();
    /** 用户处理器 */
    final List<Supplier<UserProcessor<?>>> processorList = new ArrayList<>();
    /** 需要移除的用户处理器 */
    @Getter(value = AccessLevel.PRIVATE)
    final List<Class<?>> removeProcessorList = new ArrayList<>();
    final BrokerClientListenerRegion brokerClientListenerRegion = new BrokerClientListenerRegion();
    /**
     * 服务器唯一标识
     * <pre>
     *     如果没设置，会随机分配一个
     *
     *     逻辑服的模块id，标记不同的逻辑服模块。
     *     开发者随意定义，只要确保每个逻辑服的模块 id 不相同就可以
     * </pre>
     */
    String id;
    /**
     * 逻辑服标签 （tag 相当于归类）
     * <pre>
     *     用于逻辑服的归类
     *     假设逻辑服： 战斗逻辑服 启动了两台或以上，为了得到启动连接的逻辑服，我们可以通过 tag 在后台查找
     *     相同的逻辑服一定要用相同的 tag
     *
     *      注意，如果没设置这个值，会使用 this.appName 的值
     * </pre>
     */
    String tag;
    /**
     * 模块名（逻辑服名）
     * <pre>
     *     注意，如果没设置 tag，此名也会是 tag 名
     *     see: {@link BrokerClientBuilder#tag}
     * </pre>
     */
    String appName;
    /** 业务框架 */
    BarSkeleton barSkeleton;
    /** 逻辑服类型 */
    BrokerClientType brokerClientType = BrokerClientType.LOGIC;
    /** 连接 broker （游戏网关） 的地址 */
    BrokerAddress brokerAddress;

    /** 消息发送超时时间 */
    int timeoutMillis = IoGameGlobalConfig.timeoutMillis;

    /** bolt 业务处理器的钩子管理器 */
    ClientProcessorHooks clientProcessorHooks;
    /** 管理 bolt client */
    BrokerClientManager brokerClientManager;
    /** aware 注入扩展 */
    AwareInject awareInject;
    /** 逻辑服状态 */
    int status = BrokerClientStatusConfig.normal;
    int withNo;

    BrokerClientBuilder() {
    }

    /**
     * 添加连接器
     *
     * @param type              type
     * @param processorSupplier 连接器
     * @return this
     */
    public BrokerClientBuilder addConnectionEventProcessor(ConnectionEventType type, Supplier<ConnectionEventProcessor> processorSupplier) {
        this.connectionEventProcessorMap.put(type, processorSupplier);
        return this;
    }

    /**
     * 注册处理器
     *
     * @param processorSupplier 处理器
     * @return this
     */
    public BrokerClientBuilder registerUserProcessor(Supplier<UserProcessor<?>> processorSupplier) {
        this.processorList.add(processorSupplier);
        return this;
    }

    /**
     * 移除不需要的处理器
     *
     * @param processorClass 处理器
     * @return this
     */
    public BrokerClientBuilder removeUserProcessor(Class<? extends UserProcessor<?>> processorClass) {
        this.removeProcessorList.add(processorClass);
        return this;
    }

    /**
     * 添加监听
     *
     * @param listener listener
     * @return this
     */
    public BrokerClientBuilder addListener(BrokerClientListener listener) {
        this.brokerClientListenerRegion.add(listener);
        return this;
    }

    /**
     * 构建 BrokerClient
     *
     * @param brokerAddress 连接 broker （游戏网关） 的地址
     * @return BrokerClient
     */
    public BrokerClient build(BrokerAddress brokerAddress) {
        this.brokerAddress = brokerAddress;
        return build();
    }

    public BrokerClient build() {

        this.settingDefaultValue();

        // 模块信息 （子游戏服的信息、逻辑服）
        BrokerClientModuleMessage brokerClientModuleMessage = this.createBrokerClientMessage();
        SimpleServerInfo simpleServerInfo = this.createSimpleServerInfo();

        BrokerClient brokerClient = new BrokerClient()
                .setId(this.id)
                .setTag(this.tag)
                .setAppName(this.appName)
                .setBarSkeleton(this.barSkeleton)
                .setBrokerAddress(this.brokerAddress)
                .setBrokerClientType(this.brokerClientType)
                .setBrokerClientModuleMessage(brokerClientModuleMessage)
                .setBrokerClientListenerRegion(this.brokerClientListenerRegion)
                .setTimeoutMillis(this.timeoutMillis)
                .setConnectionEventProcessorMap(this.connectionEventProcessorMap)
                .setProcessorList(this.processorList)
                .setClientProcessorHooks(this.clientProcessorHooks)
                .setBrokerClientManager(this.brokerClientManager)
                .setSimpleServerInfo(simpleServerInfo)
                .setAwareInject(this.awareInject)
                .setStatus(this.status);

        brokerClient.option(BrokerClientAttr.onlineListenerRecordSet, new NonBlockingHashSet<>());
        brokerClient.option(BrokerClientAttr.offlineListenerRecordSet, new NonBlockingHashSet<>());
        brokerClient.setWithNo(this.withNo);

        // 保存一下 BrokerClient 的引用
        if (this.brokerClientType == BrokerClientType.LOGIC) {
            BrokerClientHelper.brokerClient = brokerClient;
        }

        return brokerClient;
    }

    private void settingDefaultValue() {

        if (Objects.isNull(this.id)) {
            this.id = UUID.randomUUID().toString();
        }

        if (Objects.isNull(this.appName)) {
            ThrowKit.ofRuntimeException("必须设置 appName");
        }

        if (Objects.isNull(this.tag)) {
            this.tag = this.appName;
        }

        if (Objects.isNull(this.clientProcessorHooks)) {
            // 因为目前 clientProcess 的 hook 只有一个，暂时这样处理着
            this.clientProcessorHooks = new ClientProcessorHooks();
        }

        // 处理分布式事件总线的 listener
        this.addListener(new EventBusBrokerClientListener());

        for (Class<?> removeClass : this.removeProcessorList) {
            Iterator<Supplier<UserProcessor<?>>> iterator = this.processorList.iterator();
            while (iterator.hasNext()) {
                Supplier<UserProcessor<?>> next = iterator.next();
                Class<?> processorClass = next.get().getClass();

                if (Objects.equals(processorClass, removeClass)) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    private List<Integer> listCmdMerge() {
        // 业务框架
        if (Objects.nonNull(barSkeleton)) {
            // 设置模块包含的 cmd 列表
            var actionCommandRegions = barSkeleton.getActionCommandRegions();
            return actionCommandRegions.listCmdMerge();
        }

        return Collections.emptyList();
    }

    /**
     * 初始化 模块信息
     *
     * @return 模块信息
     */
    private BrokerClientModuleMessage createBrokerClientMessage() {
        var cmdMergeList = this.listCmdMerge();

        return new BrokerClientModuleMessage()
                .setId(this.id)
                .setName(this.appName)
                .setAddress(NetworkKit.LOCAL_IP)
                .setCmdMergeList(cmdMergeList)
                .setBrokerClientType(this.brokerClientType)
                .setTag(this.tag)
                .setStatus(this.status)
                .setWithNo(this.withNo)
                .setIoGamePid(ioGamePidRandom)
                ;
    }

    private SimpleServerInfo createSimpleServerInfo() {
        SimpleServerInfo simpleServerInfo = new SimpleServerInfo();
        simpleServerInfo.setId(this.id);
        simpleServerInfo.setName(this.appName);
        simpleServerInfo.setTag(this.tag);
        simpleServerInfo.setIdHash(HashKit.hash32(this.id));
        simpleServerInfo.setBrokerClientType(this.brokerClientType.name());
        simpleServerInfo.setStartTime(System.currentTimeMillis());
        simpleServerInfo.setStatus(this.status);

        return simpleServerInfo;
    }
}
