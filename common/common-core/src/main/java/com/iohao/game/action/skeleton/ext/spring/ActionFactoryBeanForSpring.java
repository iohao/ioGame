/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
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
package com.iohao.game.action.skeleton.ext.spring;

import com.iohao.game.action.skeleton.core.ActionCommand;
import com.iohao.game.action.skeleton.core.ActionFactoryBean;
import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Objects;

/**
 * spring集成
 * <pre>
 *     把 action 交由 spring 管理
 *
 *     对于 action 的解释可以参考这里:
 *     https://www.yuque.com/iohao/game/sqcevl
 * </pre>
 *
 * @author 渔民小镇
 * @date 2022-03-22
 */
@SuppressWarnings("unchecked")
public class ActionFactoryBeanForSpring<T> implements ActionFactoryBean<T>, ApplicationContextAware {

    private ApplicationContext applicationContext;
    @Getter
    boolean spring;

    @Override

    public T getBean(ActionCommand actionCommand) {
        Class<?> actionControllerClazz = actionCommand.getActionControllerClazz();
        return (T) this.applicationContext.getBean(actionControllerClazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Objects.requireNonNull(applicationContext);
        this.spring = true;

        this.applicationContext = applicationContext;
    }

    private ActionFactoryBeanForSpring() {
    }

    public static ActionFactoryBeanForSpring me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final ActionFactoryBeanForSpring ME = new ActionFactoryBeanForSpring();
    }
}
