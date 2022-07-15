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
package com.iohao.game.common.kit.asm;

import com.esotericsoftware.reflectasm.MethodAccess;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Method;

/**
 * 类方法信息
 *
 * @author 渔民小镇
 * @date 2022-01-02
 */
@Getter
@FieldDefaults(level = AccessLevel.PACKAGE)
public class MethodRefInfo {

    /** 方法下标 */
    int methodIndex;
    /** 方法名 */
    String methodName;
    /** 原生方法对象 */
    Method method;
    /** 方法 访问器 */
    MethodAccess methodAccess;

    MethodRefInfo() {

    }

    /**
     * 执行方法
     *
     * @param object 业务对象
     * @param args   方法参数
     * @return 返回值
     */
    public Object invokeMethod(Object object, Object args) {
        return methodAccess.invoke(object, methodIndex, args);
    }

    /**
     * 执行无参方法
     *
     * @param object 业务对象
     * @return 返回值
     */
    public Object invokeMethod(Object object) {
        return methodAccess.invoke(object, methodIndex);
    }
}
