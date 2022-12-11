/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iohao.game.bolt.broker.client.external.ext.impl;

import com.iohao.game.action.skeleton.core.exception.ActionErrorEnum;
import com.iohao.game.action.skeleton.protocol.external.RequestCollectExternalMessage;
import com.iohao.game.bolt.broker.client.external.bootstrap.ExternalKit;
import com.iohao.game.bolt.broker.client.external.bootstrap.message.ExternalMessage;
import com.iohao.game.bolt.broker.client.external.ext.ExternalBizRegion;
import com.iohao.game.bolt.broker.client.external.ext.ExternalBizRegionContext;
import com.iohao.game.bolt.broker.client.external.session.UserSession;
import com.iohao.game.bolt.broker.client.external.session.UserSessions;
import com.iohao.game.core.common.client.ExternalBizCodeCont;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

import java.io.Serializable;

/**
 * 强制用户（玩家）下线
 * <pre>
 *     对外服业务扩展
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-07-27
 */
public class ForcedOfflineExternalBizRegion implements ExternalBizRegion {

    @Override
    public int getBizCode() {
        return ExternalBizCodeCont.forcedOffline;
    }

    @Override
    public Serializable request(ExternalBizRegionContext regionContext) {
        RequestCollectExternalMessage request = regionContext.getRequestCollectExternalMessage();

        long userId = request.getData();
        UserSessions userSessions = UserSessions.me();

        // true 表示用户存在对外服
        boolean existUser = userSessions.existUserSession(userId);

        if (existUser) {
            // 强制下线
            UserSession userSession = userSessions.getUserSession(userId);
            // 发送强制下线消息
            this.sendForcedOfflineMsg(userSession);
        }

        return null;
    }

    private void sendForcedOfflineMsg(UserSession userSession) {

        ExternalMessage externalMessage = ExternalKit.createExternalMessage();
        // 强制玩家下线 状态码
        externalMessage.setResponseStatus(ActionErrorEnum.forcedOffline.getCode());
        externalMessage.setValidMsg(ActionErrorEnum.forcedOffline.getMsg());

        Channel channel = userSession.getChannel();
        channel.writeAndFlush(externalMessage).addListener((ChannelFutureListener) future -> {
            // 回调 UserSessions 中移除对应的玩家
            UserSessions.me().removeUserSession(userSession);
        });
    }
}
