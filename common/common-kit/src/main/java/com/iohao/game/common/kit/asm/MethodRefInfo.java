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
