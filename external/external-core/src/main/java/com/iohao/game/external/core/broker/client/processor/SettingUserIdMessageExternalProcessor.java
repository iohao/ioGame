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
import com.iohao.game.action.skeleton.protocol.login.SettingUserIdMessage;
import com.iohao.game.action.skeleton.protocol.login.SettingUserIdMessageResponse;
import com.iohao.game.bolt.broker.core.common.AbstractAsyncUserProcessor;
import com.iohao.game.common.kit.trace.TraceKit;
import com.iohao.game.external.core.aware.UserSessionsAware;
import com.iohao.game.external.core.session.UserChannelId;
import com.iohao.game.external.core.session.UserSessions;
import org.slf4j.MDC;

import java.util.Objects;

/**
 * 设置 userId 的处理器
 *
 * @author 渔民小镇
 * @date 2023-02-21
 */
public final class SettingUserIdMessageExternalProcessor extends AbstractAsyncUserProcessor<SettingUserIdMessage>
        implements UserSessionsAware {
    UserSessions<?, ?> userSessions;

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, SettingUserIdMessage request) {
        var headMetadata = request.getHeadMetadata();

        String channelId = headMetadata.getChannelId();
        var userChannelId = new UserChannelId(channelId);

        long userId = request.getUserId();
        var response = new SettingUserIdMessageResponse();
        response.setUserId(userId);

        String traceId = headMetadata.getTraceId();
        if (Objects.nonNull(traceId)) {
            try {
                MDC.put(TraceKit.traceName, traceId);
                // 当设置好玩家 id ，也表示着已经身份验证了（表示登录过了）。
                boolean result = this.userSessions.settingUserId(userChannelId, userId);
                response.setSuccess(result);
            } finally {
                MDC.clear();
            }
        } else {
            // 当设置好玩家 id ，也表示着已经身份验证了（表示登录过了）。
            boolean result = this.userSessions.settingUserId(userChannelId, userId);
            response.setSuccess(result);
        }

        asyncCtx.sendResponse(response);
    }

    @Override
    public String interest() {
        return SettingUserIdMessage.class.getName();
    }

    @Override
    public void setUserSessions(UserSessions<?, ?> userSessions) {
        this.userSessions = userSessions;
    }
}
