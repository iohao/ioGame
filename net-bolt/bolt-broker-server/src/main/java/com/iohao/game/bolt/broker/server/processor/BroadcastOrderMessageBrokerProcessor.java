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
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.bolt.broker.core.message.BroadcastOrderMessage;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.aware.BrokerServerAware;
import com.iohao.game.common.consts.IoGameLogName;
import com.iohao.game.common.kit.ExecutorKit;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * 把逻辑服的广播 顺序的 转发到对外服
 * <pre>
 *     只使用了一个线程
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-07-14
 */
@Setter
@Slf4j(topic = IoGameLogName.MsgTransferTopic)
public class BroadcastOrderMessageBrokerProcessor extends AsyncUserProcessor<BroadcastOrderMessage>
        implements BrokerServerAware {
    BrokerServer brokerServer;
    final ExecutorService executorService = ExecutorKit.newSingleThreadExecutor("BroadcastOrderBroker");

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, BroadcastOrderMessage broadcastOrderMessage) {
        if (IoGameGlobalConfig.broadcastLog) {
            log.info("Broadcast 网关 顺序的 广播消息到对外服务器 {}", broadcastOrderMessage);
        }

        BrokerExternalKit.sendMessageToExternal(this.brokerServer, broadcastOrderMessage);
    }


    @Override
    public Executor getExecutor() {
        return executorService;
    }

    @Override
    public String interest() {
        return BroadcastOrderMessage.class.getName();
    }
}
