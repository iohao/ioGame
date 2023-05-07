/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General  License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General  License for more details.
 *
 * You should have received a copy of the GNU General  License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
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
