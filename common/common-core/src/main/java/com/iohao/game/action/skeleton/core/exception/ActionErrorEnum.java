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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * action 错误码
 * <pre>
 *     关于异常机制的解释可以参考这里:
 *     https://www.yuque.com/iohao/game/avlo99
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-01-14
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ActionErrorEnum implements MsgExceptionInfo {
    /**
     * 系统其它错误
     * <pre>
     *      一般不是用户自定义的异常，很可能是用户引入的第三方包抛出的异常
     * </pre>
     */
    systemOtherErrCode(-1000, "系统其它错误"),
    /** 参数验错误码 */
    validateErrCode(-1001, "参数验错误"),
    /** 路由错误码，一般是客户端请求了不存在的路由引起的 */
    cmdInfoErrorCode(-1002, "路由错误"),
    /** 心跳错误码 */
    idleErrorCode(-1003, "心跳超时相关"),
    /** 需要登录后才能调用业务方法 */
    verifyIdentity(-1004, "请先登录"),
    /** class 不存在 */
    classNotExist(-1005, "class 不存在"),
    ;

    /** 消息码 */
    final int code;
    /** 消息模板 */
    final String msg;

    ActionErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
