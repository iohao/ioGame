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
import lombok.experimental.UtilityClass;
import org.jctools.maps.NonBlockingHashMap;

import java.util.Map;
import java.util.Objects;

/**
 * 类信息工具
 * <pre>
 *     包含了反射等信息
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-01-02
 */
@UtilityClass
public class ClassRefInfoKit {
    /**
     * key: 类信息
     * value: 类反射信息
     */
    private final Map<Class<?>, ClassRefInfo> classRefInfoMap = new NonBlockingHashMap<>();

    /**
     * 获取类信息
     *
     * @param clazz class
     * @return 类信息
     */
    public ClassRefInfo getClassRefInfo(Class<?> clazz) {
        ClassRefInfo classRefInfo = classRefInfoMap.get(clazz);

        // 无锁化
        if (Objects.isNull(classRefInfo)) {
            classRefInfo = createClassRefInfo(clazz);
            classRefInfo = classRefInfoMap.putIfAbsent(clazz, classRefInfo);
            if (Objects.isNull(classRefInfo)) {
                classRefInfo = classRefInfoMap.get(clazz);
            }
        }

        return classRefInfo;
    }

    /**
     * 创建类的反射信息
     *
     * @param clazz 类信息
     * @return 类反射信息
     */
    private ClassRefInfo createClassRefInfo(Class<?> clazz) {
        // 构造函数访问器
        ConstructorAccess<?> constructorAccess = ConstructorAccess.get(clazz);
        // 方法访问器
        MethodAccess methodAccess = MethodAccess.get(clazz);

        return ClassRefInfo.newBuilder()
                .setClazz(clazz)
                .setConstructorAccess(constructorAccess)
                .setMethodAccess(methodAccess)
                .build();
    }
}
