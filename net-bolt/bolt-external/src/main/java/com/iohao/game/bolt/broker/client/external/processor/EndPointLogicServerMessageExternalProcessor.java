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
package com.iohao.game.bolt.broker.client.external.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.iohao.game.action.skeleton.protocol.processor.EndPointLogicServerMessage;
import com.iohao.game.action.skeleton.protocol.processor.EndPointOperationEnum;
import com.iohao.game.bolt.broker.client.external.session.UserSessionAttr;
import com.iohao.game.bolt.broker.client.external.session.UserSessions;
import com.iohao.game.bolt.broker.core.common.AbstractAsyncUserProcessor;
import com.iohao.game.common.kit.CollKit;
import com.iohao.game.common.kit.HashKit;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @author 渔民小镇
 * @date 2022-05-28
 */
public class EndPointLogicServerMessageExternalProcessor extends AbstractAsyncUserProcessor<EndPointLogicServerMessage> {
    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, EndPointLogicServerMessage message) {
        // 旧游戏对外服的动态绑定逻辑
        List<Long> userList = message.getUserList();
        if (CollKit.isEmpty(userList)) {
            return;
        }

        EndPointOperationEnum operation = Objects.isNull(message.getOperation())
                ? EndPointOperationEnum.CLEAR
                : EndPointOperationEnum.COVER_BINDING;

        String logicServerId = null;

        if (EndPointOperationEnum.COVER_BINDING == operation) {
            Set<String> logicServerIdSet = message.getLogicServerIdSet();
            Optional<String> optional = CollKit.findAny(logicServerIdSet);
            logicServerId = optional.orElse(null);

            if (Objects.isNull(logicServerId)) {
                return;
            }
        }

        // 到对外服在转 hash32，以防之后需要这个逻辑服的id（string）
        int endPointLogicServerId = HashKit.hash32(logicServerId);

        // true 绑定逻辑服id，false 清除绑定的逻辑服id
        boolean binding = operation != EndPointOperationEnum.CLEAR;

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
