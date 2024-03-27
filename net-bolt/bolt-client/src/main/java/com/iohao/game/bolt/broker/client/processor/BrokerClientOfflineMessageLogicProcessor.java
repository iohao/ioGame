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
package com.iohao.game.bolt.broker.client.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.iohao.game.bolt.broker.core.aware.BrokerClientAware;
import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.message.BrokerClientModuleMessage;
import com.iohao.game.bolt.broker.core.message.BrokerClientOfflineMessage;
import lombok.Setter;

/**
 * @author 渔民小镇
 * @date 2023-12-14
 */
@Setter
public final class BrokerClientOfflineMessageLogicProcessor extends AsyncUserProcessor<BrokerClientOfflineMessage>
        implements BrokerClientAware {

    BrokerClient brokerClient;

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, BrokerClientOfflineMessage message) {
        // BrokerClientOnlineMessage 是 Broker（游戏网关）发送过来的信息，表示有逻辑服下线
        BrokerClientModuleMessage moduleMessage = message.getModuleMessage();

        BrokerClientLineKit.executeSafe(moduleMessage, () -> {
            // offline process
            BrokerClientLineKit.offlineProcess(moduleMessage, brokerClient);
        });
    }

    @Override
    public String interest() {
        return BrokerClientOfflineMessage.class.getName();
    }
}
