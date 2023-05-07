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
    /** 数据不存在 */
    dataNotExist(-1006, "数据不存在"),
    /** 强制玩家下线 */
    forcedOffline(-1007, "强制玩家下线"),
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
