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
package com.iohao.game.external.core.broker.client.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.iohao.game.bolt.broker.core.common.AbstractAsyncUserProcessor;
import com.iohao.game.bolt.broker.core.message.BrokerClientModuleMessage;
import com.iohao.game.bolt.broker.core.aware.CmdRegionsAware;
import com.iohao.game.core.common.cmd.CmdRegions;

/**
 * @author 渔民小镇
 * @date 2023-05-01
 */
public final class BrokerClientModuleMessageExternalProcessor extends AbstractAsyncUserProcessor<BrokerClientModuleMessage>
        implements CmdRegionsAware {
    CmdRegions cmdRegions;

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, BrokerClientModuleMessage moduleMessage) {
        // 游戏逻辑服的路由数据
        this.cmdRegions.loading(moduleMessage);
    }

    @Override
    public String interest() {
        return BrokerClientModuleMessage.class.getName();
    }

    @Override
    public void setCmdRegions(CmdRegions cmdRegions) {
        this.cmdRegions = cmdRegions;
    }
}
