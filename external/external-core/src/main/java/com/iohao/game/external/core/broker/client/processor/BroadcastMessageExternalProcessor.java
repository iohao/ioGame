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
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.bolt.broker.core.message.BroadcastMessage;
import com.iohao.game.common.consts.IoGameLogName;
import com.iohao.game.external.core.aware.UserSessionsAware;
import com.iohao.game.external.core.kit.ExternalKit;
import com.iohao.game.external.core.session.UserSessions;
import lombok.extern.slf4j.Slf4j;

/**
 * 接收并处理 来自网关的广播消息
 *
 * @author 渔民小镇
 * @date 2023-02-21
 */
@Slf4j(topic = IoGameLogName.MsgTransferTopic)
public final class BroadcastMessageExternalProcessor extends AbstractAsyncUserProcessor<BroadcastMessage>
        implements UserSessionsAware {
    UserSessions<?, ?> userSessions;

    @Override
    public void setUserSessions(UserSessions<?, ?> userSessions) {
        this.userSessions = userSessions;
    }

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, BroadcastMessage message) {
        if (IoGameGlobalConfig.isExternalLog()) {
            log.info("对外服接收网关的数据：{}", message);
        }

        ExternalKit.broadcast(message, this.userSessions);
    }

    @Override
    public String interest() {
        return BroadcastMessage.class.getName();
    }

}
