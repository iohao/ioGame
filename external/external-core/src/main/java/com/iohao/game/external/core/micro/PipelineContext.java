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
package com.iohao.game.external.core.micro;

import java.util.Objects;

/**
 * Pipeline 上下文
 *
 * @author 渔民小镇
 * @date 2023-02-19
 */
public interface PipelineContext {
    /**
     * 把处理器添加到第一个位置
     *
     * @param handler 处理器
     */
    default void addFirst(Object handler) {
        Objects.requireNonNull(handler);
        String simpleName = handler.getClass().getSimpleName();
        this.addFirst(simpleName, handler);
    }

    /**
     * 把处理器添加到第一个位置
     *
     * @param name    处理器的名称
     * @param handler 处理器
     */
    void addFirst(String name, Object handler);

    /**
     * 把处理器添加到最后的位置
     *
     * @param handler 处理器
     */
    default void addLast(Object handler) {
        Objects.requireNonNull(handler);
        String simpleName = handler.getClass().getSimpleName();
        this.addLast(simpleName, handler);
    }

    /**
     * 把处理器添加到最后的位置
     *
     * @param name    处理器的名称
     * @param handler 处理器
     */
    void addLast(String name, Object handler);

    /**
     * 移除指定处理器
     *
     * @param name 处理器的名称
     */
    void remove(String name);
}
