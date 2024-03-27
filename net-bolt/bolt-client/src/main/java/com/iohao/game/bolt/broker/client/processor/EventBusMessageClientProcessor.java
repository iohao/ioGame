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
package com.iohao.game.bolt.broker.client.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.SkeletonAttr;
import com.iohao.game.action.skeleton.eventbus.EventBus;
import com.iohao.game.action.skeleton.eventbus.EventBusMessage;
import com.iohao.game.bolt.broker.core.aware.BrokerClientAware;
import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.common.AbstractAsyncUserProcessor;
import com.iohao.game.common.consts.IoGameLogName;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 分布式事件总线 brokerClient
 *
 * @author 渔民小镇
 * @date 2023-12-24
 */
@Setter
@Slf4j(topic = IoGameLogName.CommonStdout)
public class EventBusMessageClientProcessor extends AbstractAsyncUserProcessor<EventBusMessage>
        implements BrokerClientAware {

    BrokerClient brokerClient;

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, final EventBusMessage eventBusMessage) {
        BarSkeleton barSkeleton = brokerClient.getBarSkeleton();
        EventBus eventBus = barSkeleton.option(SkeletonAttr.eventBus);
        eventBus.fireMe(eventBusMessage);
    }

    @Override
    public String interest() {
        return EventBusMessage.class.getName();
    }
}
