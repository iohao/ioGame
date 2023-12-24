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
package com.iohao.game.action.skeleton.eventbus;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * 订阅者
 * <pre>
 *     通常由添加了 {@link EventSubscribe} 注解的方法转换而来
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-12-24
 * @see EventSubscribe
 */
@Slf4j
@Setter
@Accessors(chain = true)
public final class Subscriber {
    final long threadIndex;
    /** 方法访问器 */
    MethodAccess methodAccess;
    /** 类访问器 */
    ConstructorAccess<?> constructorAccess;
    /** 方法名 */
    String methodName;
    /** EventSubscribe class */
    Class<?> targetClazz;
    Object target;
    /** 方法对象 */
    Method method;
    /** 方法下标 (配合 MethodAccess 使用) */
    int methodIndex;
    /** 方法参数名 */
    String parameterName;
    /** 方法参数类型 */
    Class<?> parameterClass;
    /** 执行器选择策略 */
    @Getter
    EventSubscribe.ExecutorSelector executorSelect;

    Subscriber(long threadIndex) {
        this.threadIndex = threadIndex;
    }

    void invoke(Object param) {
        try {
            // 调用开发者在 action 类中编写的业务方法，即 action
            methodAccess.invoke(target, methodIndex, param);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }
}
