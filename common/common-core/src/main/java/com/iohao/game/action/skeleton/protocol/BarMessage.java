/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
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

import com.alibaba.fastjson2.annotation.JSONField;
import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.action.skeleton.core.exception.ActionErrorEnum;
import com.iohao.game.action.skeleton.core.exception.MsgExceptionInfo;
import com.iohao.game.common.kit.ToJson;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

/**
 * 消息基类
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PROTECTED)
@Accessors(chain = true)
public abstract sealed class BarMessage implements Serializable, ToJson permits RequestMessage, ResponseMessage {
    @Serial
    private static final long serialVersionUID = 562068269463876111L;

    /** 响应码: 0:成功, 其他表示有错误 */
    int responseStatus;
    /** 异常信息、JSR380 验证信息 */
    String validatorMsg;

    /** 元信息 */
    HeadMetadata headMetadata;

    /**
     * 业务数据的 class 信息
     * <pre>
     *     https://gitee.com/iohao/iogame/issues/I5G0FC
     * </pre>
     */
    String dataClass;
    /** 实际请求的业务参数 byte[] */
    @JSONField(serialize = false)
    byte[] data;

    public BarMessage setData(byte[] data) {
        this.data = data;
        return this;
    }

    public BarMessage setData(Object data) {
        // 保存一下业务数据的 class
        this.dataClass = data.getClass().getName();

        byte[] bytes = DataCodecKit.encode(data);
        return this.setData(bytes);
    }

    /**
     * 设置验证的错误信息
     *
     * @param validatorMsg 错误信息
     * @return this
     */
    public BarMessage setValidatorMsg(String validatorMsg) {
        if (validatorMsg != null) {
            this.validatorMsg = validatorMsg;
            this.responseStatus = ActionErrorEnum.validateErrCode.getCode();
        }

        return this;
    }

    public BarMessage setError(MsgExceptionInfo msgExceptionInfo) {
        this.responseStatus = msgExceptionInfo.getCode();
        this.validatorMsg = msgExceptionInfo.getMsg();
        return this;
    }

    /**
     * 是否有错误
     * <pre>
     *     this.errorCode != 0 表示有错误
     * </pre>
     *
     * @return true 有错误码
     */
    public boolean hasError() {
        return this.responseStatus != 0;
    }
}
