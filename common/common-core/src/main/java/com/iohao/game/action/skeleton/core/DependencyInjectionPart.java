/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
