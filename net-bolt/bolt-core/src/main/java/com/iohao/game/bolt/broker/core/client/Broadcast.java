/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
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
package com.iohao.game.bolt.broker.core.client;

import com.alipay.remoting.exception.RemotingException;
import com.iohao.game.action.skeleton.core.DevConfig;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.bolt.broker.core.message.BroadcastMessage;
import com.iohao.game.bolt.broker.core.message.BroadcastOrderMessage;
import com.iohao.game.common.kit.log.IoGameLoggerFactory;
import org.slf4j.Logger;

import java.util.Collection;

/**
 * 广播相关操作
 *
 * @author 渔民小镇
 * @date 2022-01-29
 */
public record Broadcast(BrokerClientItem brokerClientItem) {
    static final Logger log = IoGameLoggerFactory.getLoggerMsg();

    /**
     * 广播消息给单个用户
     *
     * @param responseMessage 消息
     * @param userId          userId
     */
    public void broadcast(ResponseMessage responseMessage, long userId) {

        HeadMetadata headMetadata = responseMessage.getHeadMetadata();
        headMetadata.setUserId(userId);

        // 广播消息
        BroadcastMessage broadcastMessage = new BroadcastMessage()
                .setResponseMessage(responseMessage);

        // 发送广播消息
        this.internalBroadcast(broadcastMessage);
    }

    /**
     * 广播消息给指定用户列表
     *
     * @param responseMessage 消息
     * @param userIdList      指定用户列表
     */
    public void broadcast(ResponseMessage responseMessage, Collection<Long> userIdList) {
        // 广播消息
        BroadcastMessage broadcastMessage = new BroadcastMessage()
                .setResponseMessage(responseMessage)
                .setUserIdList(userIdList);

        // 发送广播消息
        this.internalBroadcast(broadcastMessage);
    }

    /**
     * 广播给全部用户
     *
     * @param responseMessage responseMessage
     */
    public void broadcast(ResponseMessage responseMessage) {
        // 广播消息
        BroadcastMessage broadcastMessage = new BroadcastMessage()
                .setResponseMessage(responseMessage)
                .setBroadcastAll(true);

        // 发送广播消息
        this.internalBroadcast(broadcastMessage);
    }

    void internalBroadcast(BroadcastMessage broadcastMessage) {
        try {
            this.brokerClientItem.oneway(broadcastMessage);
        } catch (RemotingException e) {
            log.error(e.getMessage(), e);
        }

        if (DevConfig.me().isBroadcastLog()) {
            // 打印广播日志
            BroadcastDebug.print(broadcastMessage);
        }
    }

    public void broadcastOrder(ResponseMessage responseMessage, Collection<Long> userIdList) {
        // 广播消息
        BroadcastOrderMessage orderMessage = new BroadcastOrderMessage();

        orderMessage.setResponseMessage(responseMessage)
                .setUserIdList(userIdList);

        // 发送广播消息
        this.internalBroadcast(orderMessage);
    }

    public void broadcastOrder(ResponseMessage responseMessage, long userId) {
        HeadMetadata headMetadata = responseMessage.getHeadMetadata();
        headMetadata.setUserId(userId);

        // 广播消息
        BroadcastOrderMessage orderMessage = new BroadcastOrderMessage();

        orderMessage.setResponseMessage(responseMessage);

        // 发送广播消息
        this.internalBroadcast(orderMessage);
    }

    public void broadcastOrder(ResponseMessage responseMessage) {
        BroadcastOrderMessage orderMessage = new BroadcastOrderMessage();

        orderMessage.setResponseMessage(responseMessage)
                .setBroadcastAll(true);

        // 发送广播消息
        this.internalBroadcast(orderMessage);
    }
}
