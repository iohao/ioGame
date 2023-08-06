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
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.bolt.broker.core.common.AbstractAsyncUserProcessor;
import com.iohao.game.bolt.broker.core.message.InnerModuleVoidMessage;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.aware.BrokerServerAware;
import com.iohao.game.bolt.broker.server.balanced.BalancedManager;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientProxy;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientRegion;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

/**
 * 模块之间的请求处理
 * <pre>
 *     模块间的请求 - 游戏逻辑服与单个游戏逻辑服通信请求 - 无返回值（可跨进程）
 *
 *     如果需要返回值的，see {@link InnerModuleMessageBrokerProcessor}
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-06-07
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InnerModuleVoidMessageBrokerProcessor extends AbstractAsyncUserProcessor<InnerModuleVoidMessage>
        implements BrokerServerAware {
    @Setter
    BrokerServer brokerServer;

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, InnerModuleVoidMessage innerModuleMessage) {
        // 模块之间的请求处理
        var requestMessage = innerModuleMessage.getRequestMessage();
        HeadMetadata headMetadata = requestMessage.getHeadMetadata();

        // 得到路由对应的逻辑服区域
        int cmdMerge = headMetadata.getCmdMerge();

        BalancedManager balancedManager = brokerServer.getBalancedManager();
        var logicBalanced = balancedManager.getLogicBalanced();
        BrokerClientRegion brokerClientRegion = logicBalanced.getBrokerClientRegion(cmdMerge);

        if (brokerClientRegion == null) {
            return;
        }

        // 逻辑服的负载均衡
        BrokerClientProxy brokerClientProxy = brokerClientRegion.getBrokerClientProxy(headMetadata);

        if (brokerClientProxy == null) {
            return;
        }

        try {
            // 请求方请求其它服务器得到的响应数据
            brokerClientProxy.oneway(requestMessage);
        } catch (RemotingException | InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public String interest() {
        return InnerModuleVoidMessage.class.getName();
    }
}
