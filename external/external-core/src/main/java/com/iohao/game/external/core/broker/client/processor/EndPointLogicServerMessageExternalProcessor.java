/*
 * ioGame
 * Copyright (C) 2021 - present  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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
import com.iohao.game.action.skeleton.protocol.processor.EndPointLogicServerMessage;
import com.iohao.game.action.skeleton.protocol.processor.EndPointOperationEnum;
import com.iohao.game.bolt.broker.core.common.AbstractAsyncUserProcessor;
import com.iohao.game.common.kit.CollKit;
import com.iohao.game.common.kit.HashKit;
import com.iohao.game.external.core.aware.UserSessionsAware;
import com.iohao.game.external.core.session.UserSessionOption;
import com.iohao.game.external.core.session.UserSessions;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author 渔民小镇
 * @date 2023-02-21
 */
public final class EndPointLogicServerMessageExternalProcessor extends AbstractAsyncUserProcessor<EndPointLogicServerMessage>
        implements UserSessionsAware {
    UserSessions<?, ?> userSessions;

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, EndPointLogicServerMessage message) {
        List<Long> userList = message.getUserList();
        EndPointOperationEnum operation = message.getOperation();
        if (CollKit.isEmpty(userList) || Objects.isNull(operation)) {
            return;
        }

        List<Integer> collect = listLogicServerId(message);

        this.userSessions.ifPresent(userList, userSession -> {
            // 将用户绑定（关联）到游戏逻辑服，所有与该游戏逻辑服相关的请求都由该服务器处理。
            userSession.ifPresent(UserSessionOption.bindingLogicServerIdSet, bindingLogicServerIdSet -> {

                switch (operation) {
                    case APPEND_BINDING:
                        bindingLogicServerIdSet.addAll(collect);
                        break;
                    case COVER_BINDING:
                        bindingLogicServerIdSet.clear();
                        bindingLogicServerIdSet.addAll(collect);
                        break;
                    case REMOVE_BINDING:
                        collect.forEach(bindingLogicServerIdSet::remove);
                        break;
                    default:
                        bindingLogicServerIdSet.clear();
                }

                int[] data = null;
                if (!bindingLogicServerIdSet.isEmpty()) {
                    data = bindingLogicServerIdSet.stream().mapToInt(Integer::intValue).toArray();
                }

                userSession.option(UserSessionOption.bindingLogicServerIdArray, data);
            });
        });
    }

    private List<Integer> listLogicServerId(EndPointLogicServerMessage message) {
        Set<String> logicServerIdSet = message.getLogicServerIdSet();
        if (Objects.isNull(logicServerIdSet)) {
            return Collections.emptyList();
        }

        return logicServerIdSet
                .stream()
                .map(HashKit::hash32)
                .toList();
    }

    @Override
    public String interest() {
        return EndPointLogicServerMessage.class.getName();
    }

    @Override
    public void setUserSessions(UserSessions<?, ?> userSessions) {
        this.userSessions = userSessions;
    }
}
