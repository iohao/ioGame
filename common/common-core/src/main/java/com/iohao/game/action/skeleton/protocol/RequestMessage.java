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
package com.iohao.game.action.skeleton.protocol;

import com.iohao.game.action.skeleton.core.CmdInfo;

import java.io.Serial;

/**
 * 请求参数
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
public sealed class RequestMessage extends BarMessage permits SyncRequestMessage {
    @Serial
    private static final long serialVersionUID = 8564408386704453534L;

    public ResponseMessage createResponseMessage() {
        ResponseMessage responseMessage = new ResponseMessage();

        this.settingCommonAttr(responseMessage);

        return responseMessage;
    }

    public void settingCommonAttr(final ResponseMessage responseMessage) {
        // response 与 request 使用的 headMetadata 为同一引用
        responseMessage.setHeadMetadata(this.headMetadata);
    }

    /**
     * 设置自身属性到 request 中
     *
     * @param requestMessage request
     */
    public void copyTo(RequestMessage requestMessage) {
        requestMessage.responseStatus = this.responseStatus;
        requestMessage.validatorMsg = this.validatorMsg;
        requestMessage.headMetadata = this.headMetadata;
        requestMessage.dataClass = this.dataClass;
        requestMessage.data = this.data;
    }

    /**
     * 创建 RequestMessage 时，附带当前 RequestMessage 对象的一些信息
     * <pre>
     *     使用场景：与其他游戏逻辑服通信时可以使用
     * </pre>
     *
     * @param cmdInfo 路由
     * @return 新的 RequestMessage
     */
    public RequestMessage createRequestMessage(CmdInfo cmdInfo) {
        return createRequestMessage(cmdInfo, null);
    }

    /**
     * 创建 RequestMessage 时，附带当前 RequestMessage 对象的一些信息
     * <pre>
     *     使用场景：与其他游戏逻辑服通信时可以使用
     * </pre>
     *
     * @param cmdInfo 路由
     * @param data    请求参数
     * @return 新的 RequestMessage
     */
    public RequestMessage createRequestMessage(CmdInfo cmdInfo, Object data) {
        HeadMetadata metadata = this.headMetadata.cloneHeadMetadata();
        metadata.setCmdInfo(cmdInfo);

        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setHeadMetadata(metadata);
        requestMessage.setData(data);

        return requestMessage;
    }
}
