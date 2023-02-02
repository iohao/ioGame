/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License..
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
