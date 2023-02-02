/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
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

import com.iohao.game.action.skeleton.annotation.ActionController;

/**
 * action 类对象创建工厂
 * <pre>
 *     负责创建 action 类的实例化对象
 *
 *     对于 action 的解释可以参考这里:
 *     https://www.yuque.com/iohao/game/sqcevl
 * </pre>
 *
 * @param <T> t
 * @author 渔民小镇
 * @date 2021-12-20
 */
public interface ActionFactoryBean<T> {
    /**
     * 获取 action 类的对象
     * <pre>
     *     添加了 {@link ActionController} 注解的类，是一个 action 类
     * </pre>
     *
     * @param actionCommand actionCommand
     * @return action 类的实例化对象
     */
    T getBean(ActionCommand actionCommand);
}
