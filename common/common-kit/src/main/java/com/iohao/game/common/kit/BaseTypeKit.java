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
