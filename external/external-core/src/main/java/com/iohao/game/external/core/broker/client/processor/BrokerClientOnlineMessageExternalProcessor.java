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
package com.iohao.game.external.core.broker.client.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.iohao.game.bolt.broker.client.processor.BrokerClientLineKit;
import com.iohao.game.bolt.broker.core.aware.BrokerClientAware;
import com.iohao.game.bolt.broker.core.aware.CmdRegionsAware;
import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.common.AbstractAsyncUserProcessor;
import com.iohao.game.bolt.broker.core.message.BrokerClientModuleMessage;
import com.iohao.game.bolt.broker.core.message.BrokerClientOnlineMessage;
import com.iohao.game.core.common.cmd.CmdRegions;
import com.iohao.game.external.core.hook.BrokerClientExternalAttr;
import lombok.Setter;

/**
 * @author 渔民小镇
 * @date 2023-12-14
 */
@Setter
public final class BrokerClientOnlineMessageExternalProcessor extends AbstractAsyncUserProcessor<BrokerClientOnlineMessage>
        implements CmdRegionsAware, BrokerClientAware {

    BrokerClient brokerClient;
    CmdRegions cmdRegions;

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, BrokerClientOnlineMessage message) {
        // 在线上的其他逻辑服（其他逻辑服指的是除自己外的其他游戏对外服、其他游戏逻辑服）
        BrokerClientModuleMessage moduleMessage = message.getModuleMessage();

        BrokerClientLineKit.executeSafe(moduleMessage, () -> {
            // online process
            brokerClient.option(BrokerClientExternalAttr.cmdRegions, cmdRegions);
            BrokerClientLineKit.onlineProcess(moduleMessage, brokerClient);
        });
    }

    @Override
    public String interest() {
        return BrokerClientOnlineMessage.class.getName();
    }
}
