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
package com.iohao.game.bolt.broker.server.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.exception.RemotingException;
import com.iohao.game.action.skeleton.pulse.message.PulseSignalResponse;
import com.iohao.game.bolt.broker.core.common.AbstractAsyncUserProcessor;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.aware.BrokerServerAware;
import com.iohao.game.bolt.broker.server.balanced.BalancedManager;
import com.iohao.game.bolt.broker.server.balanced.LogicBrokerClientLoadBalanced;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientProxy;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author 渔民小镇
 * @date 2023-04-22
 */
@Slf4j
public class PulseSignalResponseBrokerProcessor extends AbstractAsyncUserProcessor<PulseSignalResponse>
        implements BrokerServerAware {
    BrokerServer brokerServer;

    @Override
    public void setBrokerServer(BrokerServer brokerServer) {
        this.brokerServer = brokerServer;
    }

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, PulseSignalResponse response) {
        int sourceClientId = response.getSourceClientId();

        BalancedManager balancedManager = this.brokerServer.getBalancedManager();
        LogicBrokerClientLoadBalanced logicBalanced = balancedManager.getLogicBalanced();
        // 根据 sourceClientId 获取对应的游戏逻辑服
        BrokerClientProxy client = logicBalanced.getBrokerClientProxyByIdHash(sourceClientId);
        if (Objects.isNull(client)) {
            return;
        }

        try {
            client.oneway(response);
        } catch (RemotingException | InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public String interest() {
        return PulseSignalResponse.class.getName();
    }


}
