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
package com.iohao.game.bolt.broker.client.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.iohao.game.action.skeleton.toy.IoGameBanner;
import com.iohao.game.bolt.broker.core.aware.BrokerClientItemAware;
import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.client.BrokerClientItem;
import com.iohao.game.bolt.broker.core.client.BrokerClientManager;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.bolt.broker.core.message.RequestBrokerClientModuleMessage;
import com.iohao.game.common.kit.log.IoGameLoggerFactory;
import lombok.Setter;
import org.slf4j.Logger;

/**
 * 收到网关请求模块信息
 * <pre>
 *     see ConnectionEventBrokerProcessor.java
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-16
 */
public class RequestBrokerClientModuleMessageClientProcessor extends AsyncUserProcessor<RequestBrokerClientModuleMessage>
        implements BrokerClientItemAware {
    static final Logger log = IoGameLoggerFactory.getLoggerCommon();

    @Setter
    BrokerClientItem brokerClientItem;

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, RequestBrokerClientModuleMessage request) {

        if (IoGameGlobalConfig.requestResponseLog) {
            log.info("bizCtx.getRemoteAddress() : {}", bizCtx.getRemoteAddress());
        }

        // 客户端服务器注册到游戏网关服
        brokerClientItem.registerToBroker();

        if (IoGameGlobalConfig.requestResponseLog) {
            BrokerClient brokerClient = brokerClientItem.getBrokerClient();
            BrokerClientManager brokerClientManager = brokerClient.getBrokerClientManager();
            log.info("brokerClientItems : {}", brokerClientManager.countActiveItem());
        }

        IoGameBanner.me().countDown();
    }

    @Override
    public String interest() {
        return RequestBrokerClientModuleMessage.class.getName();
    }
}
