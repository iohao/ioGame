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
package com.iohao.game.bolt.broker.client.external.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.iohao.game.action.skeleton.protocol.processor.EndPointLogicServerMessage;
import com.iohao.game.bolt.broker.client.external.session.UserSessionAttr;
import com.iohao.game.bolt.broker.client.external.session.UserSessions;
import com.iohao.game.bolt.broker.core.common.AbstractAsyncUserProcessor;
import com.iohao.game.common.kit.CollKit;
import com.iohao.game.common.kit.MurmurHash3;
import com.iohao.game.common.kit.StrKit;

import java.util.List;

/**
 * @author 渔民小镇
 * @date 2022-05-28
 */
public class EndPointLogicServerMessageExternalProcessor extends AbstractAsyncUserProcessor<EndPointLogicServerMessage> {
    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, EndPointLogicServerMessage message) {

        List<Long> userList = message.getUserList();
        String logicServerId = message.getLogicServerId();

        if (CollKit.isEmpty(userList) || StrKit.isEmpty(logicServerId)) {
            return;
        }

        // 到对外服在转 hash32，以防之后需要这个逻辑服的id（string）
        int endPointLogicServerId = MurmurHash3.hash32(logicServerId);

        // true 绑定逻辑服id，false 清除绑定的逻辑服id
        boolean binding = message.isBinding();

        userList.stream()
                .filter(UserSessions.me()::existUserSession)
                .map(UserSessions.me()::getUserSession)
                .forEach(userSession -> {
                    // 给用户绑定逻辑服，之后与该逻辑服有关的请求，都会分配给这个逻辑服来处理。
                    if (binding) {
                        userSession.attr(UserSessionAttr.endPointLogicServerId, endPointLogicServerId);
                    } else {
                        userSession.attr(UserSessionAttr.endPointLogicServerId, null);
                    }
                });
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
        return EndPointLogicServerMessage.class.getName();
    }
}
