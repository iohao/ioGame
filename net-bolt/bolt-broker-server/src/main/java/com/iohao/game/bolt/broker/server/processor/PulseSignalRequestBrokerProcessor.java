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
import com.iohao.game.action.skeleton.pulse.message.SignalType;
import com.iohao.game.action.skeleton.pulse.message.PulseSignalRequest;
import com.iohao.game.bolt.broker.core.common.AbstractAsyncUserProcessor;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.aware.BrokerServerAware;
import com.iohao.game.bolt.broker.server.balanced.BalancedManager;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientProxy;
import com.iohao.game.common.kit.exception.ThrowKit;

import java.util.function.Consumer;

/**
 * 将脉冲信号发送给对应的逻辑服
 *
 * @author 渔民小镇
 * @date 2023-04-22
 */
public final class PulseSignalRequestBrokerProcessor extends AbstractAsyncUserProcessor<PulseSignalRequest>
        implements BrokerServerAware {

    BrokerServer brokerServer;

    @Override
    public void setBrokerServer(BrokerServer brokerServer) {
        this.brokerServer = brokerServer;
    }

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, PulseSignalRequest request) {

        BalancedManager balancedManager = this.brokerServer.getBalancedManager();

        Consumer<BrokerClientProxy> consumer = client -> {
            try {
                client.oneway(request);
            } catch (RemotingException | InterruptedException e) {
                ThrowKit.ofRuntimeException(e);
            }
        };

        if (request.containsSignalType(SignalType.external)) {
            // 转发给游戏对外服
            balancedManager.getExternalLoadBalanced()
                    .listBrokerClientProxy()
                    .forEach(consumer);
        }

        if (request.containsSignalType(SignalType.logic)) {
            // 转发给游戏逻辑服
            balancedManager.getLogicBalanced()
                    .listBrokerClientRegion()
                    .stream()
                    .flatMap(clientRegion -> clientRegion.listBrokerClientProxy().stream())
                    .forEach(consumer);
        }
    }

    @Override
    public String interest() {
        return PulseSignalRequest.class.getName();
    }
}
