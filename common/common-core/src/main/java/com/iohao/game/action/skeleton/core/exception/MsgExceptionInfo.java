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

import java.util.Objects;

/**
 * 异常消息
 * <pre>
 *     关于异常机制的解释可以参考这里:
 *     https://www.yuque.com/iohao/game/avlo99
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-01-14
 */
public interface MsgExceptionInfo {
    /**
     * 异常消息
     *
     * @return 消息
     */
    String getMsg();

    /**
     * 异常码
     *
     * @return 异常码
     */
    int getCode();

    /**
     * 断言为 true, 就抛出异常
     *
     * @param v1 断言值
     * @throws MsgException e
     */
    default void assertTrueThrows(boolean v1) throws MsgException {
        if (v1) {
            throw new MsgException(this);
        }
    }

    /**
     * 断言必须是 true, 否则抛出异常
     *
     * @param v1 断言值
     * @throws MsgException e
     */
    default void assertTrue(boolean v1) throws MsgException {
        if (v1) {
            return;
        }

        throw new MsgException(this);
    }

    /**
     * 断言必须是 非null, 否则抛出异常
     *
     * @param value 断言值
     * @throws MsgException e
     */
    default void assertNonNull(Object value) throws MsgException {
        assertTrue(Objects.nonNull(value));
    }

    /**
     * 断言必须是 false, 否则抛出异常
     *
     * @param v1 断言值
     * @throws MsgException e
     */
    default void assertFalse(boolean v1) throws MsgException {
        this.assertTrue(!v1);
    }

    /**
     * 断言必须是 false, 否则抛出异常
     *
     * @param v1  断言值
     * @param msg 自定义消息
     * @throws MsgException e
     */
    default void assertFalse(boolean v1, String msg) throws MsgException {
        this.assertTrue(!v1, msg);
    }


    /**
     * 断言必须是 true, 否则抛出异常
     *
     * @param v1  断言值
     * @param msg 自定义消息
     * @throws MsgException e
     */
    default void assertTrue(boolean v1, String msg) throws MsgException {
        if (v1) {
            return;
        }

        int code = this.getCode();
        throw new MsgException(code, msg);
    }
}
