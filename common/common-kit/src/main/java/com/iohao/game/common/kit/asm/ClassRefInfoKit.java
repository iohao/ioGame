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
package com.iohao.game.common.kit.asm;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import com.iohao.game.common.kit.MoreKit;
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
            ClassRefInfo newValue = createClassRefInfo(clazz);
            return MoreKit.putIfAbsent(classRefInfoMap, clazz, newValue);
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
