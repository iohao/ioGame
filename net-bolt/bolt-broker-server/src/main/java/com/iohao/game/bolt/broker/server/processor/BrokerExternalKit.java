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

import com.alipay.remoting.exception.RemotingException;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.bolt.broker.core.message.BroadcastMessage;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.balanced.BalancedManager;
import com.iohao.game.bolt.broker.server.balanced.ExternalBrokerClientLoadBalanced;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientProxy;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * 工具类：把游戏逻辑服的广播转发到游戏对外服
 *
 * @author 渔民小镇
 * @date 2022-05-28
 */
@Slf4j
@UtilityClass
public class BrokerExternalKit {
    /**
     * 将数据发送给游戏对外服
     *
     * @param brokerServer     游戏网关
     * @param broadcastMessage 数据
     */
    public void sendMessageToExternal(BrokerServer brokerServer, BroadcastMessage broadcastMessage) {
        // 指定的游戏对外服 id
        ResponseMessage responseMessage = broadcastMessage.getResponseMessage();
        HeadMetadata headMetadata = responseMessage.getHeadMetadata();
        int sourceClientId = headMetadata.getSourceClientId();

        // 游戏对外服相关
        BalancedManager balancedManager = brokerServer.getBalancedManager();
        var externalLoadBalanced = balancedManager.getExternalLoadBalanced();

        // 没有指定游戏对外服 id，全部转发
        if (sourceClientId == 0) {
            List<BrokerClientProxy> list = externalLoadBalanced.listBrokerClientProxy();
            sendMessage(list, broadcastMessage);
            return;
        }

        // 指定了游戏对外服
        Stream<BrokerClientProxy> stream = streamToggle(sourceClientId, externalLoadBalanced);
        Consumer<BrokerClientProxy> consumer = consumer(broadcastMessage);
        stream.forEach(consumer);
    }

    /**
     * 将数据发送给所有的游戏对外服
     *
     * @param brokerServer 游戏网关
     * @param message      message
     */
    public void sendMessageToExternals(BrokerServer brokerServer, Object message) {
        BalancedManager balancedManager = brokerServer.getBalancedManager();
        var externalLoadBalanced = balancedManager.getExternalLoadBalanced();
        List<BrokerClientProxy> list = externalLoadBalanced.listBrokerClientProxy();
        sendMessage(list, message);
    }

    /**
     * 得到 Stream，根据 Broker（游戏网关）转发消息容错配置
     * <pre>
     *     更详细的说明可以看 {@link IoGameGlobalConfig#brokerSniperToggleAK47}
     * </pre>
     *
     * @param sourceClientId       游戏对外服 id
     * @param externalLoadBalanced externalLoadBalanced
     * @return 得到 Stream
     */
    Stream<BrokerClientProxy> streamToggle(int sourceClientId, ExternalBrokerClientLoadBalanced externalLoadBalanced) {
        List<BrokerClientProxy> list = externalLoadBalanced.listBrokerClientProxy();
        Stream<BrokerClientProxy> stream = list.stream();

        // 没有指定游戏对外服 id，不增加任何过虑条件
        if (sourceClientId == 0) {
            return stream;
        }

        if (IoGameGlobalConfig.brokerSniperToggleAK47) {
            // 当为 true 时，开启容错机制，只有找到了指定的游戏对外服，才增加过虑条件
            if (externalLoadBalanced.contains(sourceClientId)) {
                stream = stream.filter(brokerClientProxy -> brokerClientProxy.getIdHash() == sourceClientId);
            }
        } else {
            // 当为 false 时，表示关闭容错机制，直接增加过虑条件
            stream = stream.filter(brokerClientProxy -> brokerClientProxy.getIdHash() == sourceClientId);
        }

        return stream;
    }

    private void sendMessage(List<BrokerClientProxy> list, Object message) {
        Consumer<BrokerClientProxy> consumer = consumer(message);
        list.forEach(consumer);
    }

    private Consumer<BrokerClientProxy> consumer(Object message) {
        return brokerClientProxy -> {
            try {
                //  转发到游戏对外服务器
                brokerClientProxy.oneway(message);
            } catch (RemotingException | InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        };
    }
}
