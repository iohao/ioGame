/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
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

import com.alipay.remoting.BizContext;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.protocol.SyncUserProcessor;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.bolt.broker.core.message.SettingUserIdMessage;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.aware.BrokerServerAware;
import com.iohao.game.bolt.broker.server.balanced.BalancedManager;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientProxy;
import com.iohao.game.common.kit.log.IoGameLoggerFactory;
import lombok.Setter;
import org.slf4j.Logger;

/**
 * @author 渔民小镇
 * @date 2022-10-26
 */
public class ChangeUserIdMessageBrokerSyncProcessor extends SyncUserProcessor<SettingUserIdMessage> implements BrokerServerAware {
    static final Logger log = IoGameLoggerFactory.getLoggerCommon();

    @Setter
    BrokerServer brokerServer;

    @Override
    public Object handleRequest(BizContext bizCtx, SettingUserIdMessage settingUserIdMessage) throws Exception {

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
            return brokerClientProxy.invokeSync(settingUserIdMessage);
        } catch (RemotingException | InterruptedException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    @Override
    public String interest() {
        return SettingUserIdMessage.class.getName();
    }
}
