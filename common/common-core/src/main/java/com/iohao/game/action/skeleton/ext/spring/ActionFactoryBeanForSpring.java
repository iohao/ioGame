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
package com.iohao.game.action.skeleton.ext.spring;

import com.iohao.game.action.skeleton.core.ActionCommand;
import com.iohao.game.action.skeleton.core.ActionFactoryBean;
import com.iohao.game.action.skeleton.core.DependencyInjectionPart;
import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

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
    public T getBean(Class<?> actionControllerClazz) {
        return (T) this.applicationContext.getBean(actionControllerClazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Objects.requireNonNull(applicationContext);

        initDependencyInjectionPart();

        this.spring = true;

        this.applicationContext = applicationContext;
    }

    private void initDependencyInjectionPart() {
        DependencyInjectionPart dependencyInjectionPart = DependencyInjectionPart.me();

        dependencyInjectionPart.setInjection(true);

        dependencyInjectionPart.setAnnotationClass(Component.class);

        dependencyInjectionPart.setActionFactoryBean(this);
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
