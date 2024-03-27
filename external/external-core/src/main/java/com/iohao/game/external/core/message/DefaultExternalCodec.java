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
import com.iohao.game.external.core.config.ExternalGlobalConfig;

/**
 * 默认的游戏对外服协议编解码
 * <pre>
 *     使用默认的游戏对外服协议 {@link ExternalMessage}
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-12-15
 * @see ExternalMessage
 */
@SuppressWarnings("unchecked")
public final class DefaultExternalCodec implements ExternalCodec {
    @Override
    public <T> T convertExternalMessage(BarMessage message) {
        // 游戏框架内置的协议， 与游戏前端相互通讯的协议
        ExternalMessage externalMessage = createExternalMessage();
        HeadMetadata headMetadata = message.getHeadMetadata();
        // 路由
        externalMessage.setCmdMerge(headMetadata.getCmdMerge());
        // 业务数据
        externalMessage.setData(message.getData());
        // 状态码
        externalMessage.setResponseStatus(message.getResponseStatus());
        // 验证信息（异常消息）
        externalMessage.setValidMsg(message.getValidatorMsg());
        // 消息标记号；由前端请求时设置，服务器响应时会携带上
        externalMessage.setMsgId(headMetadata.getMsgId());
        // 开发者自定义数据
        externalMessage.setCustomData(headMetadata.getCustomData());
        // 请求命令类型: 0 心跳，1 业务
        externalMessage.setCmdCode(headMetadata.getCmdCode());
        externalMessage.setOther(headMetadata.getInetSocketAddress());

        return (T) externalMessage;
    }

    @Override
    public <T> T createExternalMessage() {
        // 游戏框架内置的协议， 与游戏前端相互通讯的协议
        ExternalMessage externalMessage = new ExternalMessage();
        // 请求命令类型: 0 心跳，1 业务
        externalMessage.setCmdCode(ExternalMessageCmdCode.biz);
        // 协议开关，用于一些协议级别的开关控制，比如 安全加密校验等。 : 0 不校验
        externalMessage.setProtocolSwitch(ExternalGlobalConfig.protocolSwitch);
        return (T) externalMessage;
    }

    @Override
    public RequestMessage convertRequest(Object externalMsg) {
        ExternalMessage externalMessage = (ExternalMessage) externalMsg;
        int cmdMerge = externalMessage.getCmdMerge();

        // 元信息
        HeadMetadata headMetadata = new HeadMetadata()
                .setCmdMerge(cmdMerge)
                .setRpcCommandType(RpcCommandType.REQUEST_ONEWAY)
                .setMsgId(externalMessage.getMsgId())
                .setCmdCode(externalMessage.getCmdCode())
                .setCustomData(externalMessage.getCustomData());

        byte[] data = externalMessage.getData();

        // 请求
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setResponseStatus(externalMessage.getResponseStatus());
        requestMessage.setValidatorMsg(externalMessage.getValidMsg());
        requestMessage.setHeadMetadata(headMetadata);
        requestMessage.setData(data);

        return requestMessage;
    }
}