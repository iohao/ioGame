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
package com.iohao.game.action.skeleton.core.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * 业务框架 异常消息
 * <pre>
 *     关于异常机制的解释可以参考这里:
 *     https://www.yuque.com/iohao/game/avlo99
 * </pre>
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
@Getter
@Setter
public class MsgException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -4977523514509693190L;

    /** 异常消息码 */
    final int msgCode;

    public MsgException(int msgCode, String message) {
        super(message);
        this.msgCode = msgCode;
    }

    public MsgException(MsgExceptionInfo msgExceptionInfo) {
        this(msgExceptionInfo.getCode(), msgExceptionInfo.getMsg());
    }
}
