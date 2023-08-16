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
package com.iohao.game.action.skeleton.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.lang.annotation.Annotation;

/**
 * 依赖注入的部分
 * <pre>
 *     通常用于集成到第三方框架，如：
 *     spring
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-10-25
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class DependencyInjectionPart {

    /** true 与第三方框架集成 */
    boolean injection;
    /**
     * 容器管标签
     * <pre>
     *     比如 spring 可以使用 Component 来标记类是交给容器管理的；
     *
     *     虽然 spring 还支持其他的注解来标记类可以交给容器管理，
     *     但 ioGame 只推荐大家使用统一的一个就好了；
     *
     *     如果要把 ioGame 集成到其他框架中，也是大概类似的处理方式；
     * </pre>
     */
    Class<? extends Annotation> annotationClass;

    /** 当前使用的 ActionFactoryBean */
    ActionFactoryBean<?> actionFactoryBean;

    public boolean deliveryContainer(Class<?> controllerClazz) {
        return controllerClazz.getAnnotation(annotationClass) != null;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(ActionCommand actionCommand) {
        return (T) actionFactoryBean.getBean(actionCommand);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<?> actionControllerClazz) {
        return (T) actionFactoryBean.getBean(actionControllerClazz);
    }

    private DependencyInjectionPart() {

    }

    public static DependencyInjectionPart me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final DependencyInjectionPart ME = new DependencyInjectionPart();
    }
}
