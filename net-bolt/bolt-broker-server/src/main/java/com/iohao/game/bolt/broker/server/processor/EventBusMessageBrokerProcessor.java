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
package com.iohao.game.bolt.broker.server.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.exception.RemotingException;
import com.iohao.game.action.skeleton.eventbus.EventBrokerClientMessage;
import com.iohao.game.action.skeleton.eventbus.EventBusMessage;
import com.iohao.game.bolt.broker.core.client.BrokerClientType;
import com.iohao.game.bolt.broker.core.common.AbstractAsyncUserProcessor;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.aware.BrokerServerAware;
import com.iohao.game.bolt.broker.server.balanced.BalancedManager;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientProxy;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 分布式事件总线 broker
 *
 * @author 渔民小镇
 * @date 2023-12-24
 */
@Slf4j
@Setter
public class EventBusMessageBrokerProcessor extends AbstractAsyncUserProcessor<EventBusMessage>
        implements BrokerServerAware {

    BrokerServer brokerServer;

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, final EventBusMessage eventBusMessage) {

        Consumer<BrokerClientProxy> consumer = client -> {
            try {
                client.oneway(eventBusMessage);
            } catch (RemotingException | InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        };

        BalancedManager balancedManager = this.brokerServer.getBalancedManager();
        Collection<EventBrokerClientMessage> eventBrokerClientMessageSet = eventBusMessage.getEventBrokerClientMessages();

        for (EventBrokerClientMessage eventBrokerClientMessage : eventBrokerClientMessageSet) {

            String brokerClientId = eventBrokerClientMessage.getBrokerClientId();
            BrokerClientType brokerClientType = BrokerClientType.valueOf(eventBrokerClientMessage.getBrokerClientType());

            if (brokerClientType == BrokerClientType.LOGIC) {
                // 转发给游戏逻辑服
                balancedManager.getLogicBalanced()
                        .listBrokerClientRegion()
                        .stream()
                        .flatMap(clientRegion -> clientRegion.listBrokerClientProxy().stream())
                        .filter(brokerClientProxy -> Objects.equals(brokerClientProxy.getId(), brokerClientId))
                        .forEach(consumer);
            }

            if (brokerClientType == BrokerClientType.EXTERNAL) {
                // 转发给游戏对外服
                balancedManager.getExternalLoadBalanced()
                        .listBrokerClientProxy()
                        .stream()
                        .filter(brokerClientProxy -> Objects.equals(brokerClientProxy.getId(), brokerClientId))
                        .forEach(consumer);
            }
        }
    }

    @Override
    public String interest() {
        return EventBusMessage.class.getName();
    }
}
