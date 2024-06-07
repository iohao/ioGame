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
package com.iohao.game.action.skeleton.eventbus;

import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.SkeletonAttr;
import com.iohao.game.action.skeleton.core.commumication.BrokerClientContext;
import com.iohao.game.action.skeleton.core.runner.Runner;
import com.iohao.game.action.skeleton.protocol.processor.SimpleServerInfo;

import java.util.Set;

/**
 * 分布式事件总线 Runner
 *
 * @author 渔民小镇
 * @date 2024-06-06
 * @since 21.10
 */
public interface EventBusRunner extends Runner {
    @Override
    default void onStart(BarSkeleton skeleton) {
        // BrokerClient，当前逻辑服引用
        BrokerClientContext brokerClientContext = skeleton.option(SkeletonAttr.brokerClientContext);
        String brokerClientId = brokerClientContext.getId();

        EventBrokerClientMessage eventBrokerClientMessage = getEventBrokerClientMessage(brokerClientContext);

        // EventBus 是逻辑服事件总线。 EventBus、业务框架、逻辑服三者是 1:1:1 的关系。
        EventBus eventBus = new EventBus(brokerClientId);
        skeleton.option(SkeletonAttr.eventBus, eventBus);

        // EventBus 默认设置
        eventBus.setSubscribeExecutorStrategy(SubscribeExecutorStrategy.defaultInstance());
        eventBus.setSubscriberInvokeCreator(SubscriberInvokeCreator.defaultInstance());
        eventBus.setEventBusMessageCreator(EventBusMessageCreator.defaultInstance());
        eventBus.setEventBusListener(EventBusListener.defaultInstance());
        eventBus.setExecutorRegion(skeleton.getExecutorRegion());

        eventBus.setBrokerClientContext(brokerClientContext);
        eventBus.setEventBrokerClientMessage(eventBrokerClientMessage);

        // EventBus 注册订阅者
        this.registerEventBus(eventBus, skeleton);

        Set<String> topic = eventBus.listTopic();
        eventBrokerClientMessage.setEventTopicMessage(new EventTopicMessage(topic));

        eventBus.setStatus(EventBus.EventBusStatus.run);

        EventBusRegion.addLocal(eventBus);
    }

    private EventBrokerClientMessage getEventBrokerClientMessage(BrokerClientContext brokerClientContext) {
        SimpleServerInfo simpleServerInfo = brokerClientContext.getSimpleServerInfo();
        String id = simpleServerInfo.getId();
        String appName = simpleServerInfo.getName();
        String tag = simpleServerInfo.getTag();
        String typeName = simpleServerInfo.getBrokerClientType();

        return new EventBrokerClientMessage(appName, tag, typeName, id);
    }

    /**
     * 可在此方法中注册订阅者
     * example
     * <pre>{@code
     *     eventBus.register(new YourEventBusSubscriber());
     * }
     * </pre>
     *
     * @param eventBus EventBus
     * @param skeleton 业务框架
     */
    void registerEventBus(EventBus eventBus, BarSkeleton skeleton);
}
