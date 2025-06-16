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
import com.iohao.game.action.skeleton.core.exception.ActionErrorEnum;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.action.skeleton.protocol.collect.RequestCollectMessage;
import com.iohao.game.action.skeleton.protocol.collect.ResponseCollectItemMessage;
import com.iohao.game.action.skeleton.protocol.collect.ResponseCollectMessage;
import com.iohao.game.bolt.broker.cluster.BrokerClusterManager;
import com.iohao.game.bolt.broker.cluster.BrokerRunModeEnum;
import com.iohao.game.bolt.broker.core.common.AbstractAsyncUserProcessor;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.aware.BrokerServerAware;
import com.iohao.game.bolt.broker.server.balanced.BalancedManager;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientRegion;
import com.iohao.game.common.kit.CompletableFutureKit;
import com.iohao.game.core.common.NetCommonKit;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 模块之间的访问，访问同类型的多个逻辑服
 * <pre>
 *     如： 模块A 访问 模块B 的某个方法，因为只有模块B持有这些数据
 *     是把多个相同逻辑服结果聚合在一起
 *
 *     文档
 *          <a href="https://iohao.github.io/game/docs/communication/request_multiple_response">request/multiple_response</a>
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
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class InnerModuleRequestCollectMessageBrokerProcessor extends AbstractAsyncUserProcessor<RequestCollectMessage>
        implements BrokerServerAware {

    BrokerServer brokerServer;

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, RequestCollectMessage requestCollectMessage) {

        RequestMessage requestMessage = requestCollectMessage.getRequestMessage();
        HeadMetadata headMetadata = requestMessage.getHeadMetadata();
        int cmdMerge = headMetadata.getCmdMerge();

        // 得到路由对应的逻辑服区域
        BalancedManager balancedManager = brokerServer.getBalancedManager();
        var logicBalanced = balancedManager.getLogicBalanced();
        BrokerClientRegion brokerClientRegion = logicBalanced.getBrokerClientRegion(cmdMerge);

        if (brokerClientRegion == null) {
            // 响应结果
            var responseCollectMessage = new ResponseCollectMessage();
            responseCollectMessage.setError(ActionErrorEnum.cmdInfoErrorCode);
            // 将响应数据给回请求方
            asyncCtx.sendResponse(responseCollectMessage);
            return;
        }

        // 并行调用多个逻辑服
        var futureList = this.listFuture(requestMessage, brokerClientRegion);
        CompletableFutureKit.sequenceAsync(futureList).thenAcceptAsync(messageList -> {
            var responseCollectMessage = new ResponseCollectMessage();
            responseCollectMessage.setMessageList(messageList);

            // 将响应数据给回请求方
            asyncCtx.sendResponse(responseCollectMessage);

            print(responseCollectMessage);
        }, NetCommonKit.getVirtualExecutor());
    }

    private void print(ResponseCollectMessage responseCollectMessage) {
        if (IoGameGlobalConfig.requestResponseLog) {

            if (this.brokerServer.getBrokerRunMode() != BrokerRunModeEnum.CLUSTER) {
                return;
            }

            int port = brokerServer.getPort();
            String brokerId = brokerServer.getBrokerId();
            BrokerClusterManager brokerClusterManager = brokerServer.getBrokerClusterManager();
            int gossipListenPort = brokerClusterManager.getGossipListenPort();
            log.info("\n port [{}] gossipListenPort [{}] id [{}] \n responseAggregationMessage : {}"
                    , port
                    , gossipListenPort
                    , brokerId
                    , responseCollectMessage);
        }
    }

    private List<CompletableFuture<ResponseCollectItemMessage>> listFuture(RequestMessage requestMessage
            , BrokerClientRegion brokerClientRegion) {

        // 逻辑服列表 stream；异步请求逻辑服
        var stream = brokerClientRegion.listBrokerClientProxy().stream();
        return stream.map(brokerClientProxy -> CompletableFuture.supplyAsync(() -> {
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

            // 逻辑服 id
            String logicServerId = brokerClientProxy.getId();
            // 得到一个逻辑服的结果
            return new ResponseCollectItemMessage(logicServerId, responseMessage);
        }, NetCommonKit.getVirtualExecutor())).toList();
    }

    @Override
    public String interest() {
        return RequestCollectMessage.class.getName();
    }
}
