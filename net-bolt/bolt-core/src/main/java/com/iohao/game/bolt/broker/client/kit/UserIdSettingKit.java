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
package com.iohao.game.bolt.broker.client.kit;

import com.alipay.remoting.exception.RemotingException;
import com.iohao.game.action.skeleton.core.commumication.BrokerClientContext;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.action.skeleton.core.flow.attr.FlowAttr;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.message.SettingUserIdMessage;
import com.iohao.game.bolt.broker.core.message.SettingUserIdMessageResponse;
import com.iohao.game.common.kit.TimeKit;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * 变更用户 id
 * <pre>
 *     <a href="https://www.yuque.com/iohao/game/tywkqv">用户连接登录编写 文档</a>
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-01-19
 */
@Slf4j
@UtilityClass
public class UserIdSettingKit {
    /**
     * 设置用户的 userId
     * <pre>
     *     玩家真正的登录，只有设置了用户的 id， 才算是验证通过
     * </pre>
     *
     * @param flowContext 业务框架 flow上下文
     * @param userId      一般从数据库中获取
     * @return true 变更成功
     */
    public boolean settingUserId(FlowContext flowContext, long userId) {

        if (userId <= 0) {
            log.error("The userId must be greater than 0");
            return false;
        }

        HeadMetadata headMetadata = flowContext.getHeadMetadata();
        //  FlowContext 存在 userId 表示已经登录过了，返回 false
        if (headMetadata.getUserId() != 0) {
            log.error("玩家[{}] 已经登录", headMetadata.getUserId());
            return false;
        }

        // 一般指用户的 channelId （来源于对外服的 channel 长连接）
        String userChannelId = headMetadata.getChannelId();
        SettingUserIdMessage userIdMessage = new SettingUserIdMessage()
                .setUserId(userId)
                .setUserChannelId(userChannelId)
                .setHeadMetadata(headMetadata)
                .setStartTime(TimeKit.currentTimeMillis());

        if (log.isDebugEnabled()) {
            log.debug("1 逻辑服 {}", userIdMessage);
        }

        try {
            BrokerClientContext brokerClientContext = flowContext.option(FlowAttr.brokerClientContext);
            BrokerClient brokerClient = (BrokerClient) brokerClientContext;
            // 请求网关 （实际上是请求对外服，让对外服保存 userId）
            SettingUserIdMessageResponse settingUserIdMessageResponse = (SettingUserIdMessageResponse) brokerClient
                    .invokeSync(userIdMessage);

            if (log.isDebugEnabled()) {
                log.debug("5 逻辑服 {}", settingUserIdMessageResponse);
            }

            if (Objects.isNull(settingUserIdMessageResponse) || !settingUserIdMessageResponse.isSuccess()) {
                return false;
            }

            if (log.isDebugEnabled()) {
                log.debug("~~~~~ consumer time ~~~~~ {}"
                        , settingUserIdMessageResponse.getEndTime() - userIdMessage.getStartTime());
            }

        } catch (RemotingException | InterruptedException e) {
            log.error(e.getMessage(), e);
            return false;
        }

        headMetadata.setUserId(userId);

        return true;
    }
}
