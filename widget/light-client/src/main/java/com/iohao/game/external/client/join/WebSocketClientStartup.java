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
package com.iohao.game.external.client.join;

import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.common.kit.log.IoGameLoggerFactory;
import com.iohao.game.external.client.ClientConnectOption;
import com.iohao.game.external.client.user.ClientUserChannel;
import com.iohao.game.external.client.user.ClientUser;
import com.iohao.game.external.core.message.ExternalMessage;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * @author 渔民小镇
 * @date 2023-07-04
 */
class WebSocketClientStartup implements ClientConnect {
    static final Logger log = IoGameLoggerFactory.getLoggerCommonStdout();

    @Override
    public void connect(ClientConnectOption option) {
        ClientUser clientUser = option.getClientUser();
        ClientUserChannel clientUserChannel = clientUser.getClientUserChannel();

        String wsUrl = option.getWsUrl();
        BarSkeleton barSkeleton = option.getBarSkeleton();

        URI uri = null;
        try {
            uri = new URI(wsUrl);
        } catch (URISyntaxException e) {
            log.error(e.getMessage(), e);
        }

        // 连接游戏服务器的地址
        WebSocketClient webSocketClient = new WebSocketClient(Objects.requireNonNull(uri), new Draft_6455()) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
            }

            @Override
            public void onMessage(String s) {
            }

            @Override
            public void onClose(int i, String s, boolean b) {
            }

            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onMessage(ByteBuffer byteBuffer) {
                // 接收服务器返回的消息
                byte[] dataContent = byteBuffer.array();
                ExternalMessage externalMessage = DataCodecKit.decode(dataContent, ExternalMessage.class);

                clientUserChannel.read(externalMessage, barSkeleton);
            }
        };

        clientUserChannel.setClientChannel(externalMessage -> {
            byte[] bytes = DataCodecKit.encode(externalMessage);
            webSocketClient.send(bytes);
        });

        clientUser.getClientUserInputCommands().start();

        // 开始连接服务器
        webSocketClient.connect();
    }
}
