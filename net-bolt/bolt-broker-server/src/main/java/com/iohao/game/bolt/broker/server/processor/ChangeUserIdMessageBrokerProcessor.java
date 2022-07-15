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
package com.iohao.game.bolt.broker.server.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.aware.BrokerServerAware;
import com.iohao.game.bolt.broker.server.balanced.BalancedManager;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientProxy;
import com.iohao.game.bolt.broker.core.message.SettingUserIdMessage;
import com.iohao.game.bolt.broker.core.message.SettingUserIdMessageResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 设置用户 id
 * <pre>
 *     用户 id 变更 （逻辑服 --> 网关 --> 对外服 --> 网关 --> 逻辑服）
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-05-14
 */
@Slf4j
public class ChangeUserIdMessageBrokerProcessor extends AsyncUserProcessor<SettingUserIdMessage> implements BrokerServerAware {
    @Setter
    BrokerServer brokerServer;

    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, SettingUserIdMessage settingUserIdMessage) {
        // 用户 id 变更
        HeadMetadata headMetadata = settingUserIdMessage.getHeadMetadata();

        // 得到对外服的 id （就是发起请求的对外服）
        int sourceClientId = headMetadata.getSourceClientId();

        BalancedManager balancedManager = brokerServer.getBalancedManager();
        var externalLoadBalanced = balancedManager.getExternalLoadBalanced();

        // 根据 sourceClientId 获取对应的对外服
        BrokerClientProxy brokerClientProxy = externalLoadBalanced.get(sourceClientId);

        try {
            // 转发给对外服, 并得到对外服的响应
            SettingUserIdMessageResponse messageResponse = brokerClientProxy.invokeSync(settingUserIdMessage);
            asyncCtx.sendResponse(messageResponse);
        } catch (RemotingException | InterruptedException e) {
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
        return SettingUserIdMessage.class.getName();
    }
}
