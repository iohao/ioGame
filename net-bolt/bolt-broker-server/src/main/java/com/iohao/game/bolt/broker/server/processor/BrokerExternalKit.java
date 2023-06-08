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

import com.alipay.remoting.exception.RemotingException;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.bolt.broker.core.message.BroadcastMessage;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.balanced.BalancedManager;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientProxy;
import com.iohao.game.common.kit.log.IoGameLoggerFactory;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;

import java.util.Objects;

/**
 * 工具类：把游戏逻辑服的广播转发到游戏对外服
 *
 * @author 渔民小镇
 * @date 2022-05-28
 */
@UtilityClass
public class BrokerExternalKit {
    private static final Logger log = IoGameLoggerFactory.getLoggerCommon();

    /**
     * 将数据发送给游戏对外服
     *
     * @param brokerServer     游戏网关
     * @param broadcastMessage 数据
     */
    public void sendMessageToExternal(BrokerServer brokerServer, BroadcastMessage broadcastMessage) {
        ResponseMessage responseMessage = broadcastMessage.getResponseMessage();
        HeadMetadata headMetadata = responseMessage.getHeadMetadata();
        // 指定的游戏对外服 id
        int sourceClientId = headMetadata.getSourceClientId();

        // 表示没有指定要访问的游戏对外服
        if (sourceClientId == 0) {
            // 给所有的游戏对外服发送
            sendMessageToExternals(brokerServer, broadcastMessage);
            return;
        }

        BalancedManager balancedManager = brokerServer.getBalancedManager();
        var externalLoadBalanced = balancedManager.getExternalLoadBalanced();
        // 得到指定的游戏对外服
        BrokerClientProxy brokerClientProxy = externalLoadBalanced.get(sourceClientId);
        if (Objects.nonNull(brokerClientProxy)) {
            try {
                //  发送到指定的游戏对外服
                brokerClientProxy.oneway(broadcastMessage);
            } catch (RemotingException | InterruptedException e) {
                log.error(e.getMessage(), e);
            }

            return;
        }

        boolean toggleAK47 = IoGameGlobalConfig.broadcastSniperToggleAK47;
        if (toggleAK47) {
            // 给所有的游戏对外服发送
            sendMessageToExternals(brokerServer, broadcastMessage);
        }
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

        try {
            for (BrokerClientProxy brokerClientProxy : externalLoadBalanced.listBrokerClientProxy()) {
                //  转发到对外服务器
                brokerClientProxy.oneway(message);
            }
        } catch (RemotingException | InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
}
