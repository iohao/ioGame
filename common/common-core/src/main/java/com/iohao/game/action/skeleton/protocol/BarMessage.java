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

import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.action.skeleton.core.exception.MsgExceptionInfo;
import com.iohao.game.common.consts.CommonConst;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * 消息基类
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract sealed class BarMessage implements Serializable permits RequestMessage, ResponseMessage {
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
     *     <a href="https://gitee.com/iohao/iogame/issues/I5G0FC">...</a>
     * </pre>
     */
    String dataClass;
    /** 实际请求的业务参数 byte[] */
    byte[] data;

    public BarMessage setData(byte[] data) {
        this.data = data;
        return this;
    }

    public BarMessage setData(Object data) {
        if (Objects.isNull(data)) {
            return this.setData(CommonConst.emptyBytes);
        }

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
