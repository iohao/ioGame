/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.iohao.game.bolt.broker.server.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.exception.RemotingException;
import com.iohao.game.action.skeleton.protocol.external.RequestCollectExternalMessage;
import com.iohao.game.action.skeleton.protocol.external.ResponseCollectExternalItemMessage;
import com.iohao.game.action.skeleton.protocol.external.ResponseCollectExternalMessage;
import com.iohao.game.bolt.broker.core.common.AbstractAsyncUserProcessor;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.aware.BrokerServerAware;
import com.iohao.game.bolt.broker.server.balanced.BalancedManager;
import com.iohao.game.bolt.broker.server.balanced.ExternalBrokerClientLoadBalanced;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientProxy;
import com.iohao.game.common.kit.CompletableFutureKit;
import com.iohao.game.common.kit.log.IoGameLoggerFactory;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InnerModuleRequestCollectExternalMessageBrokerProcessor extends AbstractAsyncUserProcessor<RequestCollectExternalMessage>
        implements BrokerServerAware {
    static final Logger log = IoGameLoggerFactory.getLoggerCommon();

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

        Stream<BrokerClientProxy> stream = externalLoadBalanced.listBrokerClientProxy().stream();
        // 游戏对外服 id
        int sourceClientId = requestCollectMessage.getSourceClientId();

        if (IoGameGlobalConfig.brokerSniperToggleAK47) {
            // 当为 true 时，开启容错机制，只有找到了指定的游戏对外服，则增加过虑条件
            if (externalLoadBalanced.contains(sourceClientId)) {
                stream = stream.filter(brokerClientProxy -> brokerClientProxy.getIdHash() == sourceClientId);
            }
        } else {
            // 当为 false 时，表示关闭容错机制，直接增加过虑条件
            stream = stream.filter(brokerClientProxy -> brokerClientProxy.getIdHash() == sourceClientId);
        }

        return stream.map(brokerClientProxy -> CompletableFuture.supplyAsync(() -> {
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

            String logicServerId = brokerClientProxy.getId();
            // 得到一个逻辑服的结果
            return itemMessage.setLogicServerId(logicServerId);
        })).toList();
    }

    @Override
    public String interest() {
        return RequestCollectExternalMessage.class.getName();
    }
}
