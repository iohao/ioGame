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
package com.iohao.game.action.skeleton.eventbus;

import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.SkeletonAttr;
import com.iohao.game.action.skeleton.core.commumication.BrokerClientContext;
import com.iohao.game.action.skeleton.core.runner.Runner;
import com.iohao.game.action.skeleton.protocol.processor.SimpleServerInfo;

/**
 * 分步式事件总线 Runner
 *
 * @author 渔民小镇
 * @date 2023-12-24
 */
public abstract class AbstractEventBusRunner implements Runner {
    @Override
    public void onStart(BarSkeleton skeleton) {
        // BrokerClient，当前逻辑服引用
        BrokerClientContext brokerClientContext = skeleton.option(SkeletonAttr.brokerClientContext);
        String brokerClientId = brokerClientContext.getId();

        EventBrokerClientMessage eventBrokerClientMessage = getEventBrokerClientMessage(brokerClientContext);

        // 事件总线与逻辑服是 1:1 的关系
        EventBus eventBus = new EventBus(brokerClientId);
        skeleton.option(SkeletonAttr.eventBus, eventBus);

        // EventBus 默认设置
        eventBus.setSubscribeExecutorSelector(SubscribeExecutorSelector.defaultInstance());
        eventBus.setSubscriberInvokeCreator(SubscriberInvokeCreator.defaultInstance());
        eventBus.setEventBusMessageCreator(EventBusMessageCreator.defaultInstance());
        eventBus.setEventBusListener(EventBusListener.defaultInstance());

        eventBus.setBrokerClientContext(brokerClientContext);
        eventBus.setEventBrokerClientMessage(eventBrokerClientMessage);

        // EventBus 注册订阅者
        this.registerEventBus(eventBus, skeleton);

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
    abstract protected void registerEventBus(EventBus eventBus, BarSkeleton skeleton);
}
