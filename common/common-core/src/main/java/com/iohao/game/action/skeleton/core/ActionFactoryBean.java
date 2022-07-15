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

import com.iohao.game.action.skeleton.annotation.ActionController;

/**
 * ActionController 工厂
 * <pre>
 *     负责创建 用户定义的 Action 业务方法
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
     * 获取 tcp ActionController 对象
     * <pre>
     * ActionController 对象.
     *     可以是任意java对象
     *     只要添加注解 {@link ActionController}
     *     都属于tcp ActionController 对象
     * </pre>
     *
     * @param actionCommand actionCommand
     * @return ActionController 对象
     */
    T getBean(ActionCommand actionCommand);
}
