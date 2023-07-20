/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
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
package com.iohao.game.common.kit;

import lombok.experimental.UtilityClass;

import java.util.Set;

/**
 * 基础类型相关工具
 *
 * @author 渔民小镇
 * @date 2022-01-16
 */
@UtilityClass
public class BaseTypeKit {
    /** 基础类型 */
    private Set<Class<?>> baseTypeSet = Set.of(
            Byte.class,
            Short.class,
            Integer.class,
            Long.class,
            Character.class,
            Double.class,
            Float.class,
            String.class
    );


    /**
     * 验证该对象是否基础类型
     * <pre>
     *     基础类型包括:
     *             Byte.class,
     *             Short.class,
     *             Integer.class,
     *             Long.class,
     *             Character.class,
     *             Double.class,
     *             Float.class,
     *             String.class
     * </pre>
     *
     * @param value 验证对象
     * @return true 是基础类型
     */
    public boolean isBaseType(Object value) {
        Class<?> beanClass = value.getClass();
        return baseTypeSet.contains(beanClass);
    }

    /**
     * 验证该对象是否基础类型
     * <pre>
     *     基础类型包括:
     *             Byte.class,
     *             Short.class,
     *             Integer.class,
     *             Long.class,
     *             Character.class,
     *             Double.class,
     *             Float.class,
     *             String.class
     * </pre>
     *
     * @param beanClass 验证 class
     * @return true 是基础类型
     */
    public boolean isBaseType(Class<?> beanClass) {
        return baseTypeSet.contains(beanClass);
    }
}
