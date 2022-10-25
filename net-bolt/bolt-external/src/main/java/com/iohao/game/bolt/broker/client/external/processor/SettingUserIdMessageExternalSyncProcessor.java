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
package com.iohao.game.bolt.broker.client.external.processor;

import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.SyncUserProcessor;
import com.iohao.game.bolt.broker.client.external.session.UserChannelId;
import com.iohao.game.bolt.broker.client.external.session.UserSessions;
import com.iohao.game.bolt.broker.core.common.BrokerGlobalConfig;
import com.iohao.game.bolt.broker.core.message.SettingUserIdMessage;
import com.iohao.game.bolt.broker.core.message.SettingUserIdMessageResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 渔民小镇
 * @date 2022-10-26
 */
@Slf4j
public class SettingUserIdMessageExternalSyncProcessor extends SyncUserProcessor<SettingUserIdMessage> {
    @Override
    public Object handleRequest(BizContext bizCtx, SettingUserIdMessage request) throws Exception {

        long userId = request.getUserId();
        String channelId = request.getUserChannelId();

        UserChannelId userChannelId = new UserChannelId(channelId);

        // 当设置好玩家 id ，也表示着已经身份验证了（表示登录过了）。
        boolean result = UserSessions.me().settingUserId(userChannelId, userId);

        log.info("======= userId : {}", userId);

        if (BrokerGlobalConfig.isExternalLog()) {
            log.debug("3 对外服设置用户id, userChannelId:{}, 真实userId:{}", userChannelId, userId);
        }

        return new SettingUserIdMessageResponse()
                .setSuccess(result)
                .setUserId(userId)
                .setEndTime(System.currentTimeMillis());
    }

    @Override
    public String interest() {
        return SettingUserIdMessage.class.getName();
    }
}
