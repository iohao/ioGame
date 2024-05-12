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
package com.iohao.game.external.core.message;

import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.core.exception.ActionErrorEnum;
import com.iohao.game.action.skeleton.core.exception.MsgExceptionInfo;
import com.iohao.game.action.skeleton.protocol.BarMessage;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.message.BroadcastMessage;
import com.iohao.game.bolt.broker.core.message.BrokerClientModuleMessage;
import com.iohao.game.common.consts.CommonConst;
import com.iohao.game.common.kit.CollKit;
import com.iohao.game.external.core.session.UserSessions;
import lombok.experimental.UtilityClass;

import java.util.Collection;

/**
 * 游戏对外服协议编解码工具
 *
 * @author 渔民小镇
 * @date 2023-12-15
 */
@UtilityClass
public class ExternalCodecKit {
    public ExternalCodec externalCodec = new DefaultExternalCodec();

    public BarMessage createIdleMessage() {
        BarMessage response = externalCodec.createResponse();
        HeadMetadata headMetadata = response.getHeadMetadata();

        // 请求命令类型: 心跳
        headMetadata.setCmdCode(ExternalMessageCmdCode.idle);
        return response;
    }

    public BarMessage createErrorIdleMessage(ActionErrorEnum idleErrorCode) {
        BarMessage response = createIdleMessage();
        // 错误码
        response.setResponseStatus(idleErrorCode.getCode());
        // 错误消息
        response.setValidatorMsg(idleErrorCode.getMsg());
        return response;
    }

    public RequestMessage createRequest() {
        return externalCodec.createRequest();
    }

    public RequestMessage createRequest(CmdInfo cmdInfo) {
        RequestMessage request = createRequest();
        request.getHeadMetadata().setCmdInfo(cmdInfo);
        return request;
    }

    public ResponseMessage createResponse() {
        return externalCodec.createResponse();
    }

    public <T> T convertExternalMessage(BarMessage responseMessage) {
        return externalCodec.convertExternalMessage(responseMessage);
    }

    public <T> T createExternalMessage() {
        return externalCodec.createExternalMessage();
    }

    public RequestMessage convertRequestMessage(Object externalMessage) {
        return externalCodec.convertRequest(externalMessage);
    }

    public void employError(BarMessage message, MsgExceptionInfo exceptionInfo) {
        message.setResponseStatus(exceptionInfo.getCode());
        message.setValidatorMsg(exceptionInfo.getMsg());
        message.setData(CommonConst.emptyBytes);
    }

    public void broadcast(BroadcastMessage message, UserSessions<?, ?> userSessions) {
        ResponseMessage responseMessage = message.getResponseMessage();
        HeadMetadata headMetadata = responseMessage.getHeadMetadata();
        headMetadata.setCmdCode(ExternalMessageCmdCode.biz);

        // 推送消息给全服真实用户
        if (message.isBroadcastAll()) {
            userSessions.broadcast(responseMessage);
            return;
        }

        // 推送消息给指定的真实用户列表
        Collection<Long> userIdList = message.getUserIdList();
        if (CollKit.notEmpty(userIdList)) {
            userSessions.ifPresent(userIdList, userSession -> userSession.writeAndFlush(responseMessage));
            return;
        }

        // 推送消息给单个真实用户
        long userId = headMetadata.getUserId();

        userSessions.ifPresent(userId, userSession -> userSession.writeAndFlush(responseMessage));
    }

    public void employ(BarMessage message, BrokerClient brokerClient) {
        // 设置当前逻辑服 id
        BrokerClientModuleMessage moduleMessage = brokerClient.getBrokerClientModuleMessage();
        int idHash = moduleMessage.getIdHash();
        HeadMetadata headMetadata = message.getHeadMetadata();
        headMetadata.setSourceClientId(idHash);
    }
}
