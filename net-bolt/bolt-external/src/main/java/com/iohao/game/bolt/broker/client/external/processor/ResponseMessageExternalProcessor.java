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
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.iohao.game.action.skeleton.core.exception.ActionErrorEnum;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.bolt.broker.client.external.bootstrap.ExternalKit;
import com.iohao.game.bolt.broker.client.external.bootstrap.message.ExternalMessage;
import com.iohao.game.bolt.broker.client.external.config.ExternalGlobalConfig;
import com.iohao.game.bolt.broker.client.external.session.UserChannelId;
import com.iohao.game.bolt.broker.client.external.session.UserSession;
import com.iohao.game.bolt.broker.client.external.session.UserSessions;
import com.iohao.game.bolt.broker.core.common.BrokerGlobalConfig;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;


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
public class ResponseMessageExternalProcessor extends AsyncUserProcessor<ResponseMessage> {
    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, ResponseMessage responseMessage) {
        if (BrokerGlobalConfig.isExternalLog()) {
            log.debug("接收来自网关的响应 {}", responseMessage);
        }

        ExternalMessage message = ExternalKit.convertExternalMessage(responseMessage);
        HeadMetadata headMetadata = responseMessage.getHeadMetadata();

        UserSession userSession;
        // 这里将有两种情况，一种是要求登录，一种是没有要求登录

        // 要求登录的情况：表示请求业务方法需要先登录
        if (ExternalGlobalConfig.verifyIdentity) {
            // 如果通过 userId 得不到 UserSession 表示没有登录，返回错误码
            try {
                long userId = headMetadata.getUserId();
                userSession = UserSessions.me().getUserSession(userId);
            } catch (RuntimeException e) {
                log.error(e.getMessage(), e);
                message.setResponseStatus(ActionErrorEnum.verifyIdentity.getCode());
                message.setValidMsg("请先登录，在请求业务方法");
                // 没登录但请求了业务方法，则返回错误码
                sendError(message, headMetadata);
                return;
            }
        } else {
            // 一般指用户的 channelId （来源于对外服的 channel 长连接）
            // see UserSession#employ
            try {
                String channelId = headMetadata.getExtJsonField();
                userSession = UserSessions.me().getUserSession(new UserChannelId(channelId));
            } catch (RuntimeException e) {
                log.error(e.getMessage(), e);
                // 通常是找不到对应的 UserSession
                return;
            }
        }

        // 响应结果给用户
        Channel channel = userSession.getChannel();
        channel.writeAndFlush(message);
    }

    private void sendError(ExternalMessage message, HeadMetadata headMetadata) {
        try {
            String channelId = headMetadata.getExtJsonField();
            UserSession userSession = UserSessions.me().getUserSession(new UserChannelId(channelId));
            // 响应结果给用户
            Channel channel = userSession.getChannel();
            channel.writeAndFlush(message);
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
        }
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
