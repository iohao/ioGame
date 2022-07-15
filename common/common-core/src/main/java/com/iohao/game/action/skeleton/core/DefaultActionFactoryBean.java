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
package com.iohao.game.action.skeleton.core;

import com.iohao.game.action.skeleton.ext.spring.ActionFactoryBeanForSpring;

/**
 * action 工厂对象 <BR>
 *
 * @author 渔民小镇
 * @date 2021-12-12
 */
public final class DefaultActionFactoryBean<T> implements ActionFactoryBean<T> {
    @Override
    @SuppressWarnings("unchecked")
    public T getBean(ActionCommand actionCommand) {

        if (actionCommand.deliveryContainer && ActionFactoryBeanForSpring.me().isSpring()) {
            ActionFactoryBeanForSpring beanForSpring = ActionFactoryBeanForSpring.me();
            return (T) beanForSpring.getBean(actionCommand);
        }

        if (actionCommand.isCreateSingleActionCommandController()) {
            return (T) actionCommand.getActionController();
        }

        return (T) actionCommand.getActionControllerConstructorAccess().newInstance();
    }


    private DefaultActionFactoryBean() {

    }

    public static DefaultActionFactoryBean me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final DefaultActionFactoryBean ME = new DefaultActionFactoryBean();
    }
}
