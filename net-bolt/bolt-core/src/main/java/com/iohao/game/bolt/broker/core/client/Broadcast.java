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
package com.iohao.game.bolt.broker.core.client;

import com.alipay.remoting.exception.RemotingException;
import com.iohao.game.action.skeleton.core.DevConfig;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.bolt.broker.core.message.BroadcastMessage;
import com.iohao.game.bolt.broker.core.message.BroadcastOrderMessage;
import com.iohao.game.common.consts.IoGameLogName;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

/**
 * 广播相关操作
 *
 * @author 渔民小镇
 * @date 2022-01-29
 */
@Slf4j(topic = IoGameLogName.MsgTransferTopic)
public record Broadcast(BrokerClientItem brokerClientItem) {
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

        if (DevConfig.isBroadcastLog()) {
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
