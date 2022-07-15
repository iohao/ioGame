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
public class MsgException extends Exception {
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
