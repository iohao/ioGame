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
import com.iohao.game.action.skeleton.protocol.collect.RequestCollectMessage;
import com.iohao.game.action.skeleton.protocol.collect.ResponseCollectItemMessage;
import com.iohao.game.action.skeleton.protocol.collect.ResponseCollectMessage;
import com.iohao.game.bolt.broker.cluster.BrokerClusterManager;
import com.iohao.game.bolt.broker.core.common.BrokerGlobalConfig;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.aware.BrokerServerAware;
import com.iohao.game.bolt.broker.server.balanced.BalancedManager;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientRegion;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 模块之间的访问，访问同类型的多个逻辑服
 * <pre>
 *     如： 模块A 访问 模块B 的某个方法，因为只有模块B持有这些数据
 *     是把多个相同逻辑服结果聚合在一起
 *
 *     文档
 *          https://www.yuque.com/iohao/game/rf9rb9
 * </pre>
 *
 * <pre>
 *     处理方式使用 CompletableFuture、ForkJoinPool 并行访问所有相同有逻辑服。
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-22
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InnerModuleRequestCollectMessageBrokerProcessor extends AsyncUserProcessor<RequestCollectMessage> implements BrokerServerAware {

    @Setter
    BrokerServer brokerServer;
    static final CompletableFuture<?>[] EMPTY_ARRAY = new CompletableFuture[0];

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, RequestCollectMessage requestCollectMessage) {

        RequestMessage requestMessage = requestCollectMessage.getRequestMessage();
        HeadMetadata headMetadata = requestMessage.getHeadMetadata();
        int cmdMerge = headMetadata.getCmdMerge();

        // 得到路由对应的逻辑服区域
        BalancedManager balancedManager = brokerServer.getBalancedManager();
        var logicBalanced = balancedManager.getLogicBalanced();
        BrokerClientRegion brokerClientRegion = logicBalanced.getBoltClientRegion(cmdMerge);

        // 响应结果
        ResponseCollectMessage responseCollectMessage = new ResponseCollectMessage();

        if (brokerClientRegion == null) {
            responseCollectMessage.setError(ActionErrorEnum.cmdInfoErrorCode);
            // 将响应数据给回请求方
            asyncCtx.sendResponse(responseCollectMessage);
            return;
        }

        // 并行调用多个逻辑服
        var futureList = this.listFuture(requestMessage, brokerClientRegion);
        // 将多个逻辑服的结果收集到 list 中
        var aggregationItemMessages = this.sequence(futureList);

        responseCollectMessage.setMessageList(aggregationItemMessages);

        // 将响应数据给回请求方
        asyncCtx.sendResponse(responseCollectMessage);

        print(responseCollectMessage);
    }

    private void print(ResponseCollectMessage responseCollectMessage) {
        if (BrokerGlobalConfig.requestResponseLog) {
            int port = brokerServer.getPort();
            String brokerId = brokerServer.getBrokerId();
            BrokerClusterManager brokerClusterManager = brokerServer.getBrokerClusterManager();
            int gossipListenPort = brokerClusterManager.getGossipListenPort();
            log.info("\n port [{}] gossipListenPort [{}] id [{}] \n responseAggregationMessage : {}"
                    , port
                    , gossipListenPort
                    , brokerId
                    , responseCollectMessage.toJsonPretty());
        }
    }

    private List<CompletableFuture<ResponseCollectItemMessage>> listFuture(RequestMessage requestMessage, BrokerClientRegion brokerClientRegion) {

        // 逻辑服列表 stream
        return brokerClientRegion.listBrokerClientProxy().stream().map(brokerClientProxy -> {
            // 逻辑服 id
            String logicServerId = brokerClientProxy.getId();

            // 异步请求逻辑服
            return CompletableFuture.supplyAsync(() -> {
                ResponseMessage responseMessage;

                try {
                    // 请求方请求其它服务器得到的响应数据
                    responseMessage = brokerClientProxy.invokeSync(requestMessage);
                } catch (RemotingException | InterruptedException e) {
                    log.error(e.getMessage(), e);
                    return null;
                }

                byte[] data;
                // 有错误或没有数据的，就不做处理了，意义不大
                if (responseMessage == null
                        || responseMessage.hasError()
                        || (data = responseMessage.getData()) == null
                        || data.length == 0) {
                    return null;
                }

                // 得到一个逻辑服的结果
                return new ResponseCollectItemMessage()
                        .setResponseMessage(responseMessage)
                        .setLogicServerId(logicServerId);

            });
        }).collect(Collectors.toList());
    }

    private <T> List<T> sequence(List<CompletableFuture<T>> futures) {
        // 组合处理 allOf
        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(futures.toArray(EMPTY_ARRAY));

        return allDoneFuture.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
        ).join();
        // see https://nurkiewicz.com/2013/05/java-8-completablefuture-in-action.html
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
        return RequestCollectMessage.class.getName();
    }
}
