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
package com.iohao.game.external.core.kit;

import com.alipay.remoting.rpc.RpcCommandType;
import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.core.CmdKit;
import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.action.skeleton.core.exception.ActionErrorEnum;
import com.iohao.game.action.skeleton.core.exception.MsgExceptionInfo;
import com.iohao.game.action.skeleton.protocol.HeadMetadata;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.action.skeleton.protocol.ResponseMessage;
import com.iohao.game.bolt.broker.core.message.BroadcastMessage;
import com.iohao.game.common.consts.CommonConst;
import com.iohao.game.common.kit.CollKit;
import com.iohao.game.common.kit.HashKit;
import com.iohao.game.external.core.config.ExternalGlobalConfig;
import com.iohao.game.external.core.message.ExternalMessage;
import com.iohao.game.external.core.message.ExternalMessageCmdCode;
import com.iohao.game.external.core.session.UserSessions;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Objects;

/**
 * @author 渔民小镇
 * @date 2023-02-21
 */
@UtilityClass
public class ExternalKit {

    /**
     * 创建请求消息
     *
     * @param cmdMerge 路由 {@link CmdKit#merge(int, int)}
     * @param idHash   当前游戏对外服的 idHash
     * @return 请求消息
     */
    public RequestMessage createRequestMessage(int cmdMerge, int idHash) {
        return createRequestMessage(cmdMerge, idHash, null);
    }

    /**
     * 创建请求消息
     *
     * @param cmdMerge 路由 {@link CmdKit#merge(int, int)}
     * @param idHash   当前游戏对外服的 idHash
     * @param data     业务数据 byte[]
     * @return 请求消息
     */
    public RequestMessage createRequestMessage(int cmdMerge, int idHash, byte[] data) {
        // 元信息
        HeadMetadata headMetadata = new HeadMetadata()
                .setCmdMerge(cmdMerge)
                .setRpcCommandType(RpcCommandType.REQUEST_ONEWAY)
                .setSourceClientId(idHash);

        // 请求
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setHeadMetadata(headMetadata);

        requestMessage.setData(data);

        return requestMessage;
    }

    public RequestMessage convertRequestMessage(ExternalMessage externalMessage, int idHash) {
        int cmdMerge = externalMessage.getCmdMerge();

        // 元信息
        HeadMetadata headMetadata = new HeadMetadata()
                .setCmdMerge(cmdMerge)
                .setRpcCommandType(RpcCommandType.REQUEST_ONEWAY)
                .setSourceClientId(idHash)
                .setMsgId(externalMessage.getMsgId())
                .setCustomData(externalMessage.getCustomData());

        byte[] data = externalMessage.getData();

        if (externalMessage.getCmdCode() == ExternalMessageCmdCode.bizCache) {
            int cacheCondition = getCacheCondition(data);
            headMetadata.setCacheCondition(cacheCondition);
        }

        // 请求
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setHeadMetadata(headMetadata);
        requestMessage.setData(data);

        return requestMessage;
    }

    public ExternalMessage convertExternalMessage(ResponseMessage responseMessage) {
        HeadMetadata headMetadata = responseMessage.getHeadMetadata();

        // 游戏框架内置的协议， 与游戏前端相互通讯的协议
        ExternalMessage externalMessage = createExternalMessage();
        // 路由
        externalMessage.setCmdMerge(headMetadata.getCmdMerge());
        // 业务数据
        externalMessage.setData(responseMessage.getData());
        // 状态码
        externalMessage.setResponseStatus(responseMessage.getResponseStatus());
        // 验证信息（异常消息）
        externalMessage.setValidMsg(responseMessage.getValidatorMsg());
        // 消息标记号；由前端请求时设置，服务器响应时会携带上
        externalMessage.setMsgId(headMetadata.getMsgId());
        // 开发者自定义数据
        externalMessage.setCustomData(headMetadata.getCustomData());

        return externalMessage;
    }

    public ExternalMessage createExternalMessage() {
        // 游戏框架内置的协议， 与游戏前端相互通讯的协议
        ExternalMessage externalMessage = new ExternalMessage();
        // 请求命令类型: 0 心跳，1 业务
        externalMessage.setCmdCode(ExternalMessageCmdCode.biz);
        // 协议开关，用于一些协议级别的开关控制，比如 安全加密校验等。 : 0 不校验
        externalMessage.setProtocolSwitch(ExternalGlobalConfig.protocolSwitch);
        return externalMessage;
    }

    public ExternalMessage createIdleErrorMessage() {
        ExternalMessage externalMessage = ExternalKit.createExternalMessage();
        // 请求命令类型: 心跳
        externalMessage.setCmdCode(ExternalMessageCmdCode.idle);
        // 错误码
        externalMessage.setResponseStatus(ActionErrorEnum.idleErrorCode.getCode());
        // 错误消息
        externalMessage.setValidMsg(ActionErrorEnum.idleErrorCode.getMsg());

        return externalMessage;
    }

    public ExternalMessage createExternalMessage(CmdInfo cmdInfo, byte[] data) {
        return createExternalMessage(cmdInfo.getCmd(), cmdInfo.getSubCmd(), data);
    }

    public ExternalMessage createExternalMessage(CmdInfo cmdInfo, Object object) {
        return createExternalMessage(cmdInfo.getCmd(), cmdInfo.getSubCmd(), object);
    }

    public ExternalMessage createExternalMessage(CmdInfo cmdInfo) {
        ExternalMessage externalMessage = ExternalKit.createExternalMessage();
        externalMessage.setCmdMerge(cmdInfo.getCmdMerge());
        return externalMessage;
    }

    public ExternalMessage createExternalMessage(int cmd, int subCmd) {
        ExternalMessage externalMessage = ExternalKit.createExternalMessage();
        externalMessage.setCmdMerge(cmd, subCmd);
        return externalMessage;
    }

    public ExternalMessage createExternalMessage(int cmd, int subCmd, Object object) {
        byte[] data = null;

        if (object != null) {
            data = DataCodecKit.encode(object);
        }

        return ExternalKit.createExternalMessage(cmd, subCmd, data);
    }

    public ExternalMessage createExternalMessage(int cmd, int subCmd, byte[] data) {
        // 游戏框架内置的协议， 与游戏前端相互通讯的协议
        ExternalMessage externalMessage = ExternalKit.createExternalMessage(cmd, subCmd);

        // 业务数据
        externalMessage.setData(data);

        return externalMessage;
    }

    public void broadcast(BroadcastMessage message, UserSessions<?, ?> userSessions) {
        ResponseMessage responseMessage = message.getResponseMessage();
        ExternalMessage externalMessage = ExternalKit.convertExternalMessage(responseMessage);

        // 推送消息给全服真实用户
        if (message.isBroadcastAll()) {
            userSessions.broadcast(externalMessage);
            return;
        }

        // 推送消息给指定的真实用户列表
        Collection<Long> userIdList = message.getUserIdList();
        if (CollKit.notEmpty(userIdList)) {
            userSessions.ifPresent(userIdList, userSession -> userSession.writeAndFlush(externalMessage));
            return;
        }

        // 推送消息给单个真实用户
        HeadMetadata headMetadata = responseMessage.getHeadMetadata();
        long userId = headMetadata.getUserId();

        userSessions.ifPresent(userId, userSession -> userSession.writeAndFlush(externalMessage));
    }

    public void employError(ExternalMessage message, MsgExceptionInfo exceptionInfo) {
        message.setResponseStatus(exceptionInfo.getCode());
        message.setValidMsg(exceptionInfo.getMsg());
        message.setData(CommonConst.emptyBytes);
    }

    /**
     * byte[] 转 hash
     * <pre>
     *     缓存查询条件: 由请求参数计算出一个 hash 值。
     *     同一 action 条件参数的 hash 值碰撞的几率不是很大。
     *
     *     当条件参数不存在时，那么就是无参 action，使用 1 来表示。
     * </pre>
     *
     * @param data bytes
     * @return hash
     */
    public int getCacheCondition(byte[] data) {
        return Objects.nonNull(data) ? HashKit.hash32(data) : 1;
    }
}
