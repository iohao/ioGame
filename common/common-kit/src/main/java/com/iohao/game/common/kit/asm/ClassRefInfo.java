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

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Map;

/**
 * class asm相关信息
 * <pre>
 *     创建对象
 *     获取方法
 *     获取字段
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-01-02
 */
@ToString
@Getter
@FieldDefaults(level = AccessLevel.PACKAGE)
public final class ClassRefInfo implements Serializable {
    private static final long serialVersionUID = -4297558765639660029L;
    /** 类信息 */
    Class<?> clazz;
    /** 以属性名作为key */
    Map<String, FieldRefInfo> filedRefInfoMap;
    /** 构造 访问器 */
    ConstructorAccess<?> constructorAccess;
    /** 方法 访问器 */
    MethodAccess methodAccess;
    /** 以方法名作为key */
    Map<String, MethodRefInfo> methodRefInfoMap;

    ClassRefInfo() {
    }

    /**
     * 创建构建器
     *
     * @return 构建器
     */
    static ClassRefInfoBuilder newBuilder() {
        return new ClassRefInfoBuilder();
    }

    /**
     * 创建一个实例
     *
     * @param <T> t
     * @return 对象
     */
    @SuppressWarnings("unchecked")
    public <T> T newInstance() {
        return (T) this.constructorAccess.newInstance();
    }

    /**
     * 字段反射信息
     *
     * @param filedName 字段名
     * @return 字段信息
     */
    public FieldRefInfo getFieldRefInfo(String filedName) {
        return this.filedRefInfoMap.get(filedName);
    }

    /**
     * 方法反射信息
     *
     * @param methodName 方法名
     * @return 方法信息
     */
    public MethodRefInfo getMethodRefInfo(String methodName) {
        return this.methodRefInfoMap.get(methodName);
    }

    /**
     * 执行方法
     *
     * @param object     业务对象
     * @param methodName 方法名
     * @param args       参数
     * @return 返回值
     */
    public Object invokeMethod(Object object, String methodName, Object args) {
        MethodRefInfo methodRefInfo = getMethodRefInfo(methodName);
        return methodRefInfo.invokeMethod(object, args);
    }

    /**
     * 从对象中获取字段属性值
     *
     * @param object    对象
     * @param filedName 字段名
     * @param <T>       t
     * @return 字段属性值
     */
    @SuppressWarnings("unchecked")
    public <T> T invokeGetter(Object object, String filedName) {
        FieldRefInfo filedRefInfo = filedRefInfoMap.get(filedName);

        String methodName = filedRefInfo.getMethodGetName();
        MethodRefInfo methodRefInfo = getMethodRefInfo(methodName);

        return (T) methodRefInfo.invokeMethod(object);
    }

    /**
     * 设置对象中的字段属性值
     *
     * @param object    对象
     * @param filedName 字段名
     * @param value     设置的值
     * @return me
     */
    public ClassRefInfo invokeSetter(Object object, String filedName, Object value) {
        FieldRefInfo filedRefInfo = filedRefInfoMap.get(filedName);

        String methodName = filedRefInfo.getMethodGetName();
        MethodRefInfo methodRefInfo = getMethodRefInfo(methodName);

        methodRefInfo.invokeMethod(object, value);
        return this;
    }

}
