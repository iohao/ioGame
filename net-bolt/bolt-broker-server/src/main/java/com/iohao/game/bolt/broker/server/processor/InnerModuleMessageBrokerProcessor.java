/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iohao.game.bolt.broker.server.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.iohao.game.action.skeleton.core.exception.ActionErrorEnum;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.bolt.broker.core.message.InnerModuleMessage;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.aware.BrokerServerAware;
import com.iohao.game.bolt.broker.server.balanced.BalancedManager;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientProxy;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientRegion;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 模块之间的请求处理
 * <pre>
 *     模块间的请求
 *
 *     如果不需要返回值的，see {@link InnerModuleVoidMessageBrokerProcessor}
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-14
 */
@Slf4j
public class InnerModuleMessageBrokerProcessor extends AsyncUserProcessor<InnerModuleMessage> implements BrokerServerAware {
    @Setter
    BrokerServer brokerServer;

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, InnerModuleMessage innerModuleMessage) {
        // 模块之间的请求处理
        var requestMessage = innerModuleMessage.getRequestMessage();
        HeadMetadata headMetadata = requestMessage.getHeadMetadata();

        // 得到路由对应的逻辑服区域
        int cmdMerge = headMetadata.getCmdMerge();

        BalancedManager balancedManager = brokerServer.getBalancedManager();
        var logicBalanced = balancedManager.getLogicBalanced();
        BrokerClientRegion brokerClientRegion = logicBalanced.getBoltClientRegion(cmdMerge);

        if (brokerClientRegion == null) {
            extractedError(asyncCtx, requestMessage);
            return;
        }

        // 逻辑服的负载均衡
        BrokerClientProxy brokerClientProxy = brokerClientRegion.getBoltClientProxy(headMetadata);

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
            log.error(e.getMessage(),e);
        }
    }

    private void extractedError(AsyncContext asyncCtx, RequestMessage requestMessage) {
        ResponseMessage responseMessage = requestMessage.createResponseMessage();
        responseMessage.setError(ActionErrorEnum.cmdInfoErrorCode);
        // 将响应数据给回请求方
        asyncCtx.sendResponse(responseMessage);
    }


    /**
     * 指定感兴趣的请求数据类型，该 UserProcessor 只对感兴趣的请求类型的数据进行处理；
     * 假设 除了需要处理 MyRequest 类型的数据，还要处理 java.lang.String 类型，有两种方式：
     * 1、再提供一个 UserProcessor 实现类，其 interest() 返回 java.lang.String.class.getName()
     * 2、使用 MultiInterestUserProcessor 实现类，可以为一个 UserProcessor 指定 List<String> multiInterest()
     *
     * @return 自定义处理器
     */
    @Override
    public String interest() {
        return InnerModuleMessage.class.getName();
    }
}
