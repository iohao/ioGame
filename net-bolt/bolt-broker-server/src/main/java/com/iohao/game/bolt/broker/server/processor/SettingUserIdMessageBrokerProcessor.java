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
package com.iohao.game.bolt.broker.server.processor;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.exception.RemotingException;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.bolt.broker.core.common.AbstractAsyncUserProcessor;
import com.iohao.game.bolt.broker.core.message.SettingUserIdMessage;
import com.iohao.game.bolt.broker.core.message.SettingUserIdMessageResponse;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.bolt.broker.server.aware.BrokerServerAware;
import com.iohao.game.bolt.broker.server.balanced.BalancedManager;
import com.iohao.game.bolt.broker.server.balanced.region.BrokerClientProxy;
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
public class SettingUserIdMessageBrokerProcessor extends AbstractAsyncUserProcessor<SettingUserIdMessage>
        implements BrokerServerAware {
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

    @Override
    public String interest() {
        return SettingUserIdMessage.class.getName();
    }
}
