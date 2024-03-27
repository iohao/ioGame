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

import com.alipay.remoting.rpc.RpcCommandType;
import com.iohao.game.action.skeleton.protocol.BarMessage;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.external.core.config.ExternalGlobalConfig;

/**
 * 游戏对外服协议编解码
 * <pre>
 *     开发者可自定义游戏对外服协议
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-12-15
 */
public interface ExternalCodec {
    /**
     * 创建 ResponseMessage
     *
     * @return ResponseMessage
     */
    default ResponseMessage createResponse() {
        var headMetadata = new HeadMetadata();
        // 请求命令类型: 0 心跳，1 业务
        headMetadata.setCmdCode(ExternalMessageCmdCode.biz);
        // 协议开关，用于一些协议级别的开关控制，比如 安全加密校验等。 : 0 不校验
        headMetadata.setProtocolSwitch(ExternalGlobalConfig.protocolSwitch);

        var responseMessage = new ResponseMessage();
        responseMessage.setHeadMetadata(headMetadata);
        return responseMessage;
    }

    default RequestMessage createRequest() {
        var headMetadata = new HeadMetadata();
        // 请求命令类型: 0 心跳，1 业务
        headMetadata.setCmdCode(ExternalMessageCmdCode.biz);
        // 协议开关，用于一些协议级别的开关控制，比如 安全加密校验等。 : 0 不校验
        headMetadata.setProtocolSwitch(ExternalGlobalConfig.protocolSwitch);
        headMetadata.setRpcCommandType(RpcCommandType.REQUEST_ONEWAY);

        var requestMessage = new RequestMessage();
        requestMessage.setHeadMetadata(headMetadata);
        return requestMessage;
    }

    /**
     * 将 BarMessage 转为游戏对外服协议
     *
     * @param message BarMessage
     * @param <T>     t
     * @return 游戏对外服协议
     */
    <T> T convertExternalMessage(BarMessage message);

    /**
     * 创建游戏对外服协议
     *
     * @param <T> t
     * @return 游戏对外服协议
     */
    <T> T createExternalMessage();

    /**
     * 将游戏对外服协议转为 RequestMessage
     *
     * @param externalMessage 游戏对外服协议
     * @return RequestMessage
     */
    RequestMessage convertRequest(Object externalMessage);
}
