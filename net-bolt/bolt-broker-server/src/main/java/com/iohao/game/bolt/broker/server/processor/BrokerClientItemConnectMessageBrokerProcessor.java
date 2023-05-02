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
import com.iohao.game.bolt.broker.core.common.AbstractAsyncUserProcessor;
import com.iohao.game.bolt.broker.core.message.BrokerClientItemConnectMessage;
import com.iohao.game.common.kit.log.IoGameLoggerFactory;
import lombok.Setter;
import org.slf4j.Logger;

/**
 * BrokerClientItemConnect 首次连接到游戏网关
 *
 * @author 渔民小镇
 * @date 2022-05-16
 */
@Setter
public class BrokerClientItemConnectMessageBrokerProcessor extends AbstractAsyncUserProcessor<BrokerClientItemConnectMessage> {

    static final Logger log = IoGameLoggerFactory.getLoggerConnection();

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, BrokerClientItemConnectMessage request) {
        log.debug("bizCtx.getRemoteAddress() : {}", bizCtx.getRemoteAddress());
    }

    @Override
    public String interest() {
        return BrokerClientItemConnectMessage.class.getName();
    }
}
