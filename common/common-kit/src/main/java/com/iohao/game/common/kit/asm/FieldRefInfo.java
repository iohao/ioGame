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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * 字段信息 - 对应类的成员属性
 * <pre>
 *    name
 *    age
 * </pre>
 * <pre>
 *     不能提供私有属性的直接方法
 *     属性必须拥有 getter setter
 *     如果要调用私有属性, 使用 Field
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-01-02
 */
@ToString
@Getter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PACKAGE)
public class FieldRefInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = -5717006947739357125L;
    /** 字段名 */
    String fieldName;
    /** 返回值类型 */
    Class<?> returnType;
    /** 字段对应的 get 方法下标 */
    int methodGetIndex;
    /** 字段对应的 set 方法下标 */
    int methodSetIndex;
    /** 字段对应的 get 方法名 */
    String methodGetName;
    /** 字段对应的 set 方法名 */
    String methodSetName;
    /** 原生字段 */
    Field field;
}