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
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.bolt.broker.core.common.AbstractAsyncUserProcessor;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.common.consts.IoGameLogName;
import com.iohao.game.external.core.aware.UserSessionsAware;
import com.iohao.game.external.core.config.ExternalGlobalConfig;
import com.iohao.game.external.core.kit.ExternalKit;
import com.iohao.game.external.core.message.ExternalMessage;
import com.iohao.game.external.core.session.UserChannelId;
import com.iohao.game.external.core.session.UserSession;
import com.iohao.game.external.core.session.UserSessions;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j(topic = IoGameLogName.MsgTransferTopic)
public final class ResponseMessageExternalProcessor extends AbstractAsyncUserProcessor<ResponseMessage>
        implements UserSessionsAware {
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
        if (userSession != null) {
            userSession.writeAndFlush(externalMessage);
        }

        // 游戏对外服缓存
        int cacheCondition = headMetadata.getCacheCondition();
        // 当缓存条件存在时，表示需要缓存数据
        if (cacheCondition != 0) {
            // 能到这里，externalCmdCache 一定不为 null
            ExternalGlobalConfig.externalCmdCache.addCacheData(responseMessage);
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
