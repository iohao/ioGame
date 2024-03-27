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
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.iohao.game.bolt.broker.core.message.BroadcastOrderMessage;
import com.iohao.game.common.kit.ExecutorKit;
import com.iohao.game.external.core.aware.UserSessionsAware;
import com.iohao.game.external.core.message.ExternalCodecKit;
import com.iohao.game.external.core.session.UserSessions;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * 接收并处理 来自网关的广播消息 - 顺序的
 *
 * @author 渔民小镇
 * @date 2023-02-21
 */
public final class BroadcastOrderMessageExternalProcessor extends AsyncUserProcessor<BroadcastOrderMessage>
        implements UserSessionsAware {

    final ExecutorService executorService = ExecutorKit.newSingleThreadExecutor("BroadcastOrderExternal");
    UserSessions<?, ?> userSessions;

    @Override
    public void setUserSessions(UserSessions<?, ?> userSessions) {
        this.userSessions = userSessions;
    }

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, BroadcastOrderMessage message) {
        ExternalCodecKit.broadcast(message, this.userSessions);
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
