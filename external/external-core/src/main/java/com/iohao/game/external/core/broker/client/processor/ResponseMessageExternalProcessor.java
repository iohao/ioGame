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
package com.iohao.game.external.core.broker.client.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.bolt.broker.core.common.AbstractAsyncUserProcessor;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.common.kit.log.IoGameLoggerFactory;
import com.iohao.game.external.core.aware.UserSessionsAware;
import com.iohao.game.external.core.config.ExternalGlobalConfig;
import com.iohao.game.external.core.kit.ExternalKit;
import com.iohao.game.external.core.message.ExternalMessage;
import com.iohao.game.external.core.session.UserChannelId;
import com.iohao.game.external.core.session.UserSession;
import com.iohao.game.external.core.session.UserSessions;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.Optional;

/**
 * 接收来自网关的响应
 * <pre>
 *     把响应 write 到客户端
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-02-21
 */
public final class ResponseMessageExternalProcessor extends AbstractAsyncUserProcessor<ResponseMessage>
        implements UserSessionsAware {
    static final Logger log = IoGameLoggerFactory.getLoggerMsg();

    final UserChannelId emptyUserChannelId = new UserChannelId("empty");
    UserSessions<?, ?> userSessions;

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, ResponseMessage responseMessage) {
        if (IoGameGlobalConfig.isExternalLog()) {
            log.info("接收来自网关的响应 {}", responseMessage);
        }

        ExternalMessage externalMessage = ExternalKit.convertExternalMessage(responseMessage);
        HeadMetadata headMetadata = responseMessage.getHeadMetadata();

        long userId = headMetadata.getUserId();
        // 当存在 userId 时，并且可以找到对应的 UserSession
        UserSession userSession;
        if (userId > 0) {
            userSession = userSessions.getUserSession(userId);
        } else {
            // 通过 channelId 来查找 UserSession
            String channelId = headMetadata.getChannelId();
            final UserChannelId userChannelId = Objects.isNull(channelId)
                    ? emptyUserChannelId
                    : new UserChannelId(channelId);

            userSession = userSessions.getUserSession(userChannelId);
        }

        // 响应结果给用户
        Optional.ofNullable(userSession).ifPresent(session -> session.writeAndFlush(externalMessage));

        // 缓存结果
        var externalCacheHook = ExternalGlobalConfig.externalCacheHook;
        if (Objects.nonNull(externalCacheHook)) {
            externalCacheHook.put(responseMessage);
        }
    }

    @Override
    public String interest() {
        return ResponseMessage.class.getName();
    }

    @Override
    public void setUserSessions(UserSessions<?, ?> userSessions) {
        this.userSessions = userSessions;
    }
}
