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
package com.iohao.game.action.skeleton.protocol.external;

import com.iohao.game.action.skeleton.core.exception.MsgException;
import com.iohao.game.action.skeleton.core.exception.MsgExceptionInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 渔民小镇
 * @date 2022-07-27
 */
@Getter
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class ResponseCollectExternalItemMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 8687159017486669115L;

    /** 由框架赋值 */
    String logicServerId;
    /** 响应码: 0:成功, 其他表示有错误 */
    int responseStatus;
    /** 错误消息 */
    String errorMsg;
    /** 响应数据，通常是（游戏对外服提供） */
    Serializable data;

    public boolean success() {
        return responseStatus == 0;
    }

    public void setError(MsgExceptionInfo msgExceptionInfo) {
        this.responseStatus = msgExceptionInfo.getCode();
        this.errorMsg = msgExceptionInfo.getMsg();
    }

    public void setError(MsgException msgException) {
        this.responseStatus = msgException.getMsgCode();
        this.errorMsg = msgException.getMessage();
    }

    @SuppressWarnings("unchecked")
    public <T> T getData() {
        return (T) data;
    }
}
