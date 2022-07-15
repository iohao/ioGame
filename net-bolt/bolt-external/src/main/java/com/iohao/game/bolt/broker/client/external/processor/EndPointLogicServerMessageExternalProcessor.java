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

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.iohao.game.action.skeleton.protocol.processor.EndPointLogicServerMessage;
import com.iohao.game.bolt.broker.client.external.session.UserSessionAttr;
import com.iohao.game.bolt.broker.client.external.session.UserSessions;
import com.iohao.game.common.kit.CollKit;
import com.iohao.game.common.kit.MurmurHash3;
import com.iohao.game.common.kit.StrKit;

import java.util.List;

/**
 * @author 渔民小镇
 * @date 2022-05-28
 */
public class EndPointLogicServerMessageExternalProcessor extends AsyncUserProcessor<EndPointLogicServerMessage> {
    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, EndPointLogicServerMessage message) {

        List<Long> userList = message.getUserList();
        String logicServerId = message.getLogicServerId();

        if (CollKit.isEmpty(userList) || StrKit.isEmpty(logicServerId)) {
            return;
        }

        // 到对外服在转 hash32，以防之后需要这个逻辑服的id（string）
        int endPointLogicServerId = MurmurHash3.hash32(logicServerId);

        userList.stream()
                .filter(UserSessions.me()::existUserSession)
                .map(UserSessions.me()::getUserSession)
                .forEach(userSession -> {
                    // 给用户绑定逻辑服，之后与该逻辑服有关的请求，都会分配给这个逻辑服来处理。
                    userSession.attr(UserSessionAttr.endPointLogicServerId, endPointLogicServerId);
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
