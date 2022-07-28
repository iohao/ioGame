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
import com.iohao.game.action.skeleton.protocol.external.RequestCollectExternalMessage;
import com.iohao.game.action.skeleton.protocol.external.ResponseCollectExternalItemMessage;
import com.iohao.game.action.skeleton.protocol.external.ResponseCollectExternalMessage;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.aware.BrokerServerAware;
import com.iohao.game.bolt.broker.server.balanced.BalancedManager;
import com.iohao.game.bolt.broker.server.balanced.ExternalBrokerClientLoadBalanced;
import com.iohao.game.common.kit.CompletableFutureKit;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 游戏逻辑服访问游戏对外服，同时访问多个游戏对外服 - 请求
 * <pre>
 *     游戏逻辑服访问游戏对外服，因为只有游戏对外服持有这些数据
 *     把多个游戏对外服的结果聚合在一起
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-07-27
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InnerModuleRequestCollectExternalMessageBrokerProcessor extends AsyncUserProcessor<RequestCollectExternalMessage> implements BrokerServerAware {

    @Setter
    BrokerServer brokerServer;

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, RequestCollectExternalMessage requestCollectMessage) {

        BalancedManager balancedManager = brokerServer.getBalancedManager();
        var balanced = balancedManager.getExternalLoadBalanced();

        // 并行调用多个游戏对外服
        var futureList = this.listFuture(requestCollectMessage, balanced);
        // 将多个对外服的结果收集到 list 中
        var aggregationItemMessages = CompletableFutureKit.sequence(futureList);

        // 多个游戏对外服的响应结果
        ResponseCollectExternalMessage responseCollectMessage = new ResponseCollectExternalMessage();
        responseCollectMessage.setMessageList(aggregationItemMessages);

        // 将响应数据给回请求方
        asyncCtx.sendResponse(responseCollectMessage);
    }

    private List<CompletableFuture<ResponseCollectExternalItemMessage>> listFuture(
            RequestCollectExternalMessage requestCollectMessage,
            ExternalBrokerClientLoadBalanced externalLoadBalanced) {

        return externalLoadBalanced.listBrokerClientProxy().stream().map(brokerClientProxy -> {
            // 逻辑服 id
            String logicServerId = brokerClientProxy.getId();

            // 异步请求逻辑服
            return CompletableFuture.supplyAsync(() -> {
                ResponseCollectExternalItemMessage itemMessage;

                try {
                    // 请求方请求其它服务器得到的响应数据
                    itemMessage = brokerClientProxy.invokeSync(requestCollectMessage);
                } catch (RemotingException | InterruptedException e) {
                    log.error(e.getMessage(), e);
                    return null;
                }

                // 有错误或没有数据的，就不做处理了，意义不大
                if (itemMessage == null) {
                    return null;
                }

                // 得到一个逻辑服的结果
                return itemMessage.setLogicServerId(logicServerId);
            });

        }).collect(Collectors.toList());
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
        return RequestCollectExternalMessage.class.getName();
    }
}
