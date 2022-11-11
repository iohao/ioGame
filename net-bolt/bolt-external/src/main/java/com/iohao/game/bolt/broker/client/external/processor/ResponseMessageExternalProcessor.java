/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License..
 */
package com.iohao.game.bolt.broker.client.external.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.bolt.broker.client.external.bootstrap.ExternalKit;
import com.iohao.game.bolt.broker.client.external.bootstrap.message.ExternalMessage;
import com.iohao.game.bolt.broker.client.external.session.UserChannelId;
import com.iohao.game.bolt.broker.client.external.session.UserSession;
import com.iohao.game.bolt.broker.client.external.session.UserSessions;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.bolt.broker.core.common.AbstractAsyncUserProcessor;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;


/**
 * 接收来自网关的响应
 * <pre>
 *     把响应 write 到客户端
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-01-18
 */
@Slf4j
public class ResponseMessageExternalProcessor extends AbstractAsyncUserProcessor<ResponseMessage> {
    final UserChannelId emptyUserChannelId = new UserChannelId("empty");

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, ResponseMessage responseMessage) {
        if (IoGameGlobalConfig.isExternalLog()) {
            log.debug("接收来自网关的响应 {}", responseMessage);
        }

        ExternalMessage message = ExternalKit.convertExternalMessage(responseMessage);

        UserSession userSession = getUserSession(responseMessage);

        // 响应结果给用户
        if (userSession == null) {
            return;
        }

        Channel channel = userSession.getChannel();
        channel.writeAndFlush(message);
    }

    private UserSession getUserSession(ResponseMessage responseMessage) {
        HeadMetadata headMetadata = responseMessage.getHeadMetadata();
        long userId = headMetadata.getUserId();

        try {

            // 当存在 userId 时，并且可以找到对应的 UserSession
            if (userId > 0 && UserSessions.me().existUserSession(userId)) {
                return UserSessions.me().getUserSession(userId);
            } else {

                String channelId = headMetadata.getChannelId();

                final UserChannelId userChannelId = Objects.isNull(channelId)
                        ? emptyUserChannelId
                        : new UserChannelId(channelId);

                // 通过 channelId 来查找 UserSession
                return UserSessions.me().getUserSession(userChannelId);
            }

        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 指定感兴趣的请求数据类型，该 UserProcessor 只对感兴趣的请求类型的数据进行处理；
     * 假设 除了需要处理 MyRequest 类型的数据，还要处理 java.lang.String 类型，有两种方式：
     * 1、再提供一个 UserProcessor 实现类，其 interest() 返回 java.lang.String.class.getName()
     * 2、使用 MultiInterestUserProcessor 实现类，可以为一个 UserProcessor 指定 List<String> multiInterest()
     *
     * @return 自定义处理器
     */
    @Override
    public String interest() {
        return ResponseMessage.class.getName();
    }
}
