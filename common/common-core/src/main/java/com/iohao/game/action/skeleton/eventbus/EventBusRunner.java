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
 * 分布式事件总线 Runner，将 EventBusRunner 添加到业务框架后，分布式事件总线相关功能才会生效。
 * <pre>{@code
 * // 通过业务框架的 addRunner 方法来扩展分布式事件总线相关内容 （Runner 扩展机制），我们将 UserLoginEventMessage、EmailEventBusSubscriber 注册到 EventBus 中。
 * public class MyLogicStartup extends AbstractBrokerClientStartup {
 *     ... ...省略部分代码
 *
 *     @Override
 *     public BarSkeleton createBarSkeleton() {
 *         // 业务框架构建器
 *         BarSkeletonBuilder builder = ...
 *
 *         // 开启分布式事件总线。逻辑服添加 EventBusRunner，用于处理 EventBus 相关业务
 *         builder.addRunner(new EventBusRunner() {
 *             @Override
 *             public void registerEventBus(EventBus eventBus, BarSkeleton skeleton) {
 *             }
 *         });
 *
 *         return builder.build();
 *     }
 * }
 * }</pre>
 * 注意事项
 * <pre>
 *     如果你的逻辑服没有任何订阅者，只是发送事件，也是需要配置 EventBusRunner 的，这是因为事件总线是按需要加载的功能。
 *     ioGame 功能特性很多，但不是每个项目都需要这些功能。按需加载有很多好处，比如 email 逻辑服后续的业务不想参与任何订阅了，那么把这个 Runner 注释掉就行了。其他代码不用改，这样也不会占用资源。
 *     所以，需要将 EventBusRunner 添加到业务框架后，分布式事件总线相关功能才会生效。
 * </pre>
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
        EventBus eventBus = ofEventBus(brokerClientId);
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

        if (eventBus instanceof DefaultEventBus defaultEventBus) {
            defaultEventBus.setStatus(EventBusStatus.run);
        }

        EventBusRegion.addLocal(eventBus);
    }

    /**
     * new EventBus
     *
     * @param id eventBus id
     * @return EventBus
     */
    default EventBus ofEventBus(String id) {
        return new DefaultEventBus(id);
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
