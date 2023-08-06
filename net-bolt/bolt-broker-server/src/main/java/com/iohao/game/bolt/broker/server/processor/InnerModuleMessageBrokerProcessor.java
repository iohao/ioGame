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
import com.iohao.game.action.skeleton.core.exception.ActionErrorEnum;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.bolt.broker.core.aware.CmdRegionsAware;
import com.iohao.game.bolt.broker.core.common.AbstractAsyncUserProcessor;
import com.iohao.game.bolt.broker.core.message.InnerModuleMessage;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.aware.BrokerServerAware;
import com.iohao.game.bolt.broker.server.balanced.BalancedManager;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientProxy;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientRegion;
import com.iohao.game.bolt.broker.server.kit.EndPointClientIdKit;
import com.iohao.game.core.common.cmd.CmdRegions;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 模块之间的请求处理
 * <pre>
 *     模块间的请求 - 游戏逻辑服与单个游戏逻辑服通信请求 - 有返回值（可跨进程）
 *
 *     如果不需要返回值的，see {@link InnerModuleVoidMessageBrokerProcessor}
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-14
 */
@Slf4j
public class InnerModuleMessageBrokerProcessor extends AbstractAsyncUserProcessor<InnerModuleMessage>
        implements BrokerServerAware, CmdRegionsAware {
    @Setter
    BrokerServer brokerServer;

    @Setter
    CmdRegions cmdRegions;

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, InnerModuleMessage innerModuleMessage) {
        // 模块之间的请求处理
        var requestMessage = innerModuleMessage.getRequestMessage();
        HeadMetadata headMetadata = requestMessage.getHeadMetadata();

        // 得到路由对应的逻辑服区域
        int cmdMerge = headMetadata.getCmdMerge();

        BalancedManager balancedManager = brokerServer.getBalancedManager();
        var logicBalanced = balancedManager.getLogicBalanced();
        BrokerClientRegion brokerClientRegion = logicBalanced.getBrokerClientRegion(cmdMerge);

        if (brokerClientRegion == null) {
            extractedError(asyncCtx, requestMessage);
            return;
        }

        EndPointClientIdKit.endPointClientId(headMetadata, this.cmdRegions);

        // 从游戏逻辑服区域中查找一个游戏逻辑服，用于处理请求
        BrokerClientProxy brokerClientProxy = brokerClientRegion.getBrokerClientProxy(headMetadata);

        if (brokerClientProxy == null) {
            extractedError(asyncCtx, requestMessage);
            return;
        }

        try {
            // 请求方请求其它服务器得到的响应数据
            ResponseMessage responseMessage = brokerClientProxy.invokeSync(requestMessage);
            // 将响应数据给回请求方
            asyncCtx.sendResponse(responseMessage);
        } catch (RemotingException | InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void extractedError(AsyncContext asyncCtx, RequestMessage requestMessage) {
        ResponseMessage responseMessage = requestMessage.createResponseMessage();
        responseMessage.setError(ActionErrorEnum.cmdInfoErrorCode);
        // 将响应数据给回请求方
        asyncCtx.sendResponse(responseMessage);
    }

    @Override
    public String interest() {
        return InnerModuleMessage.class.getName();
    }
}
