/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General  License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General  License for more details.
 *
 * You should have received a copy of the GNU General  License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.iohao.game.action.skeleton.protocol;

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
}
