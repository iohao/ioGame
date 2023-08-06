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
import com.iohao.game.bolt.broker.core.message.SettingUserIdMessage;
import com.iohao.game.bolt.broker.core.message.SettingUserIdMessageResponse;
import com.iohao.game.common.consts.IoGameLogName;
import com.iohao.game.external.core.aware.UserSessionsAware;
import com.iohao.game.external.core.session.UserChannelId;
import com.iohao.game.external.core.session.UserSessions;
import lombok.extern.slf4j.Slf4j;

/**
 * 设置 userId 的处理器
 *
 * @author 渔民小镇
 * @date 2023-02-21
 */
@Slf4j(topic = IoGameLogName.MsgTransferTopic)
public final class SettingUserIdMessageExternalProcessor extends AbstractAsyncUserProcessor<SettingUserIdMessage>
        implements UserSessionsAware {
    UserSessions<?, ?> userSessions;

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, SettingUserIdMessage request) {

        long userId = request.getUserId();
        String channelId = request.getUserChannelId();

        UserChannelId userChannelId = new UserChannelId(channelId);

        // 当设置好玩家 id ，也表示着已经身份验证了（表示登录过了）。
        boolean result = this.userSessions.settingUserId(userChannelId, userId);

        SettingUserIdMessageResponse response = new SettingUserIdMessageResponse();
        response.setSuccess(result);
        response.setUserId(userId);

        asyncCtx.sendResponse(response);

        if (IoGameGlobalConfig.isExternalLog()) {
            log.info("3 对外服设置用户id, userChannelId:{}, 真实userId:{}", userChannelId, userId);
        }
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
